/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jm.com.dpbennett.jmts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import jm.com.dpbennett.business.entity.DatePeriod;
import jm.com.dpbennett.business.management.SearchManagement;
import jm.com.dpbennett.business.utils.BusinessEntityUtils;
import jm.com.dpbennett.business.utils.SearchParameters;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author dbennett
 */
@Named(value = "searchManager")
@SessionScoped
public class SearchManager implements SearchManagement, Serializable {

    @PersistenceUnit(unitName = "BSJDBPU")
    private EntityManagerFactory EMF1;
    private HashMap searhParameters;
    private String currentSearchParameterKey;
    private Main main;

    /**
     * Creates a new instance of SearchManager
     */
    public SearchManager() {

        searhParameters = new HashMap();
        currentSearchParameterKey = "Job Search";

        // FoodsDB search types and search fields
        ArrayList foodsDBSearchTypes = new ArrayList();
        ArrayList foodsDBSearchDateFields = new ArrayList();
        // Add search types
        foodsDBSearchTypes.add(new SelectItem("General", "General"));
        // Add search fields
        foodsDBSearchDateFields.add(new SelectItem("dateRegistered", "Date registered"));
        // Add new search parameters 
        searhParameters.put(
                "Food Factories Search",
                new SearchParameters(
                "Food Factories Search",
                null,
                false,
                foodsDBSearchTypes,
                true,
                "dateRegistered",
                false,
                foodsDBSearchDateFields,
                "General",
                new DatePeriod("This month", "month", null, null, false, false, false),
                ""));

        // Admin search types and search fields
        ArrayList adminSearchTypes = new ArrayList();
        ArrayList adminSearchDateFields = new ArrayList();
        // Add search types
        adminSearchTypes.add(new SelectItem("General", "General"));
        // Add search fields
        adminSearchDateFields.add(new SelectItem("dateEdited", "Date edited"));
        // Add new search parameters 
        searhParameters.put(
                "Admin Search",
                new SearchParameters(
                "Admin Search",
                null,
                false,
                adminSearchTypes,
                true,
                "dateEdited",
                true,
                adminSearchDateFields,
                "General",
                new DatePeriod("This month", "month", null, null, false, false, true),
                ""));

        // Job search types and search fields
        ArrayList jobTypes = new ArrayList();
        ArrayList jobSearchTypes = new ArrayList();
        ArrayList jobSearchDateFields = new ArrayList();
        // Add job type. NB currently not used in search
        jobTypes.add(new SelectItem("Earning jobs", "Earning jobs"));
        jobTypes.add(new SelectItem("Non-earning jobs", "Non-earning jobs"));
        jobTypes.add(new SelectItem("Earning/Non-earning jobs", "Earning/Non-earning jobs"));
        jobTypes.add(new SelectItem("Compliance non-earning jobs", "Compliance non-earning jobs"));
        // Add search types
        jobSearchTypes.add(new SelectItem("General", "General"));
        jobSearchTypes.add(new SelectItem("My jobs", "My jobs"));
        jobSearchTypes.add(new SelectItem("My department's jobs", "My department's jobs"));
        jobSearchTypes.add(new SelectItem("Parent jobs only", "Parent jobs only"));


        // Add search fields
        jobSearchDateFields.add(new SelectItem("dateSubmitted", "Date submitted"));
        jobSearchDateFields.add(new SelectItem("dateAndTimeEntered", "Date entered"));
        jobSearchDateFields.add(new SelectItem("dateCostingApproved", "Date costing approved"));
        jobSearchDateFields.add(new SelectItem("dateOfCompletion", "Date completed"));
        jobSearchDateFields.add(new SelectItem("expectedDateOfCompletion", "Exp'ted date of completion"));
        jobSearchDateFields.add(new SelectItem("dateSamplesCollected", "Date sample(s) collected"));
        jobSearchDateFields.add(new SelectItem("dateDocumentCollected", "Date document(s) collected"));
        // Add new search parameters 
        searhParameters.put(
                "Job Search",
                new SearchParameters(
                "Job Search",
                jobTypes,
                true,
                jobSearchTypes,
                true,
                "dateAndTimeEntered",
                true,
                jobSearchDateFields,
                "General",
                new DatePeriod("This month", "month", null, null, false, false, true),
                ""));

        // Service Request search types and search fields
        ArrayList servciceRequestSearchTypes = new ArrayList();
        ArrayList servciceRequestSearchDateFields = new ArrayList();
        // Add search types
        servciceRequestSearchTypes.add(new SelectItem("General", "General"));
        // Add search fields
        servciceRequestSearchDateFields.add(new SelectItem("dateSubmitted", "Date submitted"));
        servciceRequestSearchDateFields.add(new SelectItem("dateOfCompletion", "Date completed"));
        servciceRequestSearchDateFields.add(new SelectItem("expectedDateOfCompletion", "Exp'ted date of completion"));
        // Add new search parameters 
        searhParameters.put(
                "Service Request Search",
                new SearchParameters(
                "Service Request Search",
                null,
                false,
                servciceRequestSearchTypes,
                false,
                "dateSubmitted",
                true,
                servciceRequestSearchDateFields,
                "General",
                new DatePeriod("This month", "month", null, null, false, false, true),
                ""));

        // Standards Compliance search types and search fields
        ArrayList standardsComplianceSearchTypes = new ArrayList();
        ArrayList standardsComplianceSearchDateFields = new ArrayList();
        // Add search types
        standardsComplianceSearchTypes.add(new SelectItem("General", "General"));
        // Add search fields
        standardsComplianceSearchDateFields.add(new SelectItem("dateOfSurvey", "Date of survey"));
        // Add new search parameters 
        searhParameters.put(
                "Standards Compliance Search",
                new SearchParameters(
                "Standards Compliance Search",
                null,
                false,
                standardsComplianceSearchTypes,
                true,
                "dateOfSurvey",
                false,
                standardsComplianceSearchDateFields,
                "General",
                new DatePeriod("This month", "month", null, null, false, false, true),
                ""));

        // Legal Metrology search types and fields
        ArrayList legalMetSearchTypes = new ArrayList();
        ArrayList legalMetSearchDateFields = new ArrayList();
        // Add search types
        legalMetSearchTypes.add(new SelectItem("General", "General"));
        // Add search fields
        legalMetSearchDateFields.add(new SelectItem("dateIssued", "Date certified"));
        legalMetSearchDateFields.add(new SelectItem("expiryDate", "Expiry date"));

        searhParameters.put(
                "Legal Metrology Search",
                new SearchParameters(
                "Legal Metrology Search",
                null,
                false,
                legalMetSearchTypes,
                true,
                "dateIssued",
                true,
                legalMetSearchDateFields,
                "General",
                new DatePeriod("Custom", "custom",
                BusinessEntityUtils.createDate(2000, 0, 1), new Date(), false, false, false),
                ""));
    }

