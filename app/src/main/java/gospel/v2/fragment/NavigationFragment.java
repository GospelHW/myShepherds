/*
 * copyright (c)2018-8-15
 * DXC technology
 */

package gospel.v2.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import gospel.v2.R;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

/**
 * Created by gospel on 2017/8/18.
 * About Login
 */
public class NavigationFragment extends Fragment implements BottomNavigationBar.OnTabSelectedListener {


    private BottomNavigationBar mBottomNavigationBar;
    private MeasureMainFragment measureMainFragment;
    private SecurityFragment securityFragment;
    private GasFragment gasFragment;
    //    private CADFragment cadFragment;
    private TextView mTextView;
    Context context;
    static int modeltype = 0;//如果是1就跳转到气体检测fragment

    public static NavigationFragment newInstance(int model) {
        NavigationFragment navigationFragment = new NavigationFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.ARGS, s);
//        navigationFragment.setArguments(bundle);
        modeltype = model;
        return navigationFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_bottom_navigation_bar, container, false);
        mBottomNavigationBar = (BottomNavigationBar) view.findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        BadgeItem numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.actionbar_bg_color)
                .setText("5");
//                .setHideOnSelect(autoHide.isChecked());

        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.total, getString(R.string.item_measure)).setInactiveIconResource(R.mipmap.total1).setActiveColorResource(R.color.actionbar_bg_color).setInActiveColorResource(R.color.black_1).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.safe2, getString(R.string.item_security)).setInactiveIconResource(R.mipmap.safe1).setActiveColorResource(R.color.actionbar_bg_color).setInActiveColorResource(R.color.black_1))
                .addItem(new BottomNavigationItem(R.mipmap.gas1, getString(R.string.item_gas)).setInactiveIconResource(R.mipmap.gas).setActiveColorResource(R.color.actionbar_bg_color).setInActiveColorResource(R.color.black_1))
                .addItem(new BottomNavigationItem(R.mipmap.cad, getString(R.string.item_cad)).setInactiveIconResource(R.mipmap.cad2).setActiveColorResource(R.color.actionbar_bg_color).setInActiveColorResource(R.color.black_1))
                .setFirstSelectedPosition(0)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);

        setDefaultFragment();
        return view;
    }

    /**
     * set the default fagment
     * <p>
     * the content id should not be same with the parent content id
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (modeltype == 0) {
            MeasureMainFragment homeFragment = measureMainFragment.newInstance(getString(R.string.item_measure));
            transaction.replace(R.id.sub_content, homeFragment).commit();
        }
        //1则直接定位到气体检测的Fragment
        if (modeltype == 1) {
            GasFragment gasFragment = GasFragment.newInstance(getString(R.string.item_gas));
            transaction.replace(R.id.sub_content, gasFragment).commit();
        }
    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();

        switch (position) {
            case 0:
                if (measureMainFragment == null) {
                    measureMainFragment = MeasureMainFragment.newInstance(getString(R.string.item_measure));
                }
                beginTransaction.replace(R.id.sub_content, measureMainFragment);
                break;
            case 1:
                if (securityFragment == null) {
                    securityFragment = SecurityFragment.newInstance(getString(R.string.item_security));
                }
                beginTransaction.replace(R.id.sub_content, securityFragment);
                break;
            case 2:
                if (gasFragment == null) {
                    gasFragment = GasFragment.newInstance(getString(R.string.item_gas));
                }
                beginTransaction.replace(R.id.sub_content, gasFragment);
                break;
            case 3:
//                if (cadFragment == null) {
//                    cadFragment = CADFragment.newInstance(getString(R.string.item_cad));
//                }
                break;
        }
        beginTransaction.commit();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
