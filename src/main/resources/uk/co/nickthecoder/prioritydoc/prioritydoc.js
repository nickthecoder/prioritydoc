var cookiesEnabled = false;
var initialPriority = 2;

$( document ).ready(function() {
    $(window).keydown( onKeydown );

    var i;
    for ( i = 1; i < 6; i ++ ) {
        attachPriorityButton(i);
    }
    $( "#priority" + initialPriority + "Button" ).trigger( "click" );
    
    $( "div.contracted" ).on( "click", expand );
    $( "div.expanded" ).on( "click", contract );

    if (window.location.hash) {
        expandAndShow(window.location.hash.substring(1));
    }          
});

function scrollToElement(selector)
{
    $('html,body').animate({ scrollTop: $(selector).offset().top} );
    return false;
}

function onKeydown(e)
{
    var code = e.which || e.keyCode;
    var letter = String.fromCharCode(code);

    if (e.shiftKey) {
        // shift+I for the main index
        if (letter == "I") {
            document.location = $("#indexLink").attr('href');
            return false;
        }
        // shift+P for the package index
        if (letter == "P") {
            document.location = $("#packageIndexLink").attr('href');
            return false;
        }
    }


    if (e.ctrlKey) {
        return true;
    }

    if ( (code > 64) && (code < 91) ) {
      scrollToElement('#' + letter );
      return false;
    }
    

    switch ( code )
    {
        case 61:
            expandAll();
            return false;
        case 173:
            contractAll();
            return false;
        default:
            break;
    }
    
    switch ( letter )
    {
        case '1':
            $( "#priority1Button" ).trigger( "click" );
            return false;
        case '2':
            $( "#priority2Button" ).trigger( "click" );
            return false;
        case '3':
            $( "#priority3Button" ).trigger( "click" );
            return false;
        case '4':
            $( "#priority4Button" ).trigger( "click" );
            return false;
        case '5':
            $( "#priority5Button" ).trigger( "click" );
            return false;
        default:
            break;
    }
}

function expandAndShow( id )
{
    var ele = $(document.getElementById(id)); // jquery doesn't like ids such as "myMethod()".
    
    $(ele).closest(".contracted").off( "click" ).on( "click", contract ).removeClass( "contracted" ).addClass( "expanded" );
    var i;
    for ( i = currentPriority + 1; i < 6; i ++ ) {
        if (ele.closest(".priority" + i).length > 0) {
            setPriority( i );
        }
    } 
    scrollToElement( ele );
}

function expand( e )
{
    // When I click a link, it still calls this method, just before opening the new page.
    if ($(e.target).get(0).tagName == 'A') {
        return;
    }
    var ele = $(e.target).closest("div.contracted");

    ele.switchClass( "contracted", "expanded" );
    ele.off( "click" );
    ele.on( "click", contract );
}

function contract( e )
{
    // When I click a link, it still calls this method, just before opening the new page.
    if ($(e.target).get(0).tagName == 'A') {
        return;
    }
    var ele = $(e.target).closest("div.expanded");

    ele.switchClass( "expanded", "contracted" );
    ele.off( "click" );
    ele.on( "click", expand );
}

function expandAll()
{
    $('.contracted').off( "click" ).on( "click", contract ).removeClass( "contracted" ).addClass( "expanded" );
}
function contractAll()
{
    $('.expanded').off( "click" ).on( "click", expand ).removeClass( "expanded" ).addClass( "contracted" );
}

var currentPriority = 0;

function setPriority( priority )
{
    $( "#priority" + priority + "Button" ).trigger("click");
}

function attachPriorityButton( i )
{
    $( "#priority" + i + "Button" ).on( "click", function() {

        currentPriority = i;

        $( "#heading .buttons a" ).removeClass("selected");
        var j;
        for (j = 1; j < 6; j ++ ) {
            if ( j > i ) {
                $('.priority' + j).addClass("hide");
            } else {
                $('.priority' + j).removeClass("hide");
            }
        }
        $( '#priority' + i + 'Button' ).addClass("selected");
        
        $( '#hiddenCount' ).html( $('.hide').length );
        
        if ( cookiesEnabled ) {
            document.cookie="prioritydoc_priority=" + i + "; path=/";
        }
    });
}

function showByName()
{
    $( "body" ).removeClass( "byCategory" ).addClass( "byName" );
}

function showByCategory()
{
    $( "body" ).removeClass( "byName" ).addClass( "byCategory" );
}


function createInitials( initials )
{
    if ( initials.length < 2) {
        return;
    }
    
    var i;
    var previous = null;
    
    for (i = 0; i < initials.length; i ++ ) {
        var initial = initials.substring( i, i + 1 );
        if (previous != null) {
            var j;
            for (j = previous.charCodeAt(0) + 1; (j < initial.charCodeAt(0)) && (j > 64) && (j < 91); j ++ ) {
                $('<span>', {
                    text: String.fromCharCode(j),
                    "class": "unused",
                }).appendTo('#initials');
            }
        }
        previous = initial;
        createInitial( initial );
    }
}

function createInitial( initial )
{
    $('<a>',{
        "class": "initial button",
        text: initial,
        title: 'Jump to : ' + initial + ' (shortcut ' + initial + ')',
        href: '#_initial_' + initial,
        click: function() { showByName(); scrollToElement('#_initial_' + initial ); return false; }
    }).appendTo('#initials');
}

function enableCookies()
{
    $( document ).ready(function() {
        cookiesEnabled = true;
    });
    parseItems( document.cookie.split(";") );
}

function parseParameters()
{
    parseItems( location.search.substr(1).split("&") );
}

function parseItems( items )
{
    for (var i = 0; i < items.length; i++) {
        try {
            var tmp = items[i].split("=");
            var name = tmp[0];
            var value = tmp.length > 1 ? tmp[1].trim() : "";
            if ( (name == "priority") || (name == "prioritydoc_priority") ) {
                var priority = parseInt(value);
                if ( (priority > 0) && (priority <=5) ) {
                    initialPriority = priority;
                }
            }
            
        } catch (err) {
        }
    }
}


