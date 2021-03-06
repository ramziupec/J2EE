package fr.upec.m2.projects.JavaEE.view;
import fr.upec.m2.projects.JavaEE.business.service.BureauService;
import fr.upec.m2.projects.JavaEE.model.Bureau;
import fr.upec.m2.projects.JavaEE.view.utils.Filter;
import fr.upec.m2.projects.JavaEE.view.utils.FilterList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;


@ViewScoped
@Named
public class BureauBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LogManager.getLogger(BureauBean.class);

    private List<Bureau> bureauList;
    private boolean is_adr_asc = true;
    private boolean is_label_asc = true;
    private boolean is_cp_asc = true;

    private FilterList filterList;
    private String code_postale;

    @Inject
    private BureauService bureauService;

    // Default c'tor for CDI.
    public BureauBean() {
        filterList = new FilterList();
    }

    @PostConstruct
    public void init() {
        setCodePostale("75000");
        sortByOrder("BY_LIB");
    }

    public List<Bureau> getBureauList() {
        return bureauList;
    }

    public List<Filter> getFilterList() {
        return filterList.getFilterList();
    }

    public int getListSize() {
        return bureauList.size();
    }

    // 0 = Tout les Arrondissement
    public void setCodePostale(String code_postale) {
        LOG.info("set code_postale to: {}", code_postale);
        this.code_postale = code_postale;
        filterList.addFilter("Code_postale", code_postale);

        if (code_postale.equals("75000"))
            bureauList = bureauService.getAllBureau();
        else
            bureauList = bureauService.getBurreauByCodePostale(code_postale);

        sortByOrder("BY_LIB");
        sortByOrder("BY_LIB");
    }

    public String getCodePostale() {
        return code_postale;
    }

    public void sortByOrder(String order) {
        LOG.info("sorting list by: {}", order);
        if (bureauList.isEmpty())
            return;

        switch (order) {
            case "BY_ADR":
                if (is_adr_asc) {
                    bureauList.sort(Comparator.comparing(Bureau::getAdresse_bureau));
                    filterList.addFilter("Order", "BY_ADR_ASC");
                }
                else {
                    bureauList.sort(Comparator.comparing(Bureau::getAdresse_bureau).reversed());
                    filterList.addFilter("Order", "BY_ADR_DSC");
                }
                is_adr_asc = !is_adr_asc;
                break;

            case "BY_LIB":
                if (is_label_asc) {
                    bureauList.sort(Comparator.comparing(Bureau::getLabel_bureau));
                    filterList.addFilter("Order", "BY_LIB_ASC");
                }
                else {
                    bureauList.sort(Comparator.comparing(Bureau::getLabel_bureau).reversed());
                    filterList.addFilter("Order", "BY_LIB_DSC");
                }
                is_label_asc = !is_label_asc;
                break;

            case "BY_CP":
                if (is_cp_asc) {
                    bureauList.sort(Comparator.comparing(Bureau::getCode_postal_bureau));
                    filterList.addFilter("Order", "BY_CP_ASC");
                }
                else {
                    bureauList.sort(Comparator.comparing(Bureau::getCode_postal_bureau).reversed());
                    filterList.addFilter("Order", "BY_CP_DSC");
                }
                is_cp_asc = !is_cp_asc;
                break;

            default:
                break;
        }
    }
}
