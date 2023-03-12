package com.webxdevelopments.onemorecocoa.views.offers

import androidx.recyclerview.widget.LinearLayoutManager
import com.webxdevelopments.onemorecocoa.R
import com.webxdevelopments.onemorecocoa.common_utils.BaseBindingFragment
import com.webxdevelopments.onemorecocoa.databinding.FragmentOffersListBinding

class OffersListFragment : BaseBindingFragment<FragmentOffersListBinding>() {
    private var TAG = OffersListFragment::class.java.simpleName.toString()

    override fun getLayoutID(): Int {
        return R.layout.fragment_offers_list
    }

    override fun initUI() {

        setProductListAdapter()
    }

    private fun setProductListAdapter() {
        binding.recyclerOfferList.layoutManager = LinearLayoutManager(requireActivity())
        var offerListAdapter = OfferListAdapter(requireActivity(), ArrayList<String>(), "",object : OfferListAdapter.OfferListCallback{
            override fun onItemClick(position: Int) {
               /* val intent = Intent(requireActivity(), ProductDetailsActivity::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), cardView, "product_details")
                startActivity(intent, options.toBundle())*/
            }
        })
        binding.recyclerOfferList.adapter = offerListAdapter
    }
}