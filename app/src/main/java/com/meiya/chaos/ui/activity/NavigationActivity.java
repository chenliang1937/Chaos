package com.meiya.chaos.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.andexert.library.RippleView;
import com.gigamole.library.NavigationTabBar;
import com.meiya.chaos.R;
import com.meiya.chaos.common.base.BaseActivity;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.ui.adapter.BusSegmentListAdapter;
import com.meiya.chaos.ui.adapter.DriveSegmentListAdapter;
import com.meiya.chaos.ui.adapter.WalkSegmentListAdapter;
import com.meiya.chaos.utils.AMapUtil;
import com.meiya.chaos.widget.NewListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenliang3 on 2016/5/21.
 */
public class NavigationActivity extends BaseActivity implements LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener{

    @BindView(R.id.navig_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.navig_toolbar)
    Toolbar toolbar;
    @BindView(R.id.navig_swap)
    RippleView swapBtn;
    @BindView(R.id.navig_your_location)
    EditText locationEt;
    @BindView(R.id.navig_you_wantgo)
    EditText destinationEt;
    @BindView(R.id.navig_navitabbar)
    NavigationTabBar navigationTabBar;
    @BindView(R.id.navig_build)
    RippleView buildBtn;
    @BindView(R.id.navig_map)
    MapView mapView;
    @BindView(R.id.navig_icon1)
    ImageView icon1;
    @BindView(R.id.navig_text1)
    TextView text1;
    @BindView(R.id.navig_text2)
    TextView text2;
    @BindView(R.id.navig_text3)
    TextView text3;
    @BindView(R.id.navig_text4)
    TextView text4;
    @BindView(R.id.navig_listview)
    NewListView listView;

    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationClientOption;

