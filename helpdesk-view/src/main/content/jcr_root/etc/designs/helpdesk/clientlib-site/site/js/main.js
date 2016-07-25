window.$ = window.jQuery;

var HELPDESK = (function (HELPDESK, $) {

    // Augmenting shared objects and arrays
    HELPDESK.modules = HELPDESK.modules || {};

    HELPDESK.initMods = function() {
        // Run required modules with default selector
        if (typeof HELPDESK.modules === 'undefined') {
            console.log("No HELPDESK modules found for page.");
            return;
        }
        for (var mod in HELPDESK.modules) {
            try {
                HELPDESK.modules[mod].init();
            } catch(e) {
                console.log("Can't init mod: " + HELPDESK.modules[mod].name);
            }
        }
    };

    return HELPDESK;

})(HELPDESK || {}, jQuery);


jQuery(function() {
    HELPDESK.initMods();
});
