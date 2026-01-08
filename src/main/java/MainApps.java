import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainApps { // Namanya tetap MainApps
    public static void main(String[] args) {
        // Sesuaikan path ini dengan folder kamu
        String pathFile = "src/main/resources/MOCK_DATA.json";
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        try {
            File fileJson = new File(pathFile);
            List<Transaction> listData = mapper.readValue(new FileInputStream(fileJson), new TypeReference<List<Transaction>>(){});

            System.out.println(" Data Berhasil Dibaca. Membuka Dashboard...");

            // 1. Dataset untuk Pie Chart (Total Pengeluaran per Kategori)
            DefaultPieDataset pieDataset = new DefaultPieDataset();
            listData.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory, 
                         Collectors.summingDouble(t -> t.getAmount().doubleValue())))
                .forEach(pieDataset::setValue);

            // 2. Dataset untuk Bar Chart (Jumlah Transaksi per Metode Pembayaran)
            DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
            listData.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentMethod, Collectors.counting()))
                .forEach((metode, jumlah) -> barDataset.addValue(jumlah, "Transaksi", metode));

            // 3. Membuat Window untuk menampilkan kedua grafik
            showDashboard(pieDataset, barDataset);

        } catch (Exception e) {
            System.out.println(" Ada masalah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showDashboard(DefaultPieDataset pieData, DefaultCategoryDataset barData) {
        // Membuat Grafik Pie
        JFreeChart pieChart = ChartFactory.createPieChart("Distribusi Pengeluaran", pieData, true, true, false);
        
        // Membuat Grafik Bar
        JFreeChart barChart = ChartFactory.createBarChart("Metode Pembayaran", "Metode", "Jumlah", barData, PlotOrientation.VERTICAL, false, true, false);

        // Menampilkan dalam satu jendela (Split Pane)
        JFrame frame = new JFrame("Dashboard Keuangan Rayyan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new java.awt.GridLayout(1, 2)); // Membagi layar jadi 2 kolom
        
        frame.add(new ChartPanel(pieChart));
        frame.add(new ChartPanel(barChart));
        
        frame.pack();
        frame.setSize(1000, 500); // Atur ukuran jendela
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}