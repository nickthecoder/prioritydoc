$( document ).ready(function() {
    canvas=document.getElementById("prioritydoc_canvas");
    ctx=canvas.getContext("2d");
    font = "10pt Verdana";
    strokeStyle="#444";
    lineWidth=2;

    $("#prioritydoc_editButton").on( "click", edit );
    $("#prioritydoc_infoButton").on( "click", info );

    if (window.location.search == "?noheader") {
        $("body").addClass("noheader");
    }

    drawConnections();

});

function info()
{
    var width = 0;
    var height = 0;

    $(".box").each( function( n ) {
        var r = $(this).position().left + $(this).outerWidth();
        if (r > width) {
            width = r;
        }
        var b = $(this).position().top + $(this).outerHeight();
        if (b > height) {
            height = b;
        }
    });

    var info = '\n<diagram width="'+ Math.floor(width) + '" height="' + Math.floor(height) + '">\n\n';
    $(".box").each( function( n ) {
        var name = $(this).attr("id");
        var x = $(this).position().left;
        var y = $(this).position().top;
        info = info + '    <class name="'+ name + '" x="' + Math.floor(x) + '" y="' + Math.floor(y) + '"/>\n';
    });
    info = info + "\n</diagram>\n";

    $("#prioritydoc_infoPopup").show();
    $("#prioritydoc_infoTextArea").val( info );
}

function edit()
{
    $("#prioritydoc_editButton").hide();
    $("#prioritydoc_infoButton").show();
    $(".box").draggable({
        stop: function( event, ui ) {
            dragged($(this));
        }
    });
}

var CP = window.CanvasRenderingContext2D && CanvasRenderingContext2D.prototype;
if (CP.lineTo) {
    CP.dashedLine = function(x, y, x2, y2, da) {
        if (!da) da = [10,5];
        this.save();
        var dx = (x2-x), dy = (y2-y);
        var len = Math.sqrt(dx*dx + dy*dy);
        var rot = Math.atan2(dy, dx);
        this.translate(x, y);
        this.moveTo(0, 0);
        this.rotate(rot);
        var dc = da.length;
        var di = 0, draw = true;
        x = 0;
        while (len > x) {
            x += da[di++ % dc];
            if (x > len) x = len;
            draw ? this.lineTo(x, 0): this.moveTo(x, 0);
            draw = !draw;
        }
        this.restore();
    }
}

function clear()
{
    canvas.width = canvas.width;
}

noEnd = function() {
}
noEnd.margin = 0;

dependencyEnd = function() {
    ctx.beginPath();
    ctx.moveTo( 10, -5 );
    ctx.lineTo( 0,0 );
    ctx.lineTo( 10, 5 );
    ctx.stroke();
};
dependencyEnd.margin = 8;

realiseEnd = function() {
    ctx.beginPath();
    ctx.moveTo( 16, -8 );
    ctx.lineTo( 0,0 );
    ctx.lineTo( 16, 8 );
    ctx.lineTo( 16, -8 );
    ctx.fillStyle="#fff";
    ctx.fill();
    ctx.stroke();
};
realiseEnd.margin = 1;
//realiseEnd.dashed = [6,6];


generaliseEnd = function() {
    ctx.beginPath();
    ctx.moveTo( 16, -8 );
    ctx.lineTo( 0,0 );
    ctx.lineTo( 16, 8 );
    ctx.lineTo( 16, -8 );
    ctx.fillStyle="#fff";
    ctx.fill();
    ctx.stroke();
};
generaliseEnd.margin = 1;

aggregationEnd = function() {
    ctx.beginPath();
    ctx.lineTo( 0,0 );
    ctx.lineTo( 12, 8 );
    ctx.lineTo( 24, 0 );
    ctx.lineTo( 12, -8 );
    ctx.lineTo( 0, 0 );
    ctx.fillStyle="#fff";
    ctx.fill();
    ctx.stroke();
};
aggregationEnd.margin = 1;

compositionEnd = function() {
    ctx.beginPath();
    ctx.lineTo( 0,0 );
    ctx.lineTo( 12, 8 );
    ctx.lineTo( 24, 0 );
    ctx.lineTo( 12, -8 );
    ctx.lineTo( 0, 0 );
    ctx.fillStyle="#000";
    ctx.fill();
};
compositionEnd.margin = 1;


function connect( connectionType, aId, bId, aLabel, bLabel )
{
    var aArrowFunc = noEnd;
    var bArrowFunc = noEnd;

    if (connectionType == "generalisation") {
        aArrowFunc = generaliseEnd;
    } else if (connectionType == "dependency" ) {
        aArrowFunc = dependencyEnd;
    } else if (connectionType == "realisation") {
        aArrowFunc = realiseEnd;
    } else if (connectionType == "aggregation") {
        bArrowFunc = aggregationEnd;
    } else if (connectionType == "composition") {
        bArrowFunc = compositionEnd;
    }

    var lineStyle = (aArrowFunc.dashed || bArrowFunc.dashed) ? "dashed" : "line";

    line( aId, bId, bArrowFunc.margin, aArrowFunc, lineStyle, bLabel );
    line( bId, aId, aArrowFunc.margin, bArrowFunc, "none", aLabel );
}

function line( aId, bId, margin, arrowFunc, lineStyle, label )
{
    var aPoint = findEdge( aId, bId, margin );
    var bPoint = findEdge( bId, aId, arrowFunc.margin );

    var dx = aPoint.x - bPoint.x;
    var dy = aPoint.y - bPoint.y;
    var mag = Math.sqrt( dx * dx + dy * dy );
    var angle = Math.atan2( dy, dx );


    ctx.save();
    ctx.translate( bPoint.x, bPoint.y );
    ctx.beginPath();
    ctx.rotate( angle );
    if (lineStyle == "dashed") {
        ctx.dashedLine( 0,0, mag, 0, [6,6] );
    } if (lineStyle == "line") {
        ctx.moveTo( 0,0 );
        ctx.lineTo( mag, 0 );
    } else {
        ctx.moveTo( mag, 0 );
    }
    ctx.stroke();

    ctx.save();
    arrowFunc();
    ctx.restore();

    var degrees = (360 + angle * 180 / Math.PI) % 360;
    if ( (degrees > 90) && (degrees < 270)) {
        ctx.rotate( Math.PI );
        ctx.fillText( label, -ctx.measureText(label).width - 20, 15 );
    } else {
        ctx.fillText( label, 10, -4 );
    }
    ctx.restore();
}

function findEdge( aId, bId, margin )
{
    var a = $( document.getElementById(aId));
    var b = $( document.getElementById(bId));

    if (!margin) {
        margin = 0;
    }

    var rectA = { left: a.position().left, right: a.position().left + a.width(), top: a.position().top, bottom: a.position().top + a.height() };
    var rectB = { left: b.position().left, right: b.position().left + b.width(), top: b.position().top, bottom: b.position().top + b.height() };

    var ax = (rectA.left + rectA.right) / 2;
    var bx = (rectB.left + rectB.right) / 2;
    var ay = (rectA.top + rectA.bottom) / 2;
    var by = (rectB.top + rectB.bottom) / 2;

    var rise = by - ay;
    var tread = bx - ax
    var m = rise / tread;
    var c = by - (m * bx);

    var x = ax < bx ? rectA.right + margin : rectA.left - margin;
    var y = m * x + c;
    if ( y < rectA.top || y > rectA.bottom ) {
        y = ay > by ? rectA.top - margin : rectA.bottom + margin;
        x = (y - c) / m;
    }

    return { x: x, y : y };
}

function dragged( box )
{
    drawConnections();
}

