var HELPDESK = function (HELPDESK, $) {

    HELPDESK.utils = HELPDESK.utils || {};

    HELPDESK.utils.isAuthMode = function () {
        if (CQ.WCM) {
            return CQ.WCM.isEditMode(true) || CQ.WCM.isDesignMode(true);
        }
        return false;
    };

    HELPDESK.utils.get_cookie = function (name) {
        cookie_name = name + "=";
        cookie_length = document.cookie.length;
        cookie_begin = 0;
        while (cookie_begin < cookie_length) {
            value_begin = cookie_begin + cookie_name.length;
            if (document.cookie.substring(cookie_begin, value_begin) == cookie_name) {
                var value_end = document.cookie.indexOf(";", value_begin);
                if (value_end == -1) {
                    value_end = cookie_length;
                }
                return unescape(document.cookie.substring(value_begin, value_end));
            }
            cookie_begin = document.cookie.indexOf(" ", cookie_begin) + 1;
            if (cookie_begin == 0) {
                break;
            }
        }
        return null;
    }

    HELPDESK.utils.set_cookie = function (name, value, expires, path) {
        if (!expires) {
            expires = new Date();
        }
        if (!path) {
            path = "/";
        }
        document.cookie = name + "=" + escape(value) + "; expires=" + expires.toGMTString() + "; path=" + path;
    }

    return HELPDESK;

}(HELPDESK || {}, jQuery);