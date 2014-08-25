/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.co.nickthecoder.prioritydoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

/**
 * Uses FreeMarker template to generate all of the javadoc comments.
 * The templates are held as resources in package : uk.co.nickthecoder.prioritydoc.templates.
 */
public class Generator
{
    protected Options options;

    protected RootDoc root;

    protected Configuration cfg;

    protected String filenameSuffix = ".html";

    /**
     * Maps a fully qualified class name to the set of classes that extend/implement it.
     */
    protected Map<String, Set<ClassDoc>> knownSubclasses;

    /**
     * Maps a fully qualified interface name to the set of classes that extend it.
     */
    protected Map<String, Set<ClassDoc>> knownSubinterfaces;

    /**
     * Create a Generator
     * 
     * @param root
     *        Details of all the classes etc to document. Not null.
     */
    public Generator( RootDoc root, Options options ) throws Exception
    {
        this.root = root;
        this.options = options;

        this.cfg = new Configuration();

        this.cfg.setClassForTemplateLoading(Generator.class, getTemplateBase());
        this.cfg.setObjectWrapper(new DefaultObjectWrapper());
        this.cfg.setDefaultEncoding("UTF-8");
        this.cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        this.cfg.setIncompatibleImprovements(new Version(2, 3, 20));
    }

    /**
     * This is passed into Configuration.setClassForTemplateLoading.
     * 
     * @return the base name for the resources.
     * @priority 2
     */
    public String getTemplateBase()
    {
        return "/uk/co/nickthecoder/prioritydoc/templates/";
    }

    /**
     * Gets the URL of a fixed resource, such as a css style sheet.
     */
    public URL getResource( String path )
    {
        return Generator.class.getResource("/uk/co/nickthecoder/prioritydoc/" + path);
    }

    /**
     * @return the absolute directory into which the give package's documentation should be written.
     * @priority 1
     */
    public File getDirectory( PackageDoc packageDoc )
    {
        String path = packageDoc.name().replace('.', File.separatorChar);
        File file = new File(this.options.destinationDirectory, path);
        return file;
    }

    /**
     * @return the absolute filename where the documentation will be written for the given class/interface/enum etc.
     * @priority 1
     */
    public File getFile( ClassDoc doc )
    {
        return new File(getDirectory(doc.containingPackage()), doc.name() + this.filenameSuffix);
    }

    /**
     * Generates everything.
     * This is the method called by {@link PriorityDoc}.
     * 
     * @priority 1
     */
    public void generate() throws Exception
    {
        cacheKnownHeirarchy();

        generateRoot();
        generatePackages(this.root.specifiedPackages());
        generateClasses(this.root.classes());
    }

    /**
     * Goes through every class, and discovers all of their subclasses, and implemented interfaces,
     * adding them to a collection, so that the relationships can be discovered in the opposite
     * direction. i.e. to find all know sub-classes for a given class.
     */
    protected void cacheKnownHeirarchy()
    {
        this.knownSubclasses = new HashMap<String, Set<ClassDoc>>();
        this.knownSubinterfaces = new HashMap<String, Set<ClassDoc>>();

        for (ClassDoc classDoc : this.root.classes()) {
            for (ClassDoc superDoc = classDoc.superclass(); superDoc != null; superDoc = superDoc.superclass()) {
                if (!superDoc.isIncluded()) {
                    break;
                }
                cacheKnownHeirarchy(classDoc, superDoc);

                for (ClassDoc superInterface : superDoc.interfaces()) {
                    if (!superInterface.isIncluded()) {
                        break;
                    }
                    cacheKnownHeirarchy(classDoc, superInterface);
                }
            }

            for (ClassDoc superInterface : classDoc.interfaces()) {
                if (!superInterface.isIncluded()) {
                    break;
                }
                cacheKnownHeirarchy(classDoc, superInterface);
            }
        }
    }

    protected void cacheKnownHeirarchy( ClassDoc classDoc, ClassDoc superDoc )
    {
        String name = superDoc.name();

        Map<String, Set<ClassDoc>> collection = classDoc.isInterface() ? this.knownSubinterfaces : this.knownSubclasses;

        Set<ClassDoc> subclasses = collection.get(name);
        if (subclasses == null) {
            subclasses = new TreeSet<ClassDoc>(NameComparator.instance);
            collection.put(name, subclasses);
        }
        subclasses.add(classDoc);
    }