    public Main getMain() {
        if (main == null) {
            main = App.findBean("main");
        }
        return main;
    }

    private EntityManagerFactory getEMF1() {
        return EMF1;
    }

    public EntityManager getEntityManager1() {
        return getEMF1().createEntityManager();
    }

    /**
     * Get the list of search types that the user is allowed to see
     *
     * @return
     */
    public ArrayList getAuthorizedSearchTypes() {
        if (getCurrentSearchParameters().getName().equals("Job Search")) {
            // Filter list based on user's authorization
            EntityManager em = getEntityManager1();

            if (getMain().getUser(em).getPrivilege().getCanEditJob()
                    || getMain().getUser(em).getPrivilege().getCanEnterJob()
                    || getMain().getUser(em).getPrivilege().getCanEditInvoicingAndPayment()
                    || getMain().getUser(em).getEmployee().getDepartment().getPrivilege().getCanEditInvoicingAndPayment()
                    || getMain().getUser(em).getEmployee().getDepartment().getPrivilege().getCanEditJob()
                    || getMain().getUser(em).getEmployee().getDepartment().getPrivilege().getCanEnterJob()) {
                return getCurrentSearchParameters().getSearchTypes();
            } else {

                ArrayList newList = new ArrayList();
                for (Object obj : getCurrentSearchParameters().getSearchTypes()) {
                    SelectItem item = (SelectItem) obj;
                    if (!item.getLabel().equals("General")) {
                        newList.add(item);
                    }
                }

                return newList;
            }

        } else {
            return getCurrentSearchParameters().getSearchTypes();
        }

    }

    @Override
    public String getCurrentSearchParameterKey() {
        return currentSearchParameterKey;
    }

    @Override
    public void setCurrentSearchParameterKey(String currentSearchParameterKey) {
        this.currentSearchParameterKey = currentSearchParameterKey;
    }

    @Override
    public SearchParameters getCurrentSearchParameters() {
        return (SearchParameters) searhParameters.get(getCurrentSearchParameterKey());
    }

    public void updateDateField() {
        //doSearch();
    }

    public void updateSearchType() {
        //doSearch();
    }

    public void changeSearchDatePeriod() {
        getCurrentSearchParameters().getDatePeriod().initDatePeriod();
        //doJobSearch();
        //doSearch();
    }

    public void handleStartDateSelect(SelectEvent event) {
        //dateSearchPeriod.setStartDate(event.getDate());
        getCurrentSearchParameters().getDatePeriod().setStartDate((Date) event.getObject());
        //doSearch();
    }

    public void handleEndDateSelect(SelectEvent event) {
        //dateSearchPeriod.setEndDate(event.getDate());
        getCurrentSearchParameters().getDatePeriod().setEndDate((Date) event.getObject());
        //doSearch();
    }

    /**
     * For future implementation if necessary
     *
     * @param query
     * @return
     */
    public List<String> completeSearchText(String query) {
        List<String> suggestions = new ArrayList<>();

        return suggestions;
    }

    public void doSearch() {
        RequestContext context = RequestContext.getCurrentInstance();
        switch (getCurrentSearchParameterKey()) {
            case "Job Search":
                JobManager jm = App.findBean("jobManager");
                if (jm != null) {                   
                    jm.doJobSearch(getCurrentSearchParameters());
                    context.update("mainTabViewForm:mainTabView:jobsDatabaseTable");
                }
                break;
            case "Service Request Search":
                ServiceManager sm = App.findBean("serviceManager");
                if (sm != null) {
                    sm.doServiceRequestSearch(getCurrentSearchParameters());
                    context.update("mainTabViewForm:mainTabView:serviceRequestsDatabaseTable");
                }
                break;                          
        }
    }
}
