package alien4cloud.csar.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import alien4cloud.dao.IGenericSearchDAO;
import alien4cloud.exception.NotFoundException;
import alien4cloud.tosca.container.model.CSARDependency;
import alien4cloud.tosca.model.Csar;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Manages cloud services archives and their dependencies.
 */
@Component
public class CsarService implements ICsarDependencyLoader {
    @Resource(name = "alien-es-dao")
    private IGenericSearchDAO csarDAO;

    /**
     * Get a cloud service if exists in Dao.
     * 
     * @param name The name of the archive.
     * @param version The version of the archive.
     * @return The {@link Csar Cloud Service Archive} if found in the repository or null.
     */
    public Csar getIfExists(String name, String version) {
        Csar csar = new Csar();
        csar.setName(name);
        csar.setVersion(version);
        csar = csarDAO.findById(Csar.class, csar.getId());
        return csar;
    }

    @Override
    public Set<CSARDependency> getDependencies(String name, String version) {
        Csar csar = getIfExists(name, version);
        if (csar == null) {
            throw new NotFoundException("Csar with name [" + name + "] and version [" + version + "] cannot be found");
        }
        if (csar.getDependencies() == null || csar.getDependencies().isEmpty()) {
            return Sets.newHashSet();
        }
        return Sets.newHashSet(csar.getDependencies());
    }

    /**
     * Save a Cloud Service Archive in ElasticSearch.
     * 
     * @param csar The csar to save.
     */
    public void save(Csar csar) {
        this.csarDAO.save(csar);
    }

    public Map<String, Csar> findByIds(String fetchContext, String... ids) {
        Map<String, Csar> csarMap = Maps.newHashMap();
        List<Csar> csars = csarDAO.findByIdsWithContext(Csar.class, fetchContext, ids);
        for (Csar csar : csars) {
            csarMap.put(csar.getId(), csar);
        }
        return csarMap;
    }

    public Csar getMandatoryCsar(String csarId) {
        Csar csar = csarDAO.findById(Csar.class, csarId);
        if (csar == null) {
            throw new NotFoundException("Csar with id [" + csarId + "] do not exist");
        } else {
            return csar;
        }
    }
}
