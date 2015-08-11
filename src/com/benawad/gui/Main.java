package com.benawad.gui;

import com.benawad.BookSorter;
import com.benawad.DownloadRunner;
import com.benawad.database.BookDatabaseCreator;
import com.benawad.database.BookDatabaseHelper;
import com.benawad.models.Book;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private JPanel panel;
    private JButton downloadBooksButton;
    private JButton clearButton;
    private JRadioButton allButton;
    private JRadioButton audiobookButton;
    private JRadioButton ebooksButton;
    private JList list;
    private JSplitPane splitPane;
    public static final String GOOGLE_API_KEY_LINK = "https://developers.google.com/api-client-library/python/guide/aaa_apikeys";
    public boolean isDownloading = false;

    public static final String TITLE = "Find Ferriss Books";

    private BookDatabaseHelper bookDatabaseHelper;
    private BookDatabaseCreator bookDatabaseCreator;

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

        // create database if not already made
        try {
            bookDatabaseCreator = new BookDatabaseCreator();
        } catch (Exception e) {
            e.printStackTrace();
            // if we can't cannot to database we can't run the program so just quit;
            if(!e.getMessage().contains("database exists")) {
                JOptionPane.showMessageDialog(null, "Cannot connect to database. Try restarting the program.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        setTitle(TITLE);
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
        table.setAutoCreateRowSorter(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    try {
                        BookDatabaseHelper helper = new BookDatabaseHelper();
                        Book book = helper.getBook(table.getValueAt(table.getSelectedRow(), 0).toString());
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    String subtitle = "";
                                    if (!book.getSubtitle().equals("No subtitle")) {
                                        subtitle = book.getSubtitle();
                                    }
                                    String authors = "Authors: " + Book.authorsToString(book.getAuthors());
                                    String pageCount = "Pages: " + book.getPageCount();
                                    String categories = "Categories: " + Book.authorsToString(book.getCategories());
                                    String formats = "Formats: ";
                                    if (book.isEbook() && book.isAudiobook()) {
                                        formats += "eBook, Audiobook";
                                    } else if (book.isEbook() && !book.isAudiobook()) {
                                        formats += "eBook";
                                    } else if (!book.isEbook() && book.isAudiobook()) {
                                        formats += "Audiobook";
                                    } else {
                                        formats += "Print";
                                    }
                                    BookFrame frame = new BookFrame(book.getTitle(), subtitle, authors, pageCount, categories, formats, book.getAmazon(), book.getGoogle(), book.getApple(), StringEscapeUtils.unescapeEcmaScript(book.getDescription()));
                                    frame.setVisible(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        scrollPane.setViewportView(table);

        panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.CENTER);
        contentPane.add(panel, BorderLayout.NORTH);

        clearButton = new JButton("Clear Selection");
        clearButton.addActionListener(new ClearSelectionListener());
        panel.add(clearButton);

        allButton = new JRadioButton("All");
        allButton.addActionListener(new RefreshScreenListener());
        allButton.setSelected(true);
        panel.add(allButton);

        audiobookButton = new JRadioButton("Audiobooks");
        audiobookButton.addActionListener(new RefreshScreenListener());
        panel.add(audiobookButton);

        ebooksButton = new JRadioButton("eBooks");
        ebooksButton.addActionListener(new RefreshScreenListener());
        panel.add(ebooksButton);

        ButtonGroup radioButtons = new ButtonGroup();

        radioButtons.add(allButton);
        radioButtons.add(audiobookButton);
        radioButtons.add(ebooksButton);

        downloadBooksButton = new JButton("Download Books");
        contentPane.add(downloadBooksButton, BorderLayout.SOUTH);
        downloadBooksButton.addActionListener(new DownloadListener());

        list = new JList();
        JScrollPane listScrollPane = new JScrollPane();
        splitPane.setLeftComponent(listScrollPane);
        listScrollPane.setViewportView(list);

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                refreshScreen();
            }
        });
        refreshScreen();
    }

    public class RefreshScreenListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            refreshScreen();
        }
    }

    public void refreshScreen() {
        try {
            bookDatabaseHelper = new BookDatabaseHelper();
            List<Book> allBooks = bookDatabaseHelper.getAllBooks();

            // fill the table
            List<Book> filteredBooks = null;
            if (list.isSelectionEmpty()) {
                filteredBooks = BookSorter.sort(
                        ebooksButton.isSelected(),
                        audiobookButton.isSelected(),
                        allButton.isSelected(),
                        allBooks);
            } else {
                filteredBooks = BookSorter.sort(
                        ebooksButton.isSelected(),
                        audiobookButton.isSelected(),
                        allButton.isSelected(),
                        (String) list.getSelectedValue(),
                        allBooks);
            }
            BookTableModel model = new BookTableModel(filteredBooks);
            table.setModel(model);

            // fill the categories list
            List<String> categories = getAllCategories(allBooks);
            // check to see if any there are any new categories
            // if so refresh the list
            // if not don't refresh it
            Set<String> listCats = new HashSet<>();
            for (int i = 0; i < list.getModel().getSize(); i++) {
                listCats.add((String) list.getModel().getElementAt(i));
            }
            // converting the lists to sets because I want to ignore order
            Set<String> catSet = new HashSet<>();
            catSet.addAll(categories);
            if (!listCats.equals(catSet)) {
                DefaultListModel listModel = new DefaultListModel();
                for (String cat : categories) {
                    listModel.addElement(cat);
                }
                list.setModel(listModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllCategories(List<Book> books) {
        List<String> cats = new ArrayList<>();
        for (Book book : books) {
            cats.addAll(book.getCategories());
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

            if (isDownloading) {
                JOptionPane.showMessageDialog(null, "Currently downloading the books", "Already Started", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JButton googleLink = new JButton("<HTML>The program uses the <FONT color=\\\"#000099\\\"><U>Google Books API</U></FONT>.<br> The download will take 15 minutes unless you click the link,<br> get an API key, enter it into the textbox below in which case it will take 3 minutes.</HTML>");
                googleLink.setHorizontalAlignment(SwingConstants.LEFT);
                googleLink.setBorderPainted(false);
                googleLink.setOpaque(false);
                googleLink.setBackground(Color.WHITE);
                googleLink.setToolTipText(GOOGLE_API_KEY_LINK);
                googleLink.addActionListener(new LinkListener(GOOGLE_API_KEY_LINK));
                JTextField apiKey = new JTextField();
                try {
                    bookDatabaseHelper = new BookDatabaseHelper();
                    apiKey.setText(bookDatabaseHelper.getApiKey());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                final JComponent[] inputs = new JComponent[]{
                        googleLink,
                        apiKey
                };
                int selectedOption = JOptionPane.showConfirmDialog(null, inputs, "Download Books", JOptionPane.OK_CANCEL_OPTION);

                if (selectedOption == JOptionPane.YES_OPTION) {
                    try {
                        bookDatabaseCreator.saveApiKey(apiKey.getText());
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    Runnable threadJob = new DownloadRunner(Main.this, bookDatabaseCreator);
                    Thread downloadThread = new Thread(threadJob);
                    downloadThread.start();
                }

            }
        }
    }

    private class ClearSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            allButton.setSelected(true);
            list.clearSelection();
            refreshScreen();
        }
    }
}