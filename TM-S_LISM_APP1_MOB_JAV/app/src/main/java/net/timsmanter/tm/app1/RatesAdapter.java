package net.timsmanter.tm.app1;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class RatesAdapter extends ArrayAdapter<Rate> {

    private Context context;
    private int ratesViewId;
    private ArrayList<Rate> rates;

    public RatesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Rate> objects) {
        super(context, resource, objects);

        this.context = context;
        ratesViewId = resource;
        rates = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (convertView == null)
        {
            view = LayoutInflater.from(context).inflate(ratesViewId, null);


            RadioGroup ratesGroup = (RadioGroup) view.findViewById(R.id.ratesGroup);
            ratesGroup.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener()
                    {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId)
                        {
                            switch (checkedId) {
                                case R.id.radioButton2:
                                    rates.get(position).setRate(2);
                                    break;
                                case R.id.radioButton3:
                                    rates.get(position).setRate(3);
                                    break;
                                case R.id.radioButton4:
                                    rates.get(position).setRate(4);
                                    break;
                                case R.id.radioButton5:
                                    rates.get(position).setRate(5);
                                    break;
                            }
                        }
                    }
            );
            //powiązanie grupy przycisków z obiektem w modelu
        }
        //aktualizacja istniejącego wiersza
        else
        {
            view = convertView;
            //RadioGroup ratesGroup = (RadioGroup) view.findViewById(R.id.ratesGroup);
            //powiązanie grupy przycisków z obiektem w modelu
        }

        TextView rateText = (TextView) view.findViewById(R.id.rateText);
        rateText.setText(rates.get(position).getName());




        return view;
    }

}
