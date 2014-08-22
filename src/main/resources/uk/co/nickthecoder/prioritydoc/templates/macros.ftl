<#assign previousInitial="">
<#assign usedInitials="">
<#assign pageDoc="">
  
<#macro page bas opt doc>
  <#assign base=bas>
  <#assign options=opt>
  <#assign base=bas>
</#macro>

<#macro head title>
  <title>${title?html} ${options.title}</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" type="text/css" href="${base}/style.css" title="Style">
  <script src="${base}/jquery-2.1.1.min.js"></script>
  <script src="${base}/jquery-ui.min.js"></script>
  <script src="${base}/prioritydoc.js"></script>
	<link rel="shortcut icon" href="${base}/images/favicon.png">
</#macro>

<#macro heading pretext="" doc="index">
  <div id="heading">
    <div class="heading1">
      <a class="index" title="Index" href="${base}/index.html"><img alt="index" src="${base}/images/favicon.png"/></a> &nbsp; ${pretext} 
    </div>
    
    <h1 onclick="scrollToElement('#topAnchor')">
      <@m.icon doc=doc/>
      <@m.name doc=doc/>
    </h1>
    <div class="buttons">
      <div class="above"><span id="hiddenCount">0</span> hidden item(s)</div>
      <img id="contractButton" class="icon" src="${base}/images/contract.png" alt="-" title="Contract (shortcut -)" onClick="contractAll();return false;">
      <img id="expandButton" class="icon" src="${base}/images/expand.png" alt="+" title="Expand (shortcut +)" onClick="expandAll();return false;">
    
      <a id="priority1Button" class="button priority1" title="Priority 1. Hide 2, 3, 4 and 5 (shortcut 1)">1</a>
      <a id="priority2Button" class="button priority2" title="Priority 2. Hide 3, 4 and 5 (shortcut 2)">2</a>
      <a id="priority3Button" class="button priority3" title="Priority 3. Hide 4 and 5 (shortcut 3)">3</a>
      <a id="priority4Button" class="button priority4" title="Priority 4. Hide 5 (shortcut 4)" >4</a>
      <a id="priority5Button" class="button priority5 vital" title="Priority 5. Show Everything (shortcut 5)">All</a>
    </div>
  </div>
</#macro>

<#macro name doc>
<#compress>
  <#if doc=="index">
    <span class="name">Index</span>
  <#else>
    <span
      <#if (doc.tags("deprecated")?size > 0)>
        class="name deprecated" title="deprecated"
      <#else>
        class="name"
      </#if>
      >
      ${doc.name()?html}
    </span>
  </#if>
</#compress>
</#macro>

  
<#macro icon doc>
<#compress>
  <#if doc=="index">
    <img alt="index" class="icon" src="${base}/images/index.png"/>
  <#else>
    <#if doc.isInterface()>
      <img alt="interface" class="icon" src="${base}/images/interface.png"/>
    <#else>
      <#if doc.isException()>
        <img alt="exception" class="icon" src="${base}/images/exception.png"/>
      <#else>
        <#if doc.isEnum() >
          <img alt="class" class="icon" src="${base}/images/enum.png"/>
        <#else>
          <img alt="class" class="icon" src="${base}/images/class.png"/>
        </#if>
      </#if>
    </#if>
  </#if>
</#compress>
</#macro>

<#macro rtype rtype><@type type=rtype/></#macro>

<#macro type type>
<#compress>
  <#if type.asParameterizedType()??>
    <@classType class=type.asClassDoc()/>&lt;<#assign comma=false><#list type.asParameterizedType().typeArguments() as argument><#if comma>,</#if><#assign comma=true><@rtype rtype=argument/></#list>&gt;
  <#else>

    <#if type.asTypeVariable()??>
      ${type.toString()}<#t> <!--type-->
    <#else>
      
      <#if type.asWildcardType()??>
        ${type.toString()}<#t> <!--wildcard-->
      <#else>

        <#if type.asClassDoc()??>

          <@classType class=type.asClassDoc()/>

        <#else>
          ${type.toString()}<#t>  <!--other-->
        </#if>
        
      </#if>

    </#if>

  </#if>
  ${(type.dimension()!"")?replace("[", " [ ")}<#t>
</#compress>
</#macro>

