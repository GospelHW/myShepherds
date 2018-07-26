/*
 * copyright (c)2018-8-15
 * DXC technology
 */

package gospel.v2.fragment.subfragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gospel on 2017/8/18.
 * About Login
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private Context context;
    private String[] titles = {"  未完成  ", "  已完成  ", "  未上传  ", "  已上传  "};

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> list, Context context) {
        super(fm);
        this.list = list;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
////        return FragmentTest.instantiation(3);
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
