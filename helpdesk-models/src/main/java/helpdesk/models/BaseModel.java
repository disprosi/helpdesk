package helpdesk.models;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class)
public class BaseModel {

    private static final Logger LOG = LoggerFactory.getLogger(BaseModel.class);

    @Self
    protected Resource selfResource; // этой мой топнав (или любая другая нода, которая вызывает) класс,
    //наследующий BaseModel
    // над ним джср контент и над ним страница
    //есть гетпейджфромресурс, которая появляется до ноды со страницей
    //когда мы получили страницу в методе инит чё то ещё делает
    //все модели экстендятся от бейсмодель и в каждой странице

    @Inject
    protected ResourceResolver resourceResolver;

    @Inject
    private QueryBuilder queryBuilder;

    protected Page rootContentPage;

    private Page currentPage;

    @PostConstruct
    protected void init() {
        currentPage = getPageFromResource(selfResource);
        rootContentPage = currentPage.getAbsoluteParent(1);
    }

    public String getHomePath() {
        return rootContentPage.getPath();
    }

    public Page getOwnPage() {
        return currentPage;
    }

    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    public Session getSession() {
        return resourceResolver.adaptTo(Session.class);
    }

    public ValueMap getPageProperties() {
        return currentPage.getProperties();
    }

    public ValueMap getComponentProperties() {
        return selfResource.getValueMap();
    }

    public String getComponentPropertyAsString(String propertyName) {
        return selfResource.getValueMap().get(propertyName, String.class);
    }

    public Page getHomePage() {
        return rootContentPage;
    }

    public Page getCatalogRootPage() {
        Resource catalogRootPageResource = resourceResolver.getResource(Constants.CATALOG_ROOT_PAGE_PATH);
        return catalogRootPageResource == null ? null : catalogRootPageResource.adaptTo(Page.class);
    }

    public String getPagePath() {
        return getOwnPage().getPath();
    }

    public String getPageTitle() {
        return getOwnPage().getTitle();
    }

    public Page getPageFromResource(Resource resource) {
     //   return resource.getResourceType().equals(Constants.RESOURCE_TYPE_CQ_PAGE) ? resource.adaptTo(Page.class) : getPageFromResource(resource.getParent());
        return resource.getResourceType().equals(Constants.RESOURCE_TYPE_CQ_PAGE) ? resource.adaptTo(Page.class) : getPageFromResource(resource.getParent());
    }

    public String getPropertyFromOwnPageWithResourceType(String resourceType, String propertyName) {
        List<String> propertiesList = getPropertyFromQueryBuilder(getPagePath(), resourceType, propertyName, true);
        return propertiesList.size() > 0 ? propertiesList.get(0) : null;
    }

    public String getPropertyFromResourceType(String path, String resourceType, String propertyName) {
        List<String> propertiesList = getPropertyFromQueryBuilder(path, resourceType, propertyName, true);
        return propertiesList.size() > 0 ? propertiesList.get(0) : null;
    }

    private List<String> getPropertyFromQueryBuilder(String path, String resourceType, String propertyName, boolean singleProperty) {
        List<String> propertiesList = new ArrayList();
        Map<String, String> map = new HashMap<String, String>();
        map.put("path", path);
        map.put("property", "sling:resourceType");
        map.put("property.value", resourceType);
        map.put("p.limit", "-1");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
        SearchResult result = query.getResult();

        for (Hit hit : result.getHits()) {
            Node searchNode = null;
            try {
                searchNode = hit.getNode();
                if (searchNode.hasProperty(propertyName)) {
                    propertiesList.add(searchNode.getProperty(propertyName).getString());
                }
            } catch (RepositoryException e) {
                LOG.error(e.getMessage());
            }
        }
        return propertiesList;
    }

    public List<String> getAllPropertiesFromOwnPageWithResourceType(String resourceType, String propertyName) {
        return getPropertyFromQueryBuilder(getPagePath(), resourceType, propertyName, false);
    }

    public Resource getSelfResource() {
        return selfResource;
    }

    public void setSelfResource(Resource selfResource) {
        this.selfResource = selfResource;
    }

    public void setRootContentPage(Page rootContentPage) {
        this.rootContentPage = rootContentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

    public Page getCurrentPage() {
        return currentPage;
    }
}
