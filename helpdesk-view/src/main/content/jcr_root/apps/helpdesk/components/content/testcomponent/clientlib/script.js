var HELPDESK = (function (HELPDESK, $) {

    HELPDESK.modules.TestComponent = {};

    HELPDESK.modules.TestComponent.init = function () {
        console.log('Component: "TestComponent"');
        testFunction();
    }

    return HELPDESK;

})(HELPDESK || {}, jQuery);