<#macro classType class>
<#compress>
  <#assign bas="">
  <#if class.isIncluded()>
    <#assign bas=base>
  <#else>
    <#assign bas=options.getJavadocsForPackage(class.containingPackage())!"">
  </#if>
  
  <#if bas=="">
    <span class="external" title="${class.qualifiedName()}">${class.name()}</span>
  <#else>
    <a <#if !class.isIncluded()>class="external" </#if>title="${class.qualifiedName()}" href="${bas}/${class.containingPackage()?replace(".","/")}/${class.name()}.html">${class.name()}</a>
  </#if>
</#compress>
</#macro>

<#macro packageName package>
<#compress>
  <span class="name"><a href="<@packageURL package=package/>">${package.name()?html}</a></span>
</#compress>
</#macro>

<#macro packageURL package>${base}/${package.name()?replace(".","/")}/index.html</#macro>

<#macro classURL class>${base}/${class.containingPackage()?replace(".","/")}/${class.name()}.html</#macro>

<#macro seeLink seeTag>
<#compress>
  <#if seeTag.referencedPackage()??>

  <#else>

    <#if seeTag.referencedMember()??>

      <#if ((seeTag.referencedClass()??) && (seeTag.referencedClass() == pageDoc))>
        <a href="<@classURL class=seeTag.referencedClass()/>#${seeTag.referencedMemberName()}">
          ${seeTag.referencedClass().name()}.${seeTag.referencedMemberName()?html}
        </a>
      <#else>
        <a href="#${seeTag.referencedMemberName()?html}">${seeTag.referencedMemberName()?html}</a>
      </#if>
            
    <#else>

      <#if seeTag.referencedClass()??>

        <a href="<@classURL class=seeTag.referencedClass()/>">
          ${seeTag.referencedClass().name()}
        </a>

      <#else>
        ${seeTag.label()?html}  
      </#if>

    </#if>

  </#if>
            
</#compress>
</#macro>

<#macro text tags>
<#compress>
  <#list tags as tag>
    <#if tag.kind() == "@see">
      <@seeLink seeTag=tag/>
    <#else>
      ${tag.text()}
    </#if>
  </#list>
</#compress>
</#macro>

<#macro summary docs title="" type="" useInitials=false>

  <#if type!="">
    <a id="${type}Anchor" class="anchor"></a>
  </#if>
  <#if (docs?size > 0)>
    <#if title != "">
      <h2>${title}</h2>
    </#if>
    <#list docs as doc>

      <#assign priority="1">
      <#list doc.tags("priority") as tag>
          <#assign priority="${tag.text()}">
      </#list>
      
      <#if useInitials>
        <@initialAnchor doc=doc/>
      </#if>

      <div class="${type} fixed priority${priority?html} nowrap">
        <@icon doc=doc/>
        <span class="name"><a href="<@classURL class=doc/>">${doc.name()?html}</a></span>
        <#if (doc.firstSentenceTags()?size > 0)>
          <span class="firstSentence">
            <@m.text tags=doc.firstSentenceTags()/>
            <#if (doc.inlineTags()?size > doc.firstSentenceTags()?size) > ...</#if>
          </span>
        </#if>
      </div>

    </#list>
  </#if>
</#macro>

<#macro parameters method>
  <#if (method.parameters()?size > 0)>
    <div class="parameters">
      <h4>Parameters :</h4>
      <div class="inner">
        <ul class="plain">
        <#list method.parameters() as parameter>
          <li>
          <@type type=parameter.type()/> ${parameter.name()?html}
          <#list method.paramTags() as paramTag>
            <#if paramTag.parameterName() == parameter.name()>
              : ${paramTag.parameterComment()}
            </#if>
          </#list>
          </li>
        </#list>
        </ul>
      </div>
    </div>
  </#if>
</#macro>

<#macro see doc>
  <#if (doc.seeTags()?size > 0)>  
    <div class="see">
      <h4>See Also:</h4>
      <div class="inner">
        <ul class="plain">
          <#list doc.seeTags() as seeTag>
            <li><@seeLink seeTag=seeTag/></li>
          </#list>
        </ul>
      </div>
    </div>
  </#if>
</#macro>

<#macro standardTags doc>

  <#if (doc.tags("return")?size > 0)>
    <div class="return">
      <h4>Returns :</h4>
      <div class="inner">
        <#list doc.tags("return") as return>
          ${return.text()}
        </#list>                
      </div>
    </div>
  </#if>

  <@see doc=doc/>
