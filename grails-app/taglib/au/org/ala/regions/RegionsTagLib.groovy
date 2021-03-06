package au.org.ala.regions

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import au.org.ala.cas.util.AuthenticationCookieUtils

class RegionsTagLib {

    static namespace = 'rg'

    /**
     * Returns the username of the user if logged in else blank
     *
     * @attrs noCookie if true the helper cookie will not be used, ie the user must actually be logged in
     */
    def loggedInUsername = { attrs ->
        def useCookie = attrs.noCookie != "true"
        if (request.getUserPrincipal()) {
        	out << request.getUserPrincipal().name
        }
        else if (useCookie && AuthenticationCookieUtils.cookieExists(request, AuthenticationCookieUtils.ALA_AUTH_COOKIE)) {
            out << AuthenticationCookieUtils.getUserName(request)
        }
        else {
            out << ""
        }
    }

    /**
     * Generate the login link for the banner.
     *
     * Will be to log in or out based on current auth status.
     *
     * @attr fixedAppUrl if supplied will be used for logout instead of the current page
     *
     * @deprecated use HeaderFooterTagLib
     */
    def loginoutLink2011 = { attrs ->
        def requestUri = ConfigurationHolder.config.security.cas.serverName + request.forwardURI
        if (AuthenticationCookieUtils.cookieExists(request, AuthenticationCookieUtils.ALA_AUTH_COOKIE)) {
            // currently logged in
            out << link(controller: 'regions', action: 'logout',
                    params: [casUrl: ConfigurationHolder.config.security.cas.logoutUrl,
                            appUrl: attrs.fixedAppUrl ?: requestUri]) {'Logout'}
        } else {
            // currently logged out
            out << "<a href='https://auth.ala.org.au/cas/login?service=${requestUri}'><span>Log in</span></a>"
        }
    }

    /**
     * Write the appropriate breadcrumb trail.
     *
     * Checks the config for skin to choose the correct hierarchy.
     *
     * @attr home the root of the page - defaults to collections
     * @attr atBase true if the page is the base page of the root (no link is added)
     */
    def breadcrumbTrail = {attrs ->
        if (ConfigurationHolder.config.ala.skin == 'ala') {
            out << "<li><a href='${ConfigurationHolder.config.ala.baseURL}'>Home</a></li>" +
                "<li> <a href='${ConfigurationHolder.config.ala.baseURL}/explore/'>Explore</a></li>"
        }
        else {
            out << "<li><a href='${ConfigurationHolder.config.ala.baseURL}'>Home</a></li>" +
                "<li> <a href='${ConfigurationHolder.config.ala.baseURL}/species-by-location/'>Locations</a></li>"
        }
    }

}
