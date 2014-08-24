<!DOCTYPE html>
<html lang="en">
<#import "macros.ftl" as m>
<@m.page bas=base opt=options doc=class/>
<head>
  <@m.head title=class.name()/>
</head>

<body>
  <#assign pretext>Package : <@m.packageName package=class.containingPackage()/></#assign>
  <@m.heading pretext=pretext doc=class/>
  
  <div id="prioritydoc_jump">
  
    <a class="icon" href="#prioritydoc_topAnchor" title="Jump to Top (Home)" onclick="return scrollToElement('#prioritydoc_topAnchor');"/><@m.icon doc=class/></a>
    
    <#if (combinedClass.staticFields?size > 0)>
        <a href="#prioritydoc_staticFieldsAnchor"  title="Jump to Static Fields" onclick="return scrollToElement('#prioritydoc_staticFieldsAnchor');"><img src="${base}/images/static_field.png"/></a>
    </#if>
    <#if (combinedClass.staticMethods?size > 0)>
        <a href="#prioritydoc_staticMethodsAnchor" title="Jump to Static Methods" onclick="return scrollToElement('#prioritydoc_staticMethodsAnchor');"><img src="${base}/images/static_method.png"/></a>
    </#if>
    <#if (combinedClass.fields?size > 0)>
        <a href="#prioritydoc_fieldsAnchor" title="Jump to Fields" onclick="return scrollToElement('#prioritydoc_fieldsAnchor');"><img src="${base}/images/field.png"/></a>
    </#if>
    <#if (class.constructors()?size > 0)>
        <a href="#prioritydoc_constructorsAnchor" title="Jump to Constructors" onclick="return scrollToElement('#prioritydoc_constructorsAnchor');"><img src="${base}/images/constructor.png"/></a>
    </#if>
    <#if (combinedClass.methods?size > 0)>
        <a href="#prioritydoc_methodsAnchor" title="Jump to Methods" onclick="return scrollToElement('#prioritydoc_methodsAnchor');"><img src="${base}/images/method.png"/></a>
    </#if>

    <span id="prioritydoc_initials"></span>

    <#if (class.innerClasses()?size > 0)>
        <a href="#innerClassesAnchor" title="Jump to Inner Classes" onclick="return scrollToElement('#innerClassesAnchor');"><img src="${base}/images/class.png"/></a>
    </#if>
    
  </div>



  <div id="prioritydoc_main">
  <div id="prioritydoc_content">
  
    <a id="prioritydoc_topAnchor" class="anchor"></a>
    
    <!-- Class Details -->

    <h2>
      <@m.accessText doc=class/> <@m.classEnumOrInterface doc=class/> <@m.name doc=class/>
      
      <#if (class.typeParameters()?size > 0)>
        &lt;
        <#assign comma=false>
        <#list class.typeParameters() as typeParameter>
          <#if comma> , </#if><#assign comma=true>${typeParameter}
        </#list>
        &gt;
      </#if>

      <#if class.superclassType()?? && (class.superclassType().qualifiedName() != "java.lang.Object")>
        <span class="extends"> extends <@m.type type=class.superclassType()/></span>
      </#if>
      
      <#if (class.interfaces()?size > 0)>
        <span class="extends"> <#if class.isInterface()>extends<#else>implements</#if>
        <#assign comma=false>
        <#list class.interfaces() as interface>
          <#if comma> , </#if><#assign comma=true><@m.type type=interface/>
        </#list>
        </span>
      </#if>
    </h2>
          
    <div class="detail">
      <div class="comment">
        <@m.text tags=class.inlineTags()/>
      </div>

      <@m.standardTags doc=class/>

      <#if (knownSubinterfaces?? && knownSubinterfaces?size > 0)>
        <h4>All Known Subinterfaces:</h4>
        <#assign comma=false>
        <#list knownSubinterfaces as subinterface>
          <#if comma> , </#if><#assign comma=true><@m.type type=subinterface/>
        </#list>
      </#if>

      <#if (knownSubclasses?? && knownSubclasses?size > 0)>
        <#if class.isInterface()>
          <h4>All Known Implementing Classes:</h4>
        <#else>
          <h4>All Known Subclasses:</h4>
        </#if>
        <#assign comma=false>
        <#list knownSubclasses as subclass>
          <#if comma> , </#if><#assign comma=true><@m.type type=subclass/>
        </#list>
      </#if>

    </div>
      
    <!-- Static Fields -->

    <a id="prioritydoc_staticFieldsAnchor" class="anchor"></a>
    <#if (combinedClass.staticFields?size > 0)>
      <h3 id="prioritydoc_staticFields">Static Fields</h3>
      <@m.fields fields=combinedClass.staticFields/>
    </#if>


    <!-- Static Methods -->

    <a id="prioritydoc_staticMethodsAnchor" class="anchor"></a>
    <#if (combinedClass.staticMethods?size > 0)>
      <h3 id="prioritydoc_prioritydoc_staticMethods">Static Methods</h3>
      <@m.methods methods=combinedClass.staticMethods useInitials=false/>  
    </#if>


    <!-- Fields -->

    <a id="prioritydoc_fieldsAnchor" class="anchor"></a>
    <#if (combinedClass.fields?size > 0)>
      <h3 id="prioritydoc_fields">Fields</h3>
      <@m.fields fields=combinedClass.fields/>
    </#if>


    <!-- Constructors -->
    
    <a id="prioritydoc_constructorsAnchor" class="anchor"></a>
    <#if (class.constructors()?size > 0)>
      <h3 id="prioritydoc_constructors">Constructors</h3>
      
      <#list class.constructors() as constructor>

        <div class="constructor <@m.priorityClass doc=constructor/> contracted">
          <span class="nowrap">
            <img src="${base}/images/constructor.png" class="icon"/>
            
            <@m.name doc=constructor/>
            (
            <#assign comma=false>
            <#list constructor.parameters() as parameter>
              <#if comma>,</#if><#assign comma=true> <@m.type type=parameter.type()/>
            </#list>
            )
            <@m.access doc=constructor/>
            <#if (constructor.commentText()?trim?length > 0)><span class="more">...</span></#if>
          </span>
          <div class="detail">
         
            <div class="comment">
              ${constructor.commentText()}
            </div>
          
            <@m.parameters method=constructor/>            

            <@m.standardTags doc=constructor/>
           
          </div>
 
        </div>
        
      </#list>
    </#if>


    <!-- Methods -->

    <a id="prioritydoc_methodsAnchor" class="anchor"></a>
    <#if (combinedClass.methods?size > 0)>
      <h3 id="prioritydoc_methods">Methods</h3>
      <@m.methods methods=combinedClass.methods useInitials=true/>  
    </#if>


    <!-- Inner Classes -->

    <a id="prioritydoc_innerClassesAnchor" class="anchor"></a>
    <#if (class.innerClasses()?size > 0)>
      <h3 id="prioritydoc_innerClasses">Inner Classes</h3>
      <#list class.innerClasses() as innerClass>
        <div class="innerClass <@m.priorityClass doc=innerClass/> contracted">
          <img src="${base}/images/class.png" class="icon"/>
          <span class="name"><a href="${innerClass.name()?html}.html">${innerClass.name()?html}</a></span>
        </div>
      </#list>
    </#if>
    
  </div>
  </div>
  
<script>
$( document ).ready(function() { createInitials( "${m.usedInitials?html}" ); });
</script>

<@m.pageFooter/>

</body>
</html>

