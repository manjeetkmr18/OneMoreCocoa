package com.webxdevelopments.onemorecocoa.views.dashboard

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.AlertUtil
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingActivity
import com.webxdevelopments.onemorecocoa.common_utils.CommonUtils
import com.webxdevelopments.onemorecocoa.databinding.ActivityDashboardBinding
import com.webxdevelopments.onemorecocoa.views.carts.CartListActivity
import com.webxdevelopments.onemorecocoa.views.dashboard.model.DashboardSideMenuModel
import com.webxdevelopments.onemorecocoa.views.firebase_utils.FirebaseProductsDB
import com.webxdevelopments.onemorecocoa.views.home.HomeFragment
import com.webxdevelopments.onemorecocoa.views.offers.OffersListFragment
import com.webxdevelopments.onemorecocoa.views.on_boarding.ProfileFragment
import com.webxdevelopments.onemorecocoa.views.products.ProductListFragment
import com.webxdevelopments.onemorecocoa.views.products.model.ProductsModel
import com.webxdevelopments.onemorecocoa.views.shop_by.ShopByCategoryFragment
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder


class DashboardActivity : BaseBindingActivity<ActivityDashboardBinding>(){
    private var TAG = DashboardActivity::class.java.simpleName.toString()
    private lateinit var slidingRootNav:SlidingRootNav
    val dashboardSideMenuList = ArrayList<DashboardSideMenuModel>()
    private lateinit var dashboardSideMenuAdapter:DashboardSideMenuAdapter

    override fun getLayoutID(): Int {
        return R.layout.activity_dashboard
    }

    override fun initUI() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Home"
        binding.toolbar.setTitleTextColor(Color.WHITE)
        slidingRootNav = SlidingRootNavBuilder(this)
            .withToolbarMenuToggle(binding.toolbar)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(true)
            .withMenuLayout(R.layout.layout_side_menu)
            .inject()
        setSideMenuData()
    }

    private fun setSideMenuData(){
        dashboardSideMenuList.add(DashboardSideMenuModel("Home",true))
        dashboardSideMenuList.add(DashboardSideMenuModel("Offers",  false))
        dashboardSideMenuList.add(DashboardSideMenuModel("Products",  false))
        dashboardSideMenuList.add(DashboardSideMenuModel("Shop by Flavour", false))
        dashboardSideMenuList.add(DashboardSideMenuModel("Logout", false))
        dashboardSideMenuAdapter = DashboardSideMenuAdapter(
            this, dashboardSideMenuList, object : DashboardSideMenuAdapter.DashboardSideMenuCallback{
                override fun onItemClick(position: Int) {
                    for(i in 0 until dashboardSideMenuList.size){
                        dashboardSideMenuList[i].isSelected = i == position
                    }
                    dashboardSideMenuAdapter.notifyDataSetChanged()
                    slidingRootNav.closeMenu(true)
                    showFragments(position)
                }

            }
        )

        val recySideMenu = findViewById<RecyclerView>(R.id.recySideMenu)
        val cardMenuProfileImage = findViewById<MaterialCardView>(R.id.cardMenuProfileImage)
        val tvMenuSideName = findViewById<TextView>(R.id.tvMenuSideName)

        recySideMenu.layoutManager = LinearLayoutManager(this)
        recySideMenu.adapter = dashboardSideMenuAdapter

        tvMenuSideName.setOnClickListener {
            slidingRootNav.closeMenu(true)
            showProfile()
        }
        cardMenuProfileImage.setOnClickListener {
            slidingRootNav.closeMenu(true)
            showProfile()
        }
        showHomeFragment()
    }

    private fun showFragments(position:Int) {
        when(position){
            0->{
                showHomeFragment()
            }
            1->{
                showOffersFragment()
            }
            2->{
                showProductsFragment()
            }
            3->{
                shopByFlavourFragment()
            }
            4->{
                showLogout()
            }
        }
    }

    private fun showLogout() {
        AlertUtil.showAlert(this,
            "Alert",
            "Are you sure you want to logout?",
            true,
            "Yes",
            "No",
            object : AlertUtil.AlertDialogBtnClick{
                override fun onPositiveBtnClick() {
                    Log.e(TAG,"onPositiveBtnClick")
                    CommonUtils.logoutUser(this@DashboardActivity)
                }

                override fun onNegativeBtnClick() {
                    Log.e(TAG,"onNegativeBtnClick")
                }

            }
        )
    }

    private fun showHomeFragment() {
        binding.toolbar.title = "Home"
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = HomeFragment(object:HomeScreenClickCallback{
            override fun onViewMoreClick() {
                for(i in 0 until dashboardSideMenuList.size){
                    if(i==2){
                        dashboardSideMenuList[i].isSelected = true
                    }else{

                        dashboardSideMenuList[i].isSelected = false
                    }
                }
                dashboardSideMenuAdapter.notifyDataSetChanged()
                slidingRootNav.closeMenu(true)
                showFragments(2)
            }
        })
        fragmentTransaction.replace(
            R.id.frame_container,
            fragment,
            "").commit()
    }

    private fun showProductsFragment() {
        binding.toolbar.title = "Products"
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = ProductListFragment()
        fragmentTransaction.replace(
            R.id.frame_container,
            fragment,
            "").commit()
    }

    private fun showOffersFragment() {
        binding.toolbar.title = "Offers"
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = OffersListFragment()
        fragmentTransaction.replace(
            R.id.frame_container,
            fragment,
            "").commit()
    }


    private fun shopByFlavourFragment() {
        binding.toolbar.title = "Shop by Flavour"
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = ShopByCategoryFragment()
        fragmentTransaction.replace(
            R.id.frame_container,
            fragment,
            "").commit()
    }

    private fun showProfile() {
        binding.toolbar.title = "Profile"
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = ProfileFragment()
        fragmentTransaction.replace(
            R.id.frame_container,
            fragment,
            "").commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_toolbar_top_side, menu)
        val item: MenuItem = menu!!.findItem(R.id.cart)
        MenuItemCompat.setActionView(item, R.layout.include_cart_item)
        val clCartItemMain = MenuItemCompat.getActionView(item) as ConstraintLayout
        val tvCartCount = clCartItemMain!!.findViewById(R.id.tvCartCount) as TextView
        val clCartItemClick = clCartItemMain!!.findViewById(R.id.clCartItemClick) as ConstraintLayout
        val ivCart = clCartItemMain!!.findViewById(R.id.ivCart) as ImageView
        clCartItemClick.setOnClickListener {
            showCart()
        }
        tvCartCount.setOnClickListener {
            showCart()
        }
        ivCart.setOnClickListener {
            showCart()
        }

        tvCartCount.text = "2"
        return super.onCreateOptionsMenu(menu)
    }

    private fun showCart() {
        startActivity(Intent(this, CartListActivity::class.java))
    }

    public interface HomeScreenClickCallback{
        fun onViewMoreClick()
    }
}