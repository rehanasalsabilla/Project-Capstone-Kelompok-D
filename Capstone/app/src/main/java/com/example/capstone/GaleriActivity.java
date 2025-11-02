package com.example.capstone;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class GaleriActivity extends BaseActivity {

    LinearLayout containerGallery;

    // URL gambar per kategori
    String[][] imageUrlsByCategory = {
            {
                    "https://i.imgur.com/BQhjf05.jpeg",
                    "https://i.imgur.com/BO4sGdP.png",
                    "https://i.imgur.com/DOaNjRw.jpeg",
                    "https://i.imgur.com/STanVEw.jpeg"
            },
            {
                    "https://i.imgur.com/Wg79zQj.png",
                    "https://i.imgur.com/Y6LHQuf.png",
                    "https://i.imgur.com/3y4FgIS.jpeg",
                    "https://i.imgur.com/5YjtEyS.jpeg",
                    "https://i.imgur.com/uCg1ajV.jpeg",
                    "https://i.imgur.com/VQRF6Yu.jpeg"
            },
            {
                    "https://i.imgur.com/MZ5krL9.jpeg",
                    "https://i.imgur.com/Znc9mAK.jpeg",
                    "https://i.imgur.com/MGfb6nT.jpeg",
                    "https://i.imgur.com/6R8HMx3.jpeg",
                    "https://i.imgur.com/DnEwE4Q.jpeg",
                    "https://i.imgur.com/H0PpFUF.jpeg"
            }
    };

    // Gambar yang berbeda untuk tiap kategori
    int[] categoryImages = {
            R.drawable.ic_lomba,      // Gambar untuk kategori Lomba
            R.drawable.ic_kegiatan,   // Gambar untuk kategori Kegiatan
            R.drawable.ic_workshop    // Gambar untuk kategori Workshop
    };

    // Judul gambar untuk setiap kategori
    String[][] categoryTitles = {
            {"Anugerah PT Telkom Indonesia", "Juara Gemastik 2024 DTI", "IoT Project", "Anugerah PT Telkom Indonesia"},
            {"Bukber IT", "Wisuda IT", "IT Fest", "IT Anniversarry", "IT Care", "A Renewal Agent"},
            {"Workshop Third International Research in Computer Science and Information Systems 2022", "Workshop ISO270001 2016 SMKI 2024", "Kuliah Tamu AWC MK Penulisan Ilmiah", "Kunjungan APTIKOM", "Kuliah Tamu Kampus NUS Singapore", "Kunjungan Forensik"}
    };

    String[] categoryNames = {"Prestasi", "Kegiatan", "Workshop"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);

        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> finish());

        containerGallery = findViewById(R.id.containerGallery);

        for (int i = 0; i < imageUrlsByCategory.length; i++) {
            addCategorySection(categoryNames[i], imageUrlsByCategory[i], categoryImages[i], i);
        }
    }

    private void addCategorySection(String categoryName, String[] imageUrls, int categoryImageResId, int categoryIndex) {
        // Memuat layout kategori dengan gambar
        View categoryView = getLayoutInflater().inflate(R.layout.item_kategori, null);
        ImageView categoryImage = categoryView.findViewById(R.id.categoryImage);

        // Mengatur gambar kategori sesuai dengan kategori
        categoryImage.setImageResource(categoryImageResId);

        // Menambahkan kategori ke dalam container
        containerGallery.addView(categoryView);  // Add category image view

        // Membuat layout GridLayout untuk menampilkan gambar galeri
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(2);
        gridLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Menambahkan gambar ke GridLayout
        for (int i = 0; i < imageUrls.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.gallery_item, null);

            TextView title = view.findViewById(R.id.titleGallery);
            ImageView img = view.findViewById(R.id.imageGallery);

            // Mengatur judul sesuai kategori dan gambar
            title.setText(categoryTitles[categoryIndex][i]);
            title.setBackgroundResource(R.drawable.rounded_background);

            // Memuat gambar menggunakan Glide
            Glide.with(view.getContext())
                    .load(imageUrls[i])
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.ic_dialog_alert)
                    .into(img);

            final String imageUrl = imageUrls[i];

            // Mengatur listener untuk membuka gambar besar saat diklik
            img.setOnClickListener(v -> showFullImageDialog(imageUrl));

            // Mengatur layout GridLayout params
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = getResources().getDisplayMetrics().widthPixels / 2 - 48;
            params.setMargins(8, 8, 8, 8);
            view.setLayoutParams(params);

            // Menambahkan view ke gridLayout
            gridLayout.addView(view);
        }

        containerGallery.addView(gridLayout);
    }

    private void showFullImageDialog(String imageUrl) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_full_image);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        ImageView fullImage = dialog.findViewById(R.id.fullImage);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.ic_dialog_alert)
                .into(fullImage);

        fullImage.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
