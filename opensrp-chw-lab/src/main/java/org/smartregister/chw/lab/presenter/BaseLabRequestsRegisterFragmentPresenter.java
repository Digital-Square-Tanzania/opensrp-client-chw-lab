package org.smartregister.chw.lab.presenter;

import static org.apache.commons.lang3.StringUtils.trim;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.lab.contract.BaseLabRegisterFragmentContract;
import org.smartregister.chw.lab.util.Constants;
import org.smartregister.chw.lab.util.DBConstants;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Set;
import java.util.TreeSet;

import timber.log.Timber;

public class BaseLabRequestsRegisterFragmentPresenter implements BaseLabRegisterFragmentContract.Presenter {

    protected WeakReference<BaseLabRegisterFragmentContract.View> viewReference;

    protected BaseLabRegisterFragmentContract.Model model;

    protected RegisterConfiguration config;

    protected Set<View> visibleColumns = new TreeSet<>();
    protected String viewConfigurationIdentifier;

    public BaseLabRequestsRegisterFragmentPresenter(BaseLabRegisterFragmentContract.View view, BaseLabRegisterFragmentContract.Model model, String viewConfigurationIdentifier) {
        this.viewReference = new WeakReference<>(view);
        this.model = model;
        this.viewConfigurationIdentifier = viewConfigurationIdentifier;
        this.config = model.defaultRegisterConfiguration();
    }

    @Override
    public String getMainCondition() {
        return " patient_id <> '' AND entity_id <>'' AND is_closed = 0";
    }

    @Override
    public String getDefaultSortQuery() {
        return getMainTable() + "." + DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ";
    }

    @Override
    public void processViewConfigurations() {
        if (StringUtils.isBlank(viewConfigurationIdentifier)) {
            return;
        }

        ViewConfiguration viewConfiguration = model.getViewConfiguration(viewConfigurationIdentifier);
        if (viewConfiguration != null) {
            config = (RegisterConfiguration) viewConfiguration.getMetadata();
            this.visibleColumns = model.getRegisterActiveColumns(viewConfigurationIdentifier);
        }

        if (config.getSearchBarText() != null && getView() != null) {
            getView().updateSearchBarHint(config.getSearchBarText());
        }
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = getMainTable();
        mainCondition = trim(getMainCondition()).equals("") ? mainCondition : getMainCondition();
        String countSelect = model.countSelect(tableName, mainCondition);
        String mainSelect = model.mainSelect(tableName, mainCondition);

        if (getView() != null) {

            getView().initializeQueryParams(tableName, countSelect, mainSelect);
            getView().initializeAdapter(visibleColumns);

            getView().countExecute();
            getView().filterandSortInInitializeQueries();
        }
    }

    protected BaseLabRegisterFragmentContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    @Override
    public void startSync() {
//        implement

    }

    @Override
    public void searchGlobally(String s) {
//        implement

    }

    @Override
    public String getMainTable() {
        return Constants.TABLES.LAB_TEST_REQUESTS;
    }

    @Override
    public String getTestSamplesWithResultsQuery() {
        return Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.RESULTS + " IS NOT NULL AND " + Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.PATIENT_ID + " IS NOT NULL";
    }

    @Override
    public String getTestSamplesWithNoResultsQuery() {
        return Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.RESULTS + " IS NULL AND " + Constants.TABLES.LAB_TEST_REQUESTS + "." + DBConstants.KEY.PATIENT_ID + " IS NOT NULL";
    }

    @Override
    public String getDefaultFilterSortQuery(String filter, String mainSelect, String sortQueries, RecyclerViewPaginatedAdapter clientAdapter) {
        SmartRegisterQueryBuilder sqb = new SmartRegisterQueryBuilder(mainSelect);

        String query = "";
        StringBuilder customFilter = new StringBuilder();

        if (StringUtils.isNotBlank(filter)) {
            customFilter.append(MessageFormat.format((" and ( {0} ) "), filter));
        }
        try {
            sqb.addCondition(customFilter.toString());
            query = sqb.orderbyCondition(sortQueries);
            query = sqb.Endquery(sqb.addlimitandOffset(query, clientAdapter.getCurrentlimit(), clientAdapter.getCurrentoffset()));
        } catch (Exception e) {
            Timber.e(e, e.toString());
        }
        return query;

    }

    @Override
    public String getDueFilterCondition() {
        return " (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(cdp_test_date,7,4)|| '-' || SUBSTR(cdp_test_date,4,2) || '-' || SUBSTR(cdp_test_date,1,2),'')) as integer) between 7 and 14) ";
    }
}
