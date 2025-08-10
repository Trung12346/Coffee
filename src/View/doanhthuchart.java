package View;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;


public class doanhthuchart extends javax.swing.JFrame {

   private DefaultTableModel model;

    public doanhthuchart(DefaultTableModel model) {
        this.model = model; // Gán model được truyền vào
        initComponents();
        JFreeChart chart = createchart(createdataset());
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));

        setContentPane(chartPanel); 
        setTitle("Biểu đồ doanh thu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng cửa sổ hiện tại
        pack(); 
        setLocationRelativeTo(null); 
        setVisible(true);
    }
      public JFreeChart createchart(CategoryDataset dataset) {
      JFreeChart barchart = ChartFactory.createBarChart(
            "Biểu đồ doanh thu", 
            "Tháng", 
            "Doanh thu", 
            dataset, 
            org.jfree.chart.plot.PlotOrientation.VERTICAL, 
            true, 
            true, 
            true
        );
        return barchart;
    }
    public CategoryDataset createdataset() { // Phương thức instance
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> monthlyRevenue = new HashMap<>();

        // Kiểm tra nếu model là null
        if (model == null) {
            System.out.println("Error: Model is null");
            return dataset;
        }

        // Lặp qua từng hàng trong JTable
        for (int i = 0; i < model.getRowCount(); i++) {
            String dateStr = (String) model.getValueAt(i, 1); // Cột "Ngày xuất hóa đơn"
            Float revenue = (Float) model.getValueAt(i, 7);   // Cột "Thành tiền"

            // Lấy tháng từ ngày (giả sử định dạng là "yyyy-MM-dd HH:mm:ss")
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = sdf.parse(dateStr);
                SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
                String monthKey = monthFormat.format(date);

                // Cộng dồn doanh thu theo tháng
                monthlyRevenue.put(monthKey, monthlyRevenue.getOrDefault(monthKey, 0.0) + revenue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Thêm dữ liệu vào dataset
        for (Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {
            dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey());
        }

        return dataset;
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(doanhthuchart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // Tạo model mẫu để kiểm tra
            DefaultTableModel model = new DefaultTableModel(
                new Object[][] {
                    {1, "2025-08-10 10:00:00", null, null, null, null, null, 100.0f},
                    {2, "2025-08-15 14:00:00", null, null, null, null, null, 150.0f},
                    {3, "2025-09-01 09:00:00", null, null, null, null, null, 200.0f}
                },
                new String[] {"Mã Hóa đơn", "Ngày xuất hóa đơn", "Products", "Vouchers", "Mã hội viên", "Nhân viên tạo", "Chiết khấu", "Thành tiền"}
            );
            new doanhthuchart(model).setVisible(true); // Gọi constructor với model
        });
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}