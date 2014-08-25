<!DOCTYPE html>
<html lang="en">
<#import "macros.ftl" as m>
<@m.page bas=base opt=options doc=package/>
<head>
  <@m.head title=package.name()/>
</head>

<body class="byCategory">
  <@m.heading doc=package pretext=options.title?html/>

  <@m.indexJump/>

  <div id="prioritydoc_main">
  <div id="prioritydoc_content">

    <!-- Template Details -->

    <a id="prioritydoc_topAnchor" class="anchor"></a>

    <div class="detail">
      <div class="comment">
        <@m.text tags=package.inlineTags()/>
      </div>

      <@m.standardTags doc=package/>
    </div>

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

