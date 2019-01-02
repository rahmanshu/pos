package com.trimitrasis.finosapps.Views.SplitBillView.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.trimitrasis.finosapps.ActivityResultPesanan;
import com.trimitrasis.finosapps.R;
import com.trimitrasis.finosapps.Views.PosView.Model.RingkasanOrderModel;
import com.trimitrasis.finosapps.Views.SplitBillView.Adapter.ListPesananAdapter;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import de.greenrobot.event.EventBus;


/**
 * Created by rahman on 9/26/2017.
 */

@EFragment(R.layout.fragment_column_pesanan)
public class listPesananFragment extends Fragment {

    @ViewById
    RecyclerView listPesanan;

    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<RingkasanOrderModel> ringkasanOrderModels;


    @AfterViews
    void afterView(){
        EventBus.getDefault().register(this);
        ringkasanOrderModels = new ArrayList<>();
        ringkasanOrderModels.clear();
        initSetTableView();
        initList();
    }



    private void initSetTableView(){
        listPesanan.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        listPesanan.setLayoutManager(mLayoutManager);
    }


    private void initList(){
        ListPesananAdapter adapter = new ListPesananAdapter(getActivity(), ringkasanOrderModels);
        listPesanan.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onEvent(ActivityResultPesanan event){
        insertDataOrder(event.getRingkasanOrderModel());
    }


    private void insertDataOrder(RingkasanOrderModel ringkasanOrder){

        boolean k = true; double totQty = 0;
        for(int i = 0; i < ringkasanOrderModels.size(); i ++){

            if(ringkasanOrderModels.get(i).getKodeBarcode().equals(ringkasanOrder.getKodeBarcode())){

                if(ringkasanOrder.getInfo().equals("delete")){

                    ringkasanOrderModels.remove(i);

                }else if(ringkasanOrder.getNote().equals("update pesanan")){

                    if(ringkasanOrderModels.get(i).getQty() == 1.0){
                        ringkasanOrderModels.remove(i);
                    }else if(ringkasanOrderModels.get(i).getQty() > 1.0){
                        ringkasanOrderModels.set(i, new RingkasanOrderModel(
                                ringkasanOrder.getItemId(),
                                ringkasanOrder.getKodeBarcode(),
                                ringkasanOrder.getKodeBarang(),
                                ringkasanOrder.getNamaBarang(),
                                ringkasanOrder.getSatuanBarang(),
                                ringkasanOrder.getHargaJual(),
                                ringkasanOrderModels.get(i).getQty() - 1.0,
                                ringkasanOrder.getDiskon(),
                                ringkasanOrder.getTax() * totQty,
                                ringkasanOrder.getInfo(),
                                ringkasanOrder.getStandart_cost(),
                                ringkasanOrder.isFlag_qty(),
                                ringkasanOrder.isFlag_bom(),
                                ringkasanOrder.getTaxGroup(),
                                ringkasanOrder.getNote(),
                                ringkasanOrder.getDetailId()
                        ));
                    }

                }else{

                    if(ringkasanOrder.getNote().equals("update bill")){
                        totQty = ringkasanOrderModels.get(i).getQty() + ringkasanOrder.getQty();
                    }

                    ringkasanOrderModels.set(i, new RingkasanOrderModel(
                            ringkasanOrder.getItemId(),
                            ringkasanOrder.getKodeBarcode(),
                            ringkasanOrder.getKodeBarang(),
                            ringkasanOrder.getNamaBarang(),
                            ringkasanOrder.getSatuanBarang(),
                            ringkasanOrder.getHargaJual(),
                            totQty, 0,
                            ringkasanOrder.getTax() * totQty,
                            "",
                            ringkasanOrder.getStandart_cost(),
                            ringkasanOrder.isFlag_qty(),
                            ringkasanOrder.isFlag_bom(),
                            ringkasanOrder.getTaxGroup(),
                            ringkasanOrder.getNote(),
                            ringkasanOrder.getDetailId()
                    ));

                }

                k = false;
                break;
            }
        }


        if(k == true && !ringkasanOrder.getInfo().equals("buy x get y")){

            ringkasanOrderModels.add(new RingkasanOrderModel(
                    ringkasanOrder.getItemId(),
                    ringkasanOrder.getKodeBarcode(),
                    ringkasanOrder.getKodeBarang(),
                    ringkasanOrder.getNamaBarang(),
                    ringkasanOrder.getSatuanBarang(),
                    ringkasanOrder.getHargaJual(),
                    ringkasanOrder.getQty(),
                    0,
                    ringkasanOrder.getTax(),
                    "",
                    ringkasanOrder.getStandart_cost(),
                    ringkasanOrder.isFlag_qty(),
                    ringkasanOrder.isFlag_bom(),
                    ringkasanOrder.getTaxGroup(),
                    ringkasanOrder.getNote(),
                    ringkasanOrder.getDetailId()
            ));

        }else if(k == true && (ringkasanOrder.getInfo().equals("buy x get y") && ringkasanOrder.getQty() > 1.0)){

            ringkasanOrderModels.add(new RingkasanOrderModel(
                    ringkasanOrder.getItemId(),
                    ringkasanOrder.getKodeBarcode(),
                    ringkasanOrder.getKodeBarang(),
                    ringkasanOrder.getNamaBarang(),
                    ringkasanOrder.getSatuanBarang(),
                    ringkasanOrder.getHargaJual(),
                    ringkasanOrder.getQty() - 1.0,
                    0,
                    ringkasanOrder.getTax(),
                    "",
                    ringkasanOrder.getStandart_cost(),
                    ringkasanOrder.isFlag_qty(),
                    ringkasanOrder.isFlag_bom(),
                    ringkasanOrder.getTaxGroup(),
                    ringkasanOrder.getNote(),
                    ringkasanOrder.getDetailId()
            ));

        }

        initList();

    }


    public void checkListPesanan(){
        if(ringkasanOrderModels == null || ringkasanOrderModels.size() == 0){
            getActivity().finish();
        }
    }


}
