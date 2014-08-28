<!DOCTYPE html>
<#import "macros.ftl" as m>
<html lang="en">
<head>
    <title>${diagram.title?html}</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${base}/boxes.css"/>
    <script type="text/javascript" src="./jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="./jquery-ui.min.js"></script>
    <script type="text/javascript" src="${base}/boxes.js"></script>
</head>

<body style="width:${diagram.width?c}px;">

    <div id="header">
        <div class="buttons">
            <span id="prioritydoc_editButton" class="button">Edit</span>
            <span id="prioritydoc_infoButton" class="button">Info</span>
        </div>
        <h1>${diagram.title?html}</h1>
    </div>
    <div id="prioritydoc_diagram">

        <canvas id="prioritydoc_canvas" width="${diagram.width?c}" height="${diagram.height?c}"></canvas>

        <#list diagram.classBoxes as box>
            <div id="${box.name}" class="box" style="left:${box.x}px;top:${box.y}px;">
                <div class="title"><a href="<@m.classURL box.classDoc/>">${box.classDoc.name()}</a></div>
                <div class="content">
                    <#if (box.fields?size > 0)>
                        <ul class="fields">
                        <#list box.fields as field>
                            <li class="public"><a href="<@m.classURL box.classDoc/>#${field.name()}">${field.name()}</a></li>
                        </#list>
                        </ul>
                    </#if>
                    <#if (box.methods?size > 0)>
                        <ul class="methods">
                        <#list box.methods as method>
                            <li class="public"><a href="<@m.classURL box.classDoc/>#${method.name()}${removeGenerics(method.signature())}">${method.name()}</a></li>
                        </#list>
                        </ul>
                    </#if>
                </div>
            </div>
        </#list>

    </div>

    <div id="prioritydoc_infoPopup" class="popup">
        <div style="float:right">
            <span class="button" onclick="$('#prioritydoc_infoPopup').hide();">close</span>
        </div>
        <textarea id="prioritydoc_infoTextArea" wrap="off"></textarea>
    </div>

<script type="text/javascript">
function drawConnections()
{
    clear();
    ctx.font = "7pt Verdana";
    <#list diagram.connections as connection>
        connect( "${connection.connectionType}", "${connection.fromName}", "${connection.toName}", "${connection.fromLabel?html}", "${connection.toLabel?html}" );
    </#list>
}
</script>
</body>

</html>