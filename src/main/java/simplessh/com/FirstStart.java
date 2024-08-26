package simplessh.com;

import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

/**
 * @author Corneli F.
 *
 * will add a gif load untill the spring is loaded
 */
public class FirstStart {
    private JFrame frame = null;
    private JLabel loadLabelText = null;
    private JLabel loadLabelIMG= null;
    private JLabel loadLabelStarted= null;

    private ConfigurableApplicationContext springContext;

    public void setSpringContext(ConfigurableApplicationContext springContext){
        this.springContext=springContext;

    }

    public void showLoad(){
        frame =new JFrame();
        //obj.setBounds(10,10,300,200);
        frame.setLocationRelativeTo ( null );
        frame.setTitle("Start server...");
        frame.setResizable(true);

        JPanel panel = new JPanel();
        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton stopButton = new JButton("Stop server");

        stopButton.addActionListener(e -> {
            try{
            System.exit(0);
            springContext.close();
            }catch(Exception d){
                System.out.println("Error close:"+d.getMessage());
            }
        });

       panel.add(stopButton);


        JButton openWindowButton = new JButton("Open link in browser");
        openWindowButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try{
                    openWebpage(new URI("http://localhost:9211"));
                }catch(Exception d){ }
            }
        });
        panel.add(openWindowButton);
        panel.add(new JLabel("<html><br/> <br/>  <br/></html>"));

        loadLabelText = new JLabel("Start Server. Please wait... ", JLabel.CENTER);
        panel.add(loadLabelText);

        ImageIcon loading = new ImageIcon(FirstStart.class.getResource("/img/ajax-loader.gif"));
        loadLabelIMG = new JLabel("", loading, JLabel.CENTER);
        panel.add(loadLabelIMG);

        loadLabelStarted= new JLabel("<html>Server Started, <br/> open in the browser http://localhost:9211 </html>", SwingConstants.CENTER);
       //loadLabelStarted.setFont(new java.awt.Font("Verdana", 0, 13));
        loadLabelStarted.setVisible(false);
        panel.add(loadLabelStarted);

        frame.getContentPane().add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize ( 320, 180 );
   }

    public boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void hideLoad()   {
     if(loadLabelText != null)
         loadLabelText.setVisible(false);

     if(loadLabelIMG != null)
         loadLabelIMG.setVisible(false);

        if(loadLabelStarted != null)
            loadLabelStarted.setVisible(true);

       try {
           openWebpage(new URI("http://localhost:9211"));
       }catch (Exception e){}

    }


    /*
 btn_bin.setBackground(new java.awt.Color(255, 255, 255));
        btn_bin.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        btn_bin.setForeground(new java.awt.Color(102, 102, 102));
        btn_bin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        btn_bin.setText("Recycle Bin");
        btn_bin.setBorder(null);
        btn_bin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_bin.setPreferredSize(new java.awt.Dimension(140, 28));
        btn_bin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_binActionPerformed(evt);
            }
        });
*/
}
