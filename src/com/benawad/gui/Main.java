package com.benawad.gui;

import com.benawad.DownloadRunner;
import com.benawad.database.BookDatabaseHelper;
import com.benawad.models.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Main extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private JPanel panel;
    private JButton downloadBooksButton;
    private JRadioButton audiobookButton;
    private JRadioButton ebooksButton;
    private JList list;
    private JSplitPane splitPane;


    private BookDatabaseHelper bookDatabaseHelper;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main frame = new Main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
/**
     * Create the frame.
     */
    public Main() {

        setTitle("Find Ferriss Books");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        splitPane = new JSplitPane();
        contentPane.add(splitPane, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        splitPane.setRightComponent(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.CENTER);
        contentPane.add(panel, BorderLayout.NORTH);

        audiobookButton = new JRadioButton("Audiobooks");
        panel.add(audiobookButton);
        audiobookButton.setSelected(true);

        ebooksButton = new JRadioButton("eBooks");
        panel.add(ebooksButton);
        ebooksButton.setSelected(true);

        downloadBooksButton = new JButton("Download Books");
        contentPane.add(downloadBooksButton, BorderLayout.SOUTH);
        downloadBooksButton.addActionListener(new DownloadListener());

        java.util.List<Book> books = null;
        // fill jtable with books
        try {
            bookDatabaseHelper = new BookDatabaseHelper();
            books = bookDatabaseHelper.getAllBooks();
            BookTableModel model = new BookTableModel(books);
            table.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "You need to download the books", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // fill jlist with categories
        list = new JList();
        JScrollPane listScrollPane = new JScrollPane();
        splitPane.setLeftComponent(listScrollPane);
        listScrollPane.setViewportView(list);

        if(!books.isEmpty()) {
            java.util.List<String> categories = getAllCategories(books);
            DefaultListModel listModel = new DefaultListModel();
            for (String cat : categories) {
                listModel.addElement(cat);
            }
            list.setModel(listModel);
        }
    }

    private List<String> getAllCategories(List<Book> books) {
        List<String> cats = new ArrayList<>();
        for(Book book : books){
            cats.addAll(book.getGenres());
        }
        Set<String> temp = new HashSet<>();
        temp.addAll(cats);
        cats.clear();
        cats.addAll(temp);
        return cats;
    }

    class DownloadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("downloading...");

            Runnable threadJob = new DownloadRunner();
            Thread downloadThread = new Thread(threadJob);
            downloadThread.start();
        }
    }
}


//
//    class LinkListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            try {
//go to this url in the user's default browser
//                URL url = new URL("");
//
//                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
//                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
//                    desktop.browse(url.toURI());
//                }
//            } catch (URISyntaxException | IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