    /**
     * Creates the destination directory and populates it with fixed documents, such as the css style sheet,
     * and creates an index page.
     */
    protected void generateRoot() throws Exception
    {
        new File(this.options.destinationDirectory).mkdirs();

        for (String resourcePath : getFixedResourcesPaths()) {
            File destination = new File(this.options.destinationDirectory, resourcePath);

            if (this.options.overwriteResources || !destination.exists()) {
                URL url = getResource(resourcePath);
                File parent = destination.getParentFile();
                parent.mkdirs();
                copy(url, destination);
            }
        }

        Map<String, Object> root = createModelRoot();

        String base = ".";
        root.put("base", base);

        List<ClassDoc> all = Arrays.asList(this.root.classes());
        Collections.sort(all, NameComparator.instance);
        root.put("sortedCombined", all);

        List<ClassDoc> classes = new ArrayList<ClassDoc>();
        List<ClassDoc> interfaces = new ArrayList<ClassDoc>();
        List<ClassDoc> exceptions = new ArrayList<ClassDoc>();
        List<ClassDoc> enums = new ArrayList<ClassDoc>();

        root.put("sortedClasses", classes);
        root.put("sortedInterfaces", interfaces);
        root.put("sortedExceptions", exceptions);
        root.put("sortedEnums", enums);

        for (ClassDoc doc : all) {
            if (doc.isEnum()) {
                enums.add(doc);
            } else if (doc.isInterface()) {
                interfaces.add(doc);
            } else if (doc.isException()) {
                exceptions.add(doc);
            } else {
                classes.add(doc);
            }
        }

        generate("index.ftl", root, new File(this.options.destinationDirectory, "index" + this.filenameSuffix));
        generate("package-list.ftl", root, new File(this.options.destinationDirectory, "package-list"));
    }

    /**
     * Generates an index page for each package.
     * 
     * @priority 2
     * @param packageDocs
     *        Not null.
     */
    protected void generatePackages( PackageDoc[] packageDocs ) throws Exception
    {
        for (PackageDoc packageDoc : packageDocs) {
            generatePackage(packageDoc);
        }
    }

    /**
     * Creates an index of all classes/interfaces/enums within the package.
     * 
     * @param packageDoc
     *        Not null.
     */
    protected void generatePackage( PackageDoc packageDoc ) throws Exception
    {
        File dir = getDirectory(packageDoc);
        dir.mkdirs();

        Map<String, Object> root = createModelRoot();
        root.put("package", packageDoc);

        String packageName = packageDoc.name();
        String base = packageName.replace(".", "/").replaceAll("[^/]*", ".");
        root.put("base", base);

        File destination = new File(dir, "index" + this.filenameSuffix);

        root.put("sortedCombined", sort(packageDoc.allClasses()));
        root.put("sortedClasses", sort(packageDoc.ordinaryClasses()));
        root.put("sortedEnums", sort(packageDoc.enums()));
        root.put("sortedInterfaces", sort(packageDoc.interfaces()));
        root.put("sortedExceptions", sort(packageDoc.exceptions()));

        generate("packageIndex.ftl", root, destination);
    }

    protected ProgramElementDoc[] sort( ProgramElementDoc[] docs )
    {
        Arrays.sort(docs, NameComparator.instance);
        return docs;
    }

    /**
     * Creates documentation for all classes.
     * 
     * @param classDocs
     *        Not null, may be empty.
     * @priority 2
     */
    protected void generateClasses( ClassDoc[] classDocs ) throws Exception
    {
        for (ClassDoc classDoc : classDocs) {
            generateClass(classDoc);
        }
    }

    /**
     * Creates the documentation for the given class.
     * 
     * @param classDoc
     *        Not null.
     * @priority 1
     */
    public void generateClass( ClassDoc classDoc ) throws Exception
    {
        Map<String, Object> root = createModelRoot();
        root.put("class", classDoc);
        String packageName = classDoc.containingPackage().name();
        String base = packageName.replace(".", "/").replaceAll("[^/]*", ".");
        root.put("base", base);

        File destination = getFile(classDoc);
        destination.getParentFile().mkdirs();

        Map<String, Object> combinedMap = new HashMap<String, Object>();

        combineMethods(combinedMap, classDoc);
        combineFields(combinedMap, classDoc);

        root.put("combinedClass", combinedMap);
        root.put("knownSubclasses", this.knownSubclasses.get(classDoc.name()));
        root.put("knownSubinterfaces", this.knownSubinterfaces.get(classDoc.name()));

        generate("class.ftl", root, destination);
    }

