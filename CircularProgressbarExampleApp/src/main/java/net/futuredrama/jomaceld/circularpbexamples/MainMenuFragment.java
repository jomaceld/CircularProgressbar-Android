/*
* Copyright 2016 Jose Antonio Maestre Celdran
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.futuredrama.jomaceld.circularpbexamples;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.futuredrama.jomaceld.circularpbexamples.ExampleFragments.Example2Fragment;
import net.futuredrama.jomaceld.circularpbexamples.ExampleFragments.Example1Fragment;


public class MainMenuFragment extends Fragment {

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Button bExample1 = (Button) rootView.findViewById(R.id.button_ex1);

        bExample1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                Fragment fragment = new Example1Fragment();
                trans.replace(R.id.container, fragment,"");
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        Button bExample2 = (Button) rootView.findViewById(R.id.button_ex2);

        bExample2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                Fragment fragment = new Example2Fragment();
                trans.replace(R.id.container, fragment,"");
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        return rootView;
    }

}