    private String localCity;//当前所在城市
    private String localCityCode;
    private GeocodeSearch startSearch;
    private GeocodeSearch endSearch;
    private LatLonPoint startPoint;
    private LatLonPoint endPoint;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private WalkRouteResult mWalkRouteResult;

    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_navigation;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);//此方法必须重写
        initMap();
    }

    @Override
    protected void initViews() {
        toolbar.setTitle("导航");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model(getResources().getDrawable(R.mipmap.ic_my_location), Color.WHITE));
        models.add(new NavigationTabBar.Model(getResources().getDrawable(R.mipmap.ic_walk), Color.WHITE));
        models.add(new NavigationTabBar.Model(getResources().getDrawable(R.mipmap.ic_car), Color.WHITE));
        models.add(new NavigationTabBar.Model(getResources().getDrawable(R.mipmap.ic_bus), Color.WHITE));
        navigationTabBar.setModels(models);
        navigationTabBar.setModelIndex(0, true);
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                getLatLon(locationEt.getText().toString().trim(), startSearch);
                getLatLon(destinationEt.getText().toString().trim(), endSearch);
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
                switch (index) {
                    case 0://定位
                        setVisibleOrGone(View.GONE);
                        aMap.setLocationSource(NavigationActivity.this);//设置定位监听
                        break;
                    case 1://步行
                        icon1.setVisibility(View.VISIBLE);
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        text3.setVisibility(View.GONE);
                        text4.setVisibility(View.GONE);
                        icon1.setImageResource(R.mipmap.ic_walk_purple);
                        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
                        break;
                    case 2://自驾
                        setVisibleOrGone(View.VISIBLE);
                        icon1.setImageResource(R.mipmap.ic_car_purple);
                        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                        break;
                    case 3://公交
                        icon1.setVisibility(View.VISIBLE);
                        text1.setVisibility(View.GONE);
                        text2.setVisibility(View.GONE);
                        text3.setVisibility(View.GONE);
                        text4.setVisibility(View.GONE);
                        icon1.setImageResource(R.mipmap.ic_bus_purple);
                        searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick(R.id.navig_build)
    public void buildBtn(){
        getLatLon(locationEt.getText().toString().trim(), startSearch);
        getLatLon(destinationEt.getText().toString().trim(), endSearch);
    }

    @OnClick(R.id.navig_swap)
    public void swapBtn(){
        String tmp_loc = locationEt.getText().toString().trim();
        String tmp_des = destinationEt.getText().toString().trim();
        locationEt.setText(tmp_des);
        destinationEt.setText(tmp_loc);
    }

    private void setVisibleOrGone(int i){
        icon1.setVisibility(i);
        text1.setVisibility(i);
        text2.setVisibility(i);
        text3.setVisibility(i);
        text4.setVisibility(i);
    }

    private void initMap(){
        setVisibleOrGone(View.GONE);

        aMap = mapView.getMap();
        aMap.setLocationSource(this);//设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);//显示定位层并可触发定位
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
        startSearch = new GeocodeSearch(this);
        startSearch.setOnGeocodeSearchListener(new MyStartGeocodeSearchListener());
        endSearch = new GeocodeSearch(this);
        endSearch.setOnGeocodeSearchListener(new MyEndGeocodeSearchListener());
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
    }

    private void setFromandtoMarker(){
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(startPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(endPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
    }

    private void searchRouteResult(int routeType, int mode){

        if (startPoint == null){
            Snackbar.make(mapView, "起点未设置", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (endPoint == null){
            Snackbar.make(mapView, "终点未设置", Snackbar.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        setFromandtoMarker();
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        if (routeType == ROUTE_TYPE_BUS){
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    localCityCode, 0);//第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        }else if (routeType == ROUTE_TYPE_DRIVE){
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode,
                    null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }else if (routeType == ROUTE_TYPE_WALK){
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(Event event){

    }

    /**
     * 定位成功后回调函数
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
                mListener.onLocationChanged(aMapLocation);//显示系统小蓝点
                localCity = aMapLocation.getCity();
                localCityCode = aMapLocation.getCityCode();
            }else {
                Snackbar.make(mapView, "定位失败," + aMapLocation.getErrorCode() + aMapLocation.getErrorInfo(), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 激活定位
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null){
            mLocationClient = new AMapLocationClient(this);
            mLocationClientOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationClientOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null){
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 响应地理编码
     * @param name
     */
    private void getLatLon(String name, GeocodeSearch search){
        GeocodeQuery query = new GeocodeQuery(name, localCity);
        search.getFromLocationNameAsyn(query);
    }

    private class MyStartGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener{

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        }

        @Override
        public void onGeocodeSearched(GeocodeResult result, int rCode) {
            if (rCode == 1000){
                if (result != null &&  result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0){
                    GeocodeAddress startAddress = result.getGeocodeAddressList().get(0);
                    startPoint = startAddress.getLatLonPoint();
                }else {
                    Snackbar.make(mapView, "对不起，没有搜索到相关数据！", Snackbar.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(mapView, rCode, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private class MyEndGeocodeSearchListener implements GeocodeSearch.OnGeocodeSearchListener{

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        }

        @Override
        public void onGeocodeSearched(GeocodeResult result, int rCode) {
            if (rCode == 1000){
                if (result != null &&  result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0){
                    GeocodeAddress endAddress = result.getGeocodeAddressList().get(0);
                    endPoint = endAddress.getLatLonPoint();
                }else {
                    Snackbar.make(mapView, "对不起，没有搜索到相关数据！", Snackbar.LENGTH_SHORT).show();
                }
            }else {
                Snackbar.make(mapView, rCode, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        progressBar.setVisibility(View.GONE);
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000){
            if (result != null && result.getPaths() != null){
                if (result.getPaths().size() > 0){
                    mBusRouteResult = result;
                    BusSegmentListAdapter adapter = new BusSegmentListAdapter(NavigationActivity.this, mBusRouteResult);
                    listView.setAdapter(adapter);
                }else if (result != null && result.getPaths() == null){
                    Snackbar.make(mapView, "对不起，没有搜索到相关数据！", Snackbar.LENGTH_SHORT);
                }
            }else {
                Snackbar.make(mapView, "对不起，没有搜索到相关数据！", Snackbar.LENGTH_SHORT);
            }
        }else {
            Snackbar.make(mapView, errorCode, Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        progressBar.setVisibility(View.GONE);
        aMap.clear();
        if (errorCode == 1000){
            if (result != null && result.getPaths() != null){
                if (result.getPaths().size() > 0){
                    mDriveRouteResult = result;
                    DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos());
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    text1.setText("驾车路线详情");
                    text2.setText(AMapUtil.getFriendlyTime(dur));
                    text4.setText(AMapUtil.getFriendlyLength(dis));
                    text3.setText("打车约"+taxiCost+"元");
                    DriveSegmentListAdapter adapter = new DriveSegmentListAdapter(NavigationActivity.this, drivePath.getSteps());
                    listView.setAdapter(adapter);
                }else if (result != null && result.getPaths() == null){
                    Snackbar.make(mapView, "对不起，没有搜索到相关数据！", Snackbar.LENGTH_SHORT);
                }
            }else {
                Snackbar.make(mapView, "对不起，没有搜索到相关数据！", Snackbar.LENGTH_SHORT);
            }
        }else {
            Snackbar.make(mapView, errorCode, Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        progressBar.setVisibility(View.GONE);
        aMap.clear();
        if (errorCode == 1000){
            if (result != null && result.getPaths() != null){
                if (result.getPaths().size() > 0){
                    mWalkRouteResult = result;
                    WalkPath walkPath = mWalkRouteResult.getPaths().get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    text1.setText("步行路线详情");
                    text2.setText(des);
                    WalkSegmentListAdapter adapter = new WalkSegmentListAdapter(NavigationActivity.this, walkPath.getSteps());
                    listView.setAdapter(adapter);
                }else if (result != null && result.getPaths() == null){
                    Snackbar.make(mapView, "对不起，没有搜索到相关数据！", Snackbar.LENGTH_SHORT);
                }
            }else {
                Snackbar.make(mapView, "对不起，没有搜索到相关数据！", Snackbar.LENGTH_SHORT);
            }
        }else {
            Snackbar.make(mapView, errorCode, Snackbar.LENGTH_SHORT);
        }
    }
}
