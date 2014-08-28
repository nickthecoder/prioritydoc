<!DOCTYPE html>
<html lang="en">
<#import "macros.ftl" as m>
<@m.page bas=base opt=options doc=""/>
<head>
  <@m.head title="Index"/>
</head>

<body class="byCategory">
  <@m.heading doc="index" pretext=options.title?html/>

  <@m.indexJump includePackages=true/>

  <div id="prioritydoc_main">
  <div id="prioritydoc_content">

    <!-- Template Details -->

    <a id="prioritydoc_topAnchor" class="anchor"></a>

    <#if mainPackage??>
      <@m.text tags=mainPackage.inlineTags()/>
    </#if>

    <!-- Packages -->

    <h3>Packages</h3>
    <a id="prioritydoc_packagesAnchor" class="anchor"></a>
    <#list packages as package>
      <div class="package priority1 fixed nowrap">
        <img src="${base}/images/package.png" class="icon"/>
        <@m.packageName package=package/>
        <#if (package.firstSentenceTags()?size > 0)>
          <span class="firstSentence">
            <@m.text tags=package.firstSentenceTags()/>
            <#if (package.inlineTags()?size > package.firstSentenceTags()?size) > ...</#if>
          </span>
        </#if>
      </div>
    </#list>

    <div class="byCategory">

      <!-- Enums -->
      <@m.summary docs=sortedEnums title="Enums" type="enum"/>

      <!-- Interfaces -->
      <@m.summary docs=sortedInterfaces title="Interfaces" type="interface"/>

      <!-- Classes -->
      <@m.summary docs=sortedClasses title="Classes" type="class"/>

      <!-- Exceptions -->
      <@m.summary docs=sortedExceptions title="Exceptions" type="exception"/>

    </div>

    <div class="byName">
      <h3>By Name</h3>
      <@m.summary docs=sortedCombined useInitials=true/>
    </div>

  </div>
  </div>


<script>
$( document ).ready(function() { createInitials( "${m.usedInitials?html}" ); });
</script>

<@m.pageFooter/>

</body>
</html>