    Map<String, List<MethodDoc>> methodsByName;
    Map<String, List<MethodDoc>> staticMethodsByName;

    /**
     * Combines the methods from a class with those from its super classes (except Object).
     * 
     * @param classDoc
     *        , the sub-class which has all of its methods combined into a list. Not null.
     * 
     * @return A list of MethodDocs sorted by thier name.
     * 
     *         Methods that are overriden should NOT be duplicated. Will be empty if the class, nor its super classes
     *         (excluding Object) have methods.
     * @priority 3
     */
    protected void combineMethods( Map<String, Object> root, ClassDoc currentClassDoc )
    {
        Map<String, List<MethodDoc>> methodsByName = new HashMap<String, List<MethodDoc>>();
        Map<String, List<MethodDoc>> staticMethodsByName = new HashMap<String, List<MethodDoc>>();

        combineMethods2(methodsByName, staticMethodsByName, currentClassDoc, false);

        List<MethodDoc> methodDocs = new ArrayList<MethodDoc>();
        for (List<MethodDoc> l : methodsByName.values()) {
            methodDocs.addAll(l);
        }
        Collections.sort(methodDocs, NameComparator.instance);

        List<MethodDoc> staticMethodDocs = new ArrayList<MethodDoc>();
        for (List<MethodDoc> l : staticMethodsByName.values()) {
            staticMethodDocs.addAll(l);
        }
        Collections.sort(staticMethodDocs, NameComparator.instance);

        root.put("methods", methodDocs);
        root.put("staticMethods", staticMethodDocs);
    }

    protected void combineMethods2( Map<String, List<MethodDoc>> methodsByName, Map<String, List<MethodDoc>> staticMethodsByName,
        ClassDoc currentClassDoc, boolean isSuper )
    {
        // Exit the recursion when we get to the top of the hierarchy.
        if ((currentClassDoc == null) || (currentClassDoc.qualifiedName().equals("java.lang.Object"))) {
            return;
        }

        for (MethodDoc methodDoc : currentClassDoc.methods()) {
            // Ignore inherrited private methods
            if (methodDoc.isPrivate() && (isSuper)) {
                continue;
            }

            if (methodDoc.isStatic()) {
                addMethod(staticMethodsByName, methodDoc);
            } else {
                addMethod(methodsByName, methodDoc);
            }
        }

        if (currentClassDoc.isInterface()) {
            // Add ALL of the super interfaces declared methods
            for (ClassDoc inter : currentClassDoc.interfaces()) {
                combineMethods2(methodsByName, staticMethodsByName, inter, true);
            }
        } else {
            // Add the super classes methods
            combineMethods2(methodsByName, staticMethodsByName, currentClassDoc.superclass(), true);
        }

    }

    /**
     * Adds a method to the collection unless a methods with the same name, and same parameter lists
     * is already in the collection.
     * 
     * @param methodsByName
     *        , a map of lists of MethodDocs keyed on the method names.
     * @param item
     *        the item which may be added into the collection.
     * @priority 4
     */
    public void addMethod( Map<String, List<MethodDoc>> methodsByName, MethodDoc item )
    {
        List<MethodDoc> list;
        if (!methodsByName.containsKey(item.name())) {
            list = new ArrayList<MethodDoc>();
            methodsByName.put(item.name(), list);
            list.add(item);
        } else {
            list = methodsByName.get(item.name());
            for (MethodDoc method : list) {
                if (method.name().equals(item.name())) {
                    if (parametersMatch(method.parameters(), item.parameters())) {
                        // A matching method already exists, don't add it again.
                        return;
                    }
                }
            }
            list.add(item);
        }
    }

    protected boolean parametersMatch( Parameter[] p1s, Parameter[] p2s )
    {
        if (p1s.length != p2s.length) {
            return false;
        }

        int i = 0;
        for (Parameter p1 : p1s) {
            Parameter p2 = p2s[i];
            i++;
            if (!p1.type().toString().equals(p2.type().toString())) {
                return false;
            }

        }
        return true;
    }

