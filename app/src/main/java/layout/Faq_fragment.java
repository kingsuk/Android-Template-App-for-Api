package layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import us.tier5.u_rang.AsyncResponse;
import us.tier5.u_rang.CheckNetwork;
import us.tier5.u_rang.R;
import us.tier5.u_rang.RegisterUser;
import us.tier5.u_rang.UserConstants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Faq_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Faq_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Faq_fragment extends Fragment implements AsyncResponse.Response{
    // Own custom variables
    RegisterUser registerUser = new RegisterUser("POST");
    HashMap<String, String> data = new HashMap<String,String>();
    String route = "/V1/get-faq";
    LinearLayout lm;
    ProgressDialog loading;
    Bundle mySaveInstanceState;
    ArrayList<AsyncTask> asyncTasksArr = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Faq_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Faq_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Faq_fragment newInstance(String param1, String param2) {
        Faq_fragment fragment = new Faq_fragment();
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
        //setting view elements
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("FAQ");
        mySaveInstanceState = savedInstanceState;
        View fragView = inflater.inflate(R.layout.content_faq, container, false);

        lm = (LinearLayout) fragView.findViewById(R.id.llParent);

        if(CheckNetwork.isInternetAvailable(getContext())) //returns true if internet available
        {
            registerUser.delegate = this;
            registerUser.register(data,route);
            loading = ProgressDialog.show(getContext(), "Please Wait",null, true, true);
        }
        else
        {
            Toast.makeText(getContext(),"No internet connection",Toast.LENGTH_SHORT).show();
        }


        return fragView;
    }

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
    public void onDestroy() {
        for (int i=0; i<asyncTasksArr.size();i++)
        {
            asyncTasksArr.get(i).cancel(true);
            //Log.i("kingsukmajumder","stopped "+i+" async task");
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        for (int i=0; i<asyncTasksArr.size();i++)
        {
            asyncTasksArr.get(i).cancel(true);
            //Log.i("kingsukmajumder","stopped "+i+" async task");
        }
        super.onPause();
    }

    @Override
    public void processFinish(String output) {
        loading.dismiss();

        try
        {
            JSONObject jsonObject = new JSONObject(output);
            if(jsonObject.getBoolean("status"))
            {
                String arrayString = jsonObject.getString("response");
                JSONArray jsonArray = new JSONArray(arrayString);

                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject faqObj = jsonArray.getJSONObject(i);

                    String question = faqObj.getString("question");
                    String answer = Html.fromHtml(faqObj.getString("answer")).toString();
                    String imageName = faqObj.getString("image");
                    final String imageUrl = UserConstants.BASE_URL+UserConstants.IMAGE_FOLDER+imageName;

                    View inflatedLayout= getLayoutInflater(mySaveInstanceState).inflate(R.layout.neighborhood, null, false);
                    TextView title = (TextView) inflatedLayout.findViewById(R.id.tvNeighborhoodTitle);
                    title.setText(question);
                    TextView text = (TextView) inflatedLayout.findViewById(R.id.tvNeighborhoodText);
                    text.setText(answer);
                    final TextView loadingTV = (TextView) inflatedLayout.findViewById(R.id.tvNeighborLoading);
                    final ImageView imageViewNeigbor = (ImageView) inflatedLayout.findViewById(R.id.ivServiceImage);

                   AsyncTask asyncTask = new AsyncTask<Void, Void, Void>() {
                        Bitmap bmp;
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }
                        @Override
                        protected Void doInBackground(Void... params) {

                                try {
                                    InputStream in = new URL(imageUrl).openStream();
                                    bmp = BitmapFactory.decodeStream(in);
                                } catch (Exception e) {
                                    Toast.makeText(getContext(),"Some error occoured while loading images!",Toast.LENGTH_LONG).show();
                                    Log.i("kingsukmajumder","error in loading images "+e.toString());
                                }


                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            //loading.dismiss();
                            if (bmp != null)
                                loadingTV.setVisibility(View.INVISIBLE);
                            imageViewNeigbor.setImageBitmap(bmp);
                        }
                    }.execute();

                    asyncTasksArr.add(asyncTask);

                    lm.addView(inflatedLayout);//adding the total laout to parent layout
                }

            }
            else
            {
                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            Log.i("kingsukmajumder",e.toString());
        }
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
        void onFragmentInteraction(String swag);
    }
}
