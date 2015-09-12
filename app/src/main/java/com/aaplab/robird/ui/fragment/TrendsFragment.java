package com.aaplab.robird.ui.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aaplab.robird.data.entity.Account;
import com.aaplab.robird.data.model.TrendsModel;
import com.aaplab.robird.ui.adapter.TrendAdapter;
import com.aaplab.robird.util.DefaultObserver;

import java.util.ArrayList;
import java.util.List;

import icepick.Icicle;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import twitter4j.GeoLocation;
import twitter4j.Trend;

/**
 * Created by majid on 25.08.15.
 */
public class TrendsFragment extends BaseSwipeToRefreshRecyclerFragment implements LocationListener {
    public static TrendsFragment create(Account account) {
        final Bundle args = new Bundle();
        args.putParcelable("account", account);

        TrendsFragment fragment = new TrendsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Icicle
    ArrayList<Trend> mTrends;

    private LocationManager mLocationManager;
    private TrendsModel mTrendsModel;
    private Account mAccount;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mTrends = mTrends == null ? new ArrayList<Trend>() : mTrends;
        mAccount = getArguments().getParcelable("account");
        mTrendsModel = new TrendsModel(mAccount);

        if (savedInstanceState == null || mTrends.isEmpty()) {
            setRefreshing(true);
            updateTrends();
        } else {
            setupTrends(mTrends);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        updateTrends();
    }

    private void updateTrends() {
        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null)
            mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        else
            onLocationChanged(location);
    }

    private void setupTrends(ArrayList<Trend> trends) {
        mTrends = trends;
        mRecyclerView.setAdapter(new TrendAdapter(getActivity(), mAccount, mTrends));
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationManager.removeUpdates(this);
        mSubscriptions.add(
                mTrendsModel
                        .local(new GeoLocation(location.getLatitude(), location.getLongitude()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DefaultObserver<List<Trend>>() {
                            @Override
                            public void onNext(List<Trend> trends) {
                                super.onNext(trends);
                                setRefreshing(false);
                                setupTrends(new ArrayList<>(trends));
                            }
                        })
        );
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
