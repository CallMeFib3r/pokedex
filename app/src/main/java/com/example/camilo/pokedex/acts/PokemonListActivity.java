package com.example.camilo.pokedex.acts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.camilo.pokedex.R;
import com.example.camilo.pokedex.adapters.PokemonListAdapter;
import com.example.camilo.pokedex.models.Pokemon;
import com.example.camilo.pokedex.services.PokemonService;
import com.example.camilo.pokedex.utils.PokemonListFetcher;
import com.example.camilo.pokedex.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PokemonListActivity extends AppCompatActivity implements PokemonService {

    @BindView(R.id.pokemon_list_recyclerview)
    RecyclerView mPokemonListRecyclerView;
    private PokemonListAdapter mPokemonListAdapter;
    private int mOffset;
    public static boolean mMustCharge;
    private GridLayoutManager mLayoutManager;
    private PokemonListFetcher mPokemonListFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        mMustCharge = true;
        mOffset = 0;
        mPokemonListFetcher.callPokemonApi(this, mOffset, mPokemonListAdapter);
    }

    @Override
    public void renderPokemonList(List<Pokemon> pokemonList) {
        mPokemonListAdapter.addNewPokemonList(pokemonList);
    }

    @Override
    public void onPokemonItemClick(Pokemon pokemon, int pos, ImageView img) {
        Intent intent = new Intent(PokemonListActivity.this, EstadoPokemon.class);
        intent.putExtra(Utils.EXTRA_POKEMON_NAME, pokemon.getName());
        intent.putExtra(Utils.EXTRA_POKEMON_ID, pos);

        if (img.getDrawable() != null)  // Check if the image loaded successfully
        {
            Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
            intent.putExtra(Utils.EXTRA_POKEMON_IMAGE, bitmap);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.pokemon_list_image_msg), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        mPokemonListFetcher = new PokemonListFetcher(this);
        mPokemonListAdapter = new PokemonListAdapter(this, this);

        mPokemonListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    // Checks if must charge Pokemon and if the recyclerView has reached the bottom
                    if (mMustCharge && visibleItemCount + pastVisibleItems >= totalItemCount) {
                        mMustCharge = false;
                        mOffset += 20;
                        mPokemonListFetcher.callPokemonApi(PokemonListActivity.this, mOffset, mPokemonListAdapter);

                    }
                }
            }
        });
        // Apply recyclerView specs
        mPokemonListRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 3);
        mPokemonListRecyclerView.setLayoutManager(mLayoutManager);
        mPokemonListRecyclerView.setAdapter(mPokemonListAdapter);
    }
}
