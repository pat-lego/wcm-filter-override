package com.adobe.aem.support.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.WCMMode;

import org.apache.sling.engine.EngineConstants;

import org.apache.sling.settings.SlingSettingsService;

@Component(service = Filter.class, property = {
        EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_COMPONENT,
        EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.SLING_FILTER_SCOPE
})
@ServiceDescription("Patch for WCMFilter")
@ServiceRanking(400)
@ServiceVendor("AEM Support")
@Designate(ocd = SupportWCMFilter.Config.class)
public class SupportWCMFilter implements Filter {

    private Config config;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private SlingSettingsService slingSettingsService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.debug("enabled = {}", config.enabled());
        if (config.enabled()) {
            logger.debug("Filter is enabled");
            boolean isAuthor = this.slingSettingsService.getRunModes().contains("author");
            WCMMode mode = WCMMode.fromRequest(request);

            if (mode != WCMMode.EDIT && isAuthor) {
                logger.debug("Resetting WCMMode to EDIT in the request");
                request.setAttribute("com.day.cq.wcm.api.WCMMode", WCMMode.EDIT);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // ignore
    }

    @Activate
    public void activate(Config config) {
        this.config = config;
    }

    public String fromRequest(ServletRequest req) {
        String mode = (String) req.getAttribute("WCMMode");
        return mode == null ? "DISABLED" : mode;
    }

    @ObjectClassDefinition(name = "Custom Service Configuration", description = "Service Configuration")
    public @interface Config {

        @AttributeDefinition(name = "Enabled", description = "Enable the filter")
        boolean enabled() default false;
    }

}
