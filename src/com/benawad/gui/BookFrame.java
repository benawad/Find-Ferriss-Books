package com.benawad.gui;






import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BookFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public BookFrame(String sTitle, String sSubtitle, String sAuthors, String sPageCount, String sCategories, String sFormats, String sAmazon, String sGoogle, String sApple, String sDescription) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Main.TITLE);
        setBounds(100, 100, 850, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(new BorderLayout());
        panel.add(panel_1);

        JLabel title = new JLabel("<html><p>"+sTitle+"</p></html>");
        title.setFont(new Font("Lucida Grande", Font.PLAIN, 36));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel_1.add(title);

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(new BorderLayout());
        panel.add(panel_2);

        JLabel subtitle = new JLabel("<html>"+sSubtitle+"</html>");
        subtitle.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel_2.add(subtitle);

        JSeparator separator = new JSeparator();
        panel_2.add(separator, BorderLayout.SOUTH);

        JSeparator separator_1 = new JSeparator();
        panel_2.add(separator_1, BorderLayout.NORTH);

        JPanel panel_3 = new JPanel();
        contentPane.add(panel_3, BorderLayout.CENTER);
        panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));

        JPanel panel_4 = new JPanel();
        panel_3.add(panel_4);
        panel_4.setLayout(new GridLayout(2, 2, 0, 20));

        JLabel authors = new JLabel(sAuthors);
        authors.setHorizontalAlignment(SwingConstants.CENTER);
        panel_4.add(authors);

        JLabel pageCount = new JLabel(sPageCount);
        pageCount.setHorizontalAlignment(SwingConstants.CENTER);
        panel_4.add(pageCount);

        JLabel categories = new JLabel(sCategories);
        categories.setHorizontalAlignment(SwingConstants.CENTER);
        panel_4.add(categories);

        JLabel formats = new JLabel(sFormats);
        formats.setHorizontalAlignment(SwingConstants.CENTER);
        panel_4.add(formats);

        JPanel panel_5 = new JPanel();
        panel_3.add(panel_5);
        GridBagLayout gbl_panel_5 = new GridBagLayout();
        gbl_panel_5.columnWidths = new int[]{117, 0};
        gbl_panel_5.rowHeights = new int[]{29, 29, 29, 0};
        gbl_panel_5.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_panel_5.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
        panel_5.setLayout(gbl_panel_5);

        JLabel linkDescription = new JLabel("Available at:");
        GridBagConstraints gbc_linkDescription = new GridBagConstraints();
        gbc_linkDescription.insets = new Insets(0, 0, 5, 0);
        gbc_linkDescription.gridx = 0;
        gbc_linkDescription.gridy = 0;
        panel_5.add(linkDescription, gbc_linkDescription);

        JButton amazon = new JButton("<HTML><FONT color=\\\"#000099\\\"><U>"+sAmazon+"</U></FONT></HTML>");
        amazon.setHorizontalAlignment(SwingConstants.LEFT);
        amazon.addActionListener(new LinkListener(sAmazon));
        amazon.setBorderPainted(false);
        amazon.setOpaque(false);
        amazon.setBackground(Color.WHITE);
        amazon.setToolTipText(sAmazon);
        GridBagConstraints gbc_amazon = new GridBagConstraints();
        gbc_amazon.anchor = GridBagConstraints.WEST;
        gbc_amazon.insets = new Insets(0, 0, 5, 0);
        gbc_amazon.gridx = 0;
        gbc_amazon.gridy = 1;
        panel_5.add(amazon, gbc_amazon);

        JButton google = new JButton("<HTML><FONT color=\\\"#000099\\\"><U>"+sGoogle+"</U></FONT></HTML>");
        google.addActionListener(new LinkListener(sGoogle));
        google.setHorizontalAlignment(SwingConstants.LEFT);
        google.setBorderPainted(false);
        google.setOpaque(false);
        google.setBackground(Color.WHITE);
        google.setToolTipText(sGoogle);
        GridBagConstraints gbc_google = new GridBagConstraints();
        gbc_google.anchor = GridBagConstraints.WEST;
        gbc_google.insets = new Insets(0, 0, 5, 0);
        gbc_google.gridx = 0;
        gbc_google.gridy = 2;
        panel_5.add(google, gbc_google);

        JButton apple = new JButton("<HTML><FONT color=\\\"#000099\\\"><U>"+sApple+"</U></FONT></HTML>");
        apple.setHorizontalAlignment(SwingConstants.LEFT);
        apple.addActionListener(new LinkListener(sApple));
        apple.setBorderPainted(false);
        apple.setOpaque(false);
        apple.setBackground(Color.WHITE);
        apple.setToolTipText(sApple);
        GridBagConstraints gbc_apple = new GridBagConstraints();
        gbc_apple.anchor = GridBagConstraints.WEST;
        gbc_apple.gridx = 0;
        gbc_apple.gridy = 3;
        panel_5.add(apple, gbc_apple);

        JScrollPane scrollPane = new JScrollPane();
        panel_3.add(scrollPane);


        JTextArea description = new JTextArea(sDescription);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setWrapStyleWord(true);
        description.setToolTipText("Description");
        scrollPane.setViewportView(description);

        JLabel descriptionLabel = new JLabel("Description:");
        scrollPane.setColumnHeaderView(descriptionLabel);
    }



}