    /**
     * Combines the methods from a class with those from its super classes (except Object).
     * 
     * @param classDoc
     *        , the sub-class which has all of its methods combined into a list. Not null.
     * 
     * @return A list of MethodDocs sorted by thier name.
     * 
     *         Methods that are overriden should NOT be duplicated. Will be empty if the class, nor its super classes
     *         (excluding Object) have methods.
     * @priority 3
     */
    protected void combineFields( Map<String, Object> root, ClassDoc classDoc )
    {
        Set<FieldDoc> fields = new TreeSet<FieldDoc>(NameComparator.instance);
        Set<FieldDoc> staticFields = new TreeSet<FieldDoc>(NameComparator.instance);

        ClassDoc currentClassDoc = classDoc;
        while ((currentClassDoc != null) && (!currentClassDoc.qualifiedName().equals("java.lang.Object"))) {

            for (FieldDoc fieldDoc : currentClassDoc.fields()) {
                if (fieldDoc.isPrivate() && (classDoc != currentClassDoc)) {
                    continue;
                }

                if (fieldDoc.isStatic()) {
                    staticFields.add(fieldDoc);
                } else {
                    fields.add(fieldDoc);
                }
            }

            currentClassDoc = currentClassDoc.superclass();
        }

        root.put("fields", fields);
        root.put("staticFields", staticFields);
    }

    /**
     * Copies the contents of a URL to a file.
     * Used to copy the fixed resources, such as the css style sheet.
     * 
     * @priority 3
     */
    protected void copy( URL source, File destination ) throws Exception
    {
        FileOutputStream out = null;
        InputStream in = null;
        URLConnection uc = null;
        try {
            uc = source.openConnection();
            uc.connect();
            in = uc.getInputStream();
            out = new FileOutputStream(destination);
            final int BUF_SIZE = 2048;
            byte[] buffer = new byte[BUF_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) > -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Returns a List of resource paths that should be copied, such as the css style sheet.
     * These are paths such as "css/style.css", "js/myscript.js", "images/foo.png"
     * 
     * @return The list of resource paths
     */
    protected List<String> getFixedResourcesPaths()
    {
        List<String> result = new ArrayList<String>();
        result.add("style.css");
        result.add("images/method.png");
        result.add("images/constructor.png");
        result.add("images/field.png");
        result.add("images/static_method.png");
        result.add("images/static_field.png");
        result.add("images/package.png");
        result.add("images/class.png");
        result.add("images/interface.png");
        result.add("images/enum.png");
        result.add("images/expand.png");
        result.add("images/exception.png");
        result.add("images/contract.png");
        result.add("images/index.png");
        result.add("images/favicon.png");
        result.add("images/fadeBackground.png");
        result.add("images/public.png");
        result.add("images/protected.png");
        result.add("images/private.png");
        result.add("jquery-2.1.1.min.js");
        result.add("jquery-ui.min.js");
        result.add("prioritydoc.js");

        return result;
    }

    /**
     * Creates the model for the root hash, which will be passed into the FreeMarker template engine.
     * This will be called for each document generated.
     * It should contain all data common to all generated documents.
     * The caller will have additional data specific to the document being generated.
     * 
     * @return a Map containing common data.
     * @priority 1
     */
    public Map<String, Object> createModelRoot()
    {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("base", ".");
        map.put("root", this.root);
        map.put("options", this.options);
        map.put("removeGenerics", RemoveGenerics.instance);

        return map;
    }

    protected void generate( String templateName, Map<String, Object> modelRoot, File destination ) throws Exception
    {

        Writer out = new OutputStreamWriter(new FileOutputStream(destination));
        try {
            Template template = this.cfg.getTemplate(templateName);

            template.process(modelRoot, out);
        } finally {
            out.close();
        }
    }

    /**
     * Allows program elements to be sorted by their name.
     */
    public static class NameComparator implements Comparator<ProgramElementDoc>
    {
        static final NameComparator instance = new NameComparator();

        /**
         * Compares just on the program element's name.
         */
        public int compare( ProgramElementDoc a, ProgramElementDoc b )
        {
            return a.name().compareTo(b.name());
        }
    }

}