</#macro>

<#macro fields fields>

  <#list fields as field>
    <#assign priority="1">
    <#list field.tags("priority") as tag>
        <#assign priority="${tag.text()}">
    </#list>
    <div class="field priority${priority?html} contracted">
      <span class="nowrap">
        <img src="${base}/images/<#if field.isStatic()>static_</#if>field.png" class="icon"/>
        
        <@name doc=field/>
        ( <@type type=field.type()/> )
        <#if (field.commentText()?trim?length > 0)><span class="more">...</span></#if>
      </span>
          
      <div class="detail">
        <div class="comment">
          ${field.commentText()}
        </div>

        <@standardTags doc=field/>
      </div>
       
    </div>
  </#list>
    
</#macro>

<#macro initialAnchor doc>
  <#assign currentInitial = doc.name()?substring(0,1)?upper_case/>

  <#if currentInitial != previousInitial>
      <#assign previousInitial = currentInitial>
      <#assign usedInitials = usedInitials + currentInitial/>
      <a id="_initial_${currentInitial?html}" class="anchor"></a>
  </#if>
</#macro>

<#macro methods methods useInitials>

  <#list methods as method>
    <#assign priority="1">
    <#list method.tags("priority") as tag>
        <#assign priority="${tag.text()}">
    </#list>

    <#if useInitials>
      <@initialAnchor doc=method/>
    </#if>

    <div class="method priority${priority} contracted">
      <span class="nowrap">
        <img src="${base}/images/<#if method.isStatic()>static_</#if>method.png" class="icon"/>
      
        <@name doc=method/>
        (
        <#assign comma=false>
        <#list method.parameters() as parameter>
          <#if comma>,</#if><#assign comma=true> <@type type=parameter.type()/> ${parameter.name()}
        </#list>
        )
        -> <@type type=method.returnType()/>
        <#if (method.commentText()?trim?length > 0)><span class="more">...</span></#if>
      </span>
      
      <div class="detail">

        <div class="comment">
          <@text tags=method.inlineTags() />
        </div>
      
        <@parameters method=method/>
        
        <@standardTags doc=method/>

        <#if (method.throwsTags()?size > 0)>
          <div class="throws">
            <h4>Throws :</h4>
            <div class="inner">
              <ul class="plain">
              <#list method.throwsTags() as throwsTag>
                <li>${throwsTag.exceptionType().name()} : ${throwsTag.exceptionComment()}</li>
              </#list>
              </ul>                
            </div>
          </div>
        </#if>
        
      </div>

    </div>
    
  </#list>  

</#macro>


<#macro indexJump>
 <div id="jump">
  
    <a class="icon" href="#topAnchor" title="Jump to Top (Home)" onclick="return scrollToElement('#topAnchor');"/><img alt="class" class="icon" src="${base}/images/package.png"/></a>
    
    <#if (sortedEnums?size > 0)>
      <a href="#enumAnchor" title="Jump to Enums" onclick="showByCategory(); return scrollToElement('#enumAnchor');"><img src="${base}/images/enum.png"/></a>
    </#if>
    <#if (sortedInterfaces?size > 0)>
      <a href="#interfaceAnchor" title="Jump to Interfaces" onclick="showByCategory(); return scrollToElement('#interfaceAnchor');"><img src="${base}/images/interface.png"/></a>
    </#if>    
    <#if (sortedClasses?size > 0)>
      <a href="#classAnchor" title="Jump to Classes" onclick="showByCategory(); return scrollToElement('#classAnchor');"><img src="${base}/images/class.png"/></a>
    </#if>    
    <#if (sortedExceptions?size > 0)>
      <a href="#exceptionAnchor" title="Jump to Exceptions" onclick="showByCategory(); return scrollToElement('#exceptionAnchor');"><img src="${base}/images/exception.png"/></a>
    </#if>    

    <span id="initials"></span>
    
  </div>
</#macro>

<#macro pageFooter>

  <script>
    <#if options.useCookies>
      enableCookies();
  </#if>
  parseParameters();
  </script>

  <div id="footing">
    <a href="${base}/index.html">${options.title} Index</a>
    <div class="createdBy">
      Created by <a href="http://nickthecoder.co.uk/software/view/PriorityDoc">PriorityDoc</a> Java Doclet
    </div>
  </div>

</#macro>


