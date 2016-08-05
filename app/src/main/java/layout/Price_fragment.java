package layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import us.tier5.u_rang.AsyncResponse;
import us.tier5.u_rang.BeanClassForListView;
import us.tier5.u_rang.CheckNetwork;
import us.tier5.u_rang.R;
import us.tier5.u_rang.RegisterUser;
import us.tier5.u_rang.listViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Price_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Price_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Price_fragment extends Fragment implements AsyncResponse.Response{
    //own custom variables
    private ArrayList<BeanClassForListView> beanClassArrayList;
    private us.tier5.u_rang.listViewAdapter listViewAdapter;
    ListView listview;
    HashMap<String, String> data = new HashMap<String,String>();
    ProgressDialog loading;
    String route = "/V1/get-prices";
    RegisterUser registerUser = new RegisterUser("POST");
    Bundle mySaveInstanceState;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Price_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Price_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Price_fragment newInstance(String param1, String param2) {
        Price_fragment fragment = new Price_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setting view elements
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("PRICES");
        View fragView = inflater.inflate(R.layout.fragment_price_fragment, container, false);


        mySaveInstanceState = savedInstanceState;

        listview = (ListView) fragView.findViewById(R.id.listView2);
        beanClassArrayList = new ArrayList<BeanClassForListView>();


        if(CheckNetwork.isInternetAvailable(getContext())) //returns true if internet available
        {
            registerUser.delegate = this;
            loading = ProgressDialog.show(getContext(), "Please Wait",null, true, true);
            registerUser.register(data,route);
        }
        else
        {
            Toast.makeText(getContext(),"No internet connection",Toast.LENGTH_SHORT).show();
        }


        return fragView;
    }

    /*@Override
    public void onDestroy() {
        Thread.interrupted();
        Log.i("kingsukmajumder","OnDestroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.i("kingsukmajumder","onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i("kingsukmajumder","onResume");
        super.onResume();
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("uri");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processFinish(String output) {
        try{
            JSONObject jsonObject = new JSONObject(output);

            if(jsonObject.getBoolean("status"))
            {
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject currentObj = jsonArray.getJSONObject(i);
                    String categoryName = currentObj.getString("name");
                    JSONArray priceListArray = currentObj.getJSONArray("pricelists");
                    if(priceListArray.length()!=0)
                    {

                        for (int j=0;j<priceListArray.length();j++)
                        {
                            JSONObject currentPriceObj = priceListArray.getJSONObject(j);
                            String itemName = currentPriceObj.getString("item");
                            String itemPrice = currentPriceObj.getString("price");

                            BeanClassForListView beanClass = new BeanClassForListView(R.drawable.price, itemName, itemPrice,categoryName);
                            beanClassArrayList.add(beanClass);

                        }
                    }
                    else
                    {
                        //Log.i("kingsukmajumder","pricelist is 0");
                    }





                }
                listViewAdapter = new listViewAdapter(getContext(), beanClassArrayList);
                listview.setAdapter(listViewAdapter);

            }
            else
            {
                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"Error in fetching prices",Toast.LENGTH_SHORT).show();
            Log.i("kingsukmajumder",e.toString());
        }
        loading.dismiss();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String String);
    }
}
