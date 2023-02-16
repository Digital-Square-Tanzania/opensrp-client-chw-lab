package org.smartregister.chw.cdp.presenter;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.cdp.contract.BaseOrdersRegisterFragmentContract;
import org.smartregister.chw.cdp.util.Constants;
import org.smartregister.chw.cdp.util.DBConstants;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;

import timber.log.Timber;

import static org.apache.commons.lang3.StringUtils.trim;

public class BaseOrdersRegisterFragmentPresenter implements BaseOrdersRegisterFragmentContract.Presenter {
    protected WeakReference<BaseOrdersRegisterFragmentContract.View> viewReference;
    protected BaseOrdersRegisterFragmentContract.Model model;
    private String taskFocus;

    public BaseOrdersRegisterFragmentPresenter(BaseOrdersRegisterFragmentContract.View view, BaseOrdersRegisterFragmentContract.Model model) {
        this.viewReference = new WeakReference<>(view);
        this.model = model;
    }

    @Override
    public void setTasksFocus(String taskFocus) {
        this.taskFocus = taskFocus;
    }

    @Override
    public String getMainCondition() {
        return getMainTable() + "." + DBConstants.KEY.IS_CLOSED + " IS 0";
    }

    @Override
    public String getDefaultSortQuery() {
        return getMainTable() + "." + DBConstants.KEY.REQUESTED_AT + " DESC ";
    }

    @Override
    public String getSentOrdersQuery() {
        return Constants.TABLES.TASK + "." + DBConstants.KEY.STATUS + " = '" + Constants.OrderStatus.READY + "'" + " OR " + Constants.TABLES.TASK + "." + DBConstants.KEY.STATUS + " = '" + Constants.OrderStatus.IN_TRANSIT + "'";
    }

    @Override
    public String getSuccessFulOrdersQuery() {
        return Constants.TABLES.TASK + "." + DBConstants.KEY.STATUS + " = '" + Constants.OrderStatus.COMPLETE+"'";
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
    public String getMainTable() {
        return Constants.TABLES.CDP_ORDERS;
    }

    @Override
    public void processViewConfigurations() {
        //
    }


    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = getMainTable();
        mainCondition = trim(getMainCondition()).equals("") ? mainCondition : getMainCondition();
        String countSelect = model.countSelect(tableName, mainCondition);
        String mainSelect = model.mainSelect(tableName, mainCondition);

        if (getView() != null) {

            getView().initializeQueryParams(tableName, countSelect, mainSelect);
            getView().initializeAdapter(tableName);

            getView().countExecute();
            getView().filterandSortInInitializeQueries();
        }
    }

    protected BaseOrdersRegisterFragmentContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    @Override
    public void startSync() {
        //
    }

    @Override
    public void searchGlobally(String uniqueId) {
        //
    }
}
