
import javax.lang.model.util.ElementScanner14;
import javax.swing.*;
import java.sql.*;
import java.util.Properties;
import java.awt.event.*;
import java.awt.*;

public class mainframe extends JFrame implements ActionListener
{
    //private JFrame mainframe;
    private JScrollPane scrollPane;
    private JPanel mainPanel;
    private JRadioButton ins;
    private JRadioButton upd;
    private JRadioButton del;
    private JRadioButton view;
    private JTextField[] jtf;
    private JComboBox<String> tables;
    private JTextArea textArea;
    private JPanel updatPanel;
    private JPanel insertPanel;
    private JPanel deletPanel;
    private JPanel selectPanel;
    private JScrollPane scrol;
    private int i,size;
    private String tablename;
    public mainframe()
    {
        setTitle("Twitter Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        mainPanel = new JPanel();
        mainPanel.setBounds(0,0,600,700);
        mainPanel.setLayout(null);
        add(mainPanel);

        createMenuBar();
        
        selectPanel = new JPanel(); 
        selectPanel.setBounds(0,0,600,300);
        selectPanel.setLayout(null);
        // System.out.println("hello1");
        JLabel msgop = new JLabel("Select the Table which you want ");
        msgop.setBounds(50, 50, 300, 25);
        tables = new JComboBox<String>();
        tables.setBounds(50, 80, 200, 25);
        tables.addItem("users");
        tables.addItem("authentication");
        tables.addItem("tweet");
        tables.addItem("retweets");
        JLabel oprop = new JLabel("Select the operation you want to perform ");

        oprop.setBounds(50, 120, 300, 25);
        ins = new JRadioButton("INSERT", true);
        ins.setBounds(50, 150, 100, 25);
        upd = new JRadioButton("UPDATE", false);
        upd.setBounds(150, 150, 100, 25);
        del = new JRadioButton("DELETE", false);
        del.setBounds(250, 150, 100, 25);
        view = new JRadioButton("VIEW",false);
        view.setBounds(350,150, 100, 25);

        ButtonGroup bg = new ButtonGroup();
        bg.add(ins);
        bg.add(upd);
        bg.add(del);
        bg.add(view);
        selectPanel.add(msgop);
        selectPanel.add(tables);
        selectPanel.add(oprop);
        selectPanel.add(ins);
        selectPanel.add(upd);
        selectPanel.add(del);
        selectPanel.add(view);
        
        JButton submit = new JButton("SUBMIT");
        submit.setBounds(50, 100, 100, 25);
        submit.addActionListener(this);
        selectPanel.add(submit);

        insertPanel = new JPanel();
        insertPanel.setBounds(0,0,700,600);
        insertPanel.setLayout(null);
        insertPanel.setVisible(false);

        deletPanel = new JPanel();
        deletPanel.setBounds(0,0,700,600);
        deletPanel.setLayout(null);
        deletPanel.setVisible(false);

        updatPanel = new JPanel();
        updatPanel.setBounds(0,0,700,600);
        updatPanel.setLayout(null);
        updatPanel.setVisible(false);
        
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(50, 450, 500, 100);
        mainPanel.add(scrollPane);

        mainPanel.add(selectPanel);
        mainPanel.add(insertPanel);
        mainPanel.add(deletPanel);
        mainPanel.add(updatPanel);
        //selectPanel.setVisible(true);
        //setBounds(100, 100, 1000, 1000);
        //scrol = new JScrollPane(mainPanel);
        //add(scrol);
        setPreferredSize(new Dimension(700,700));
        
        pack();
        setVisible(true);
    }
  
    public void actionPerformed(ActionEvent ae)
    {
        tablename =(String)tables.getSelectedItem();
        if(ins.isSelected())
        {
            insertvalues(tablename);
        }else if(del.isSelected())
        {
            delete(tablename);
        }else if(upd.isSelected())
        {
            update(tablename);
        }else if(view.isSelected())
        {
            viewtable(tablename);
        }
    }

    public void viewtable(String tablename)
    {
        textArea.setText("");
        try{
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","twitterdb","root");
            Statement stmt = con.createStatement();
            String qry = "Select * from "+tablename;
            ResultSet rs = stmt.executeQuery(qry);
            ResultSetMetaData rsm = rs.getMetaData();
            String s = "";
            for(int j = 1;j<= rsm.getColumnCount();j++)
            {
                s+= rsm.getColumnName(j);
                s+= "   ";
            }
            textArea.append(s+"\n");
            s= "";
            while(rs.next())
            {
                for(int j = 1;j<=rsm.getColumnCount();j++)
                {
                    s+= rs.getString(rsm.getColumnName(j));
                    s+= "     ";
                }
                textArea.append(s+"\n");
                s = "";
            }
        }catch(Exception ex)
        {
            textArea.append("couldnt display the table");
        }
    }

    private void insertvalues(String tablename)
    {
        deletPanel.removeAll();
        deletPanel.revalidate();
        deletPanel.repaint();
        updatPanel.removeAll();
        updatPanel.revalidate();
        updatPanel.repaint();
        insertPanel.removeAll();
        insertPanel.revalidate();
        insertPanel.repaint();
        try{
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","twitterdb","root");
            Statement stmt = con.createStatement();
            String qry = "Select * from "+tablename;
            ResultSet rs = stmt.executeQuery(qry);
            ResultSetMetaData rsm = rs.getMetaData();
            jtf = new JTextField[rsm.getColumnCount()];
            JLabel[] jl = new JLabel[rsm.getColumnCount()];
            JLabel title = new JLabel(" INSERT ");
            deletPanel.add(title);
            size = rsm.getColumnCount();
            int x = 50; // Initial x-coordinate
            int y = 50; // Initial y-coordinate
            int labelWidth = 200; // Width of the label
            int textFieldWidth = 150; // Width of the text field
            int height = 25; // Height of each component
            int spacing = 20; // Vertical spacing between components
            for(int i = 0;i<rsm.getColumnCount();i++)
            {
                jl[i] = new JLabel(rsm.getColumnName(i+1));
                jtf[i] = new JTextField();
                jl[i].setBounds(x,y,labelWidth,height);
                jtf[i].setBounds(x + labelWidth + spacing, y, textFieldWidth, height);
                insertPanel.add(jl[i]);
                insertPanel.add(jtf[i]);
                y += height+spacing;
            }
            JButton sub = new JButton("SUBMIT", null);
            sub.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    try{
                        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","twitterdb","root");
                        Statement stmt = con.createStatement();
                        String qry = "insert into "+tablename+" values(";
                        //System.out.println(qry);
                        if(size == 2)
                        {
                            qry += jtf[0].getText()+",";
                            qry += "'"+jtf[1].getText()+"'";
                        }else if(size == 4)
                        {
                            qry += "'"+jtf[0].getText()+"'";
                            qry +=","+ "'"+jtf[1].getText()+"'";
                            qry += ","+"'"+jtf[2].getText()+"'";
                            qry += ","+jtf[3].getText();
                        }
                        else if(size == 7)
                        {
                            qry += "'"+jtf[0].getText()+"'";
                            qry += ","+"'"+jtf[1].getText()+"'";
                            qry += ","+"'"+jtf[2].getText()+"'";
                            qry += ","+"'"+jtf[3].getText()+"'";
                            qry += ","+"'"+jtf[4].getText()+"'";
                            qry += ","+jtf[5].getText();
                            qry += ","+jtf[6].getText();
                            
                        }
                        else if(size == 5)
                        {
                            qry += jtf[0].getText()+",";
                            qry += "'"+jtf[1].getText()+"'";
                            qry += ","+"'"+jtf[2].getText()+"'";
                            qry += ","+jtf[3].getText()+",";
                            qry += jtf[4].getText();

                            
                        }
                        qry+=")";
                        System.out.println(qry);
                        stmt.executeQuery(qry);
                        textArea.setText("");
                        textArea.append("a row is inserted into "+tablename);
                    }
                    catch(Exception ex)
                    {
                        textArea.setText("");
                        textArea.append("couldnt perform insert");
                        ex.printStackTrace();
                    }
                }
            }
            );
            sub.setBounds(x, y, textFieldWidth, height);
            insertPanel.add(sub);
            //f.setLayout(new GridLayout(size+1, 2, 3, 1));
            insertPanel.setSize(700,500);
            //System.out.println("hello");
            deletPanel.setVisible(false);
            selectPanel.setVisible(false);
            insertPanel.setVisible(true);
        }catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    
    private void update(String tablename)
    {
        deletPanel.removeAll();
        deletPanel.revalidate();
        deletPanel.repaint();
        insertPanel.removeAll();
        insertPanel.revalidate();
        insertPanel.repaint();
        updatPanel.removeAll();
        updatPanel.revalidate();
        updatPanel.repaint();
        try{
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","twitterdb","root");
            Statement stmt = con.createStatement();
            String qry = "Select * from "+tablename;
            ResultSet rs = stmt.executeQuery(qry);
            ResultSetMetaData rsm = rs.getMetaData();
            jtf = new JTextField[rsm.getColumnCount()];
            JLabel title = new JLabel(" DELETE ");
            updatPanel.add(title);
            JLabel[] jl = new JLabel[rsm.getColumnCount()];
            size = rsm.getColumnCount();
            int x = 50; // Initial x-coordinate
            int y = 50; // Initial y-coordinate
            int labelWidth = 200; // Width of the label
            int textFieldWidth = 150; // Width of the text field
            int height = 25; // Height of each component
            int spacing = 30; // Vertical spacing between components
            for(int i = 0;i<rsm.getColumnCount();i++)
            {
                jl[i] = new JLabel(rsm.getColumnName(i+1));
                jtf[i] = new JTextField();
                jl[i].setBounds(x,y,labelWidth,height);
                jtf[i].setBounds(x + labelWidth + spacing, y, textFieldWidth, height);
                updatPanel.add(jl[i]);
                updatPanel.add(jtf[i]);
                y += height+spacing;
            }
            JButton sub = new JButton("SUBMIT", null);
            sub.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    try{
                        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","twitterdb","root");
                        Statement stmt = con.createStatement();
                        String qry = "update "+tablename+" set ";
                        if(size == 2)
                        {
                            if(jtf[0].getText().length() == 0)
                            {
                                System.out.println("Cannot append");
                            }else
                            {
                                qry+= rsm.getColumnName(2)+" = '"+jtf[1].getText()+"' where "+rsm.getColumnName(1)+" = "+jtf[0].getText();
                            }
                        }else if(size == 4)
                        {
                          qry += rsm.getColumnName(4) + " = " + jtf[3].getText() + ", " + rsm.getColumnName(3) + " = '" + jtf[2].getText() + "', " + rsm.getColumnName(2) + " = '" + jtf[1].getText() + "' where " + rsm.getColumnName(1) + " = '" + jtf[0].getText() + "'";
 
                        }
                        else if(size == 5)
                        {
                            if(jtf[0].getText().length() == 0)
                            {
                                System.out.println("Cannot append");
                            }
                            else{
                                qry +=rsm.getColumnName(5) + " = " + jtf[4].getText() + ","+rsm.getColumnName(4) + " = " + jtf[3].getText() + ", " + rsm.getColumnName(3) + " = '" + jtf[2].getText() + "', " + rsm.getColumnName(2) + " = '" + jtf[1].getText() + "' where " + rsm.getColumnName(1) + " = '" + jtf[0].getText() + "'";
                            }

                        }
                         else if(size == 7)
                        {
                            if(jtf[0].getText().length() == 0)
                            {
                                System.out.println("Cannot append");
                            }
                            else{
                                qry +=rsm.getColumnName(7) + " = " + jtf[6].getText()+ "," +rsm.getColumnName(6) + " = " + jtf[5].getText() + ","+rsm.getColumnName(5) + " = '" + jtf[4].getText() + "',"+rsm.getColumnName(4) + " = '" + jtf[3].getText() + "'," + rsm.getColumnName(3) + " = '" + jtf[2].getText() + "', " + rsm.getColumnName(2) + " = '" + jtf[1].getText() + "' where " + rsm.getColumnName(1) + " = '" + jtf[0].getText() + "'";
                            }

                        }
                        System.out.println(qry);
                        stmt.executeQuery(qry);
                        textArea.setText("");
                        textArea.append(tablename+" is updated ");
                    }
                    catch(Exception ex)
                    {
                        System.out.println(ex);
                    }
                }
            });
            sub.setBounds(x, y, textFieldWidth, height);
            updatPanel.add(sub);
            //f.setLayout(new GridLayout(size+1, 2, 3, 1));
            updatPanel.setSize(700,500);
            //System.out.println("hello");
            insertPanel.setVisible(false);
            selectPanel.setVisible(false);
            updatPanel.setVisible(true);
            deletPanel.setVisible(false);
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }


    private void delete(String tablename)
    {
        insertPanel.removeAll();
        insertPanel.revalidate();
        insertPanel.repaint();
        updatPanel.removeAll();
        updatPanel.revalidate();
        updatPanel.repaint();
        deletPanel.removeAll();
        deletPanel.revalidate();
        deletPanel.repaint();
        try{
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","twitterdb","root");
            Statement stmt = con.createStatement();
            String qry = "Select * from "+tablename;
            ResultSet rs = stmt.executeQuery(qry);
            ResultSetMetaData rsm = rs.getMetaData();
            jtf = new JTextField[rsm.getColumnCount()];
            JLabel title = new JLabel(" DELETE ");
            deletPanel.add(title);
            JLabel[] jl = new JLabel[rsm.getColumnCount()];
            size = rsm.getColumnCount();
            int x = 50; // Initial x-coordinate
            int y = 50; // Initial y-coordinate
            int labelWidth = 200; // Width of the label
            int textFieldWidth = 150; // Width of the text field
            int height = 25; // Height of each component
            int spacing = 30; // Vertical spacing between components
            for(int i = 0;i<rsm.getColumnCount();i++)
            {
                jl[i] = new JLabel(rsm.getColumnName(i+1));
                jtf[i] = new JTextField();
                jl[i].setBounds(x,y,labelWidth,height);
                jtf[i].setBounds(x + labelWidth + spacing, y, textFieldWidth, height);
                deletPanel.add(jl[i]);
                deletPanel.add(jtf[i]);
                y += height+spacing;
            }
            JButton sub = new JButton("SUBMIT", null);
            sub.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    try{
                        Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","twitterdb","root");
                        Statement stmt = con.createStatement();
                        String qry = "Delete from "+tablename+" where ";
                        if(size == 2)
                        {
                            if(jtf[1].getText().length() == 0)
                            {
                                qry += rsm.getColumnName(1)+" = ";
                                qry += jtf[0].getText();
                            }else if(jtf[0].getText().length() == 0)
                            {
                                qry += rsm.getColumnName(2)+" = ";
                                qry += "'"+jtf[1].getText()+"'";
                            }else if(jtf[0].getText().length() != 0 && jtf[1].getText().length() != 0)
                            {
                                qry += rsm.getColumnName(1)+" = ";
                                qry += jtf[0].getText()+" and ";
                                qry += rsm.getColumnName(2)+" = ";
                                qry += "'"+jtf[1].getText()+"'";
                            }else
                            {
                                System.out.println("no");
                            }
                        }else if(size == 3)
                        {
                            int[] arr = new int[3];
                            for(int j = 0;j<3;j++)
                            {
                                arr[j] = jtf[j].getText().length();
                            }
                            int flag = 0;
                            for(int j = 0;j<size;j++)
                            {
                                if(arr[j] != 0)
                                {
                                    if(flag == 0)
                                    {
                                        if(j == size-1)
                                        {
                                            qry += rsm.getColumnName(j+1)+" = ";
                                            qry += "'"+jtf[j].getText()+"'";
                                        }else{
                                            qry += rsm.getColumnName(j+1)+" = ";
                                            qry += jtf[j].getText(); 
                                            flag = 1;
                                        }  
                                    }else
                                    {
                                        if(j == size-1)
                                        {
                                            qry += " and "+rsm.getColumnName(j+1)+" = ";
                                            qry += "'"+jtf[j].getText()+"'";
                                        }else{
                                            qry += " and "+ rsm.getColumnName(j+1)+" = ";
                                            qry += jtf[j].getText();
                                        } 
                                    }
                                }
                            }
                        }
                        else if(size == 6)
                        {
                            for(int i = 0;i<size;i++)
                            {
                                int[] arr = new int[3];
                                for(int j = 0;j<3;j++)
                                {
                                    arr[j] = jtf[j].getText().length();
                                }
                                int flag = 0;
                                for(int j = 0;j<size;j++)
                                {
                                    if(arr[j] != 0)
                                    {
                                        if(flag == 0)
                                        {
                                            
                                            qry += rsm.getColumnName(j+1)+" = ";
                                            qry += jtf[j].getText(); 
                                            flag = 1; 
                                        }else
                                        {
                                            
                                            qry += " and "+ rsm.getColumnName(j+1)+" = ";
                                            qry += jtf[j].getText(); 
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println(qry);
                        stmt.executeQuery(qry);
                        textArea.setText("");
                        textArea.append("1 row deleted from "+tablename);
                    }
                    catch(Exception ex)
                    {
                        System.out.println(ex);
                    }
                }
            });
            sub.setBounds(x, y, textFieldWidth, height);
            deletPanel.add(sub);
            //f.setLayout(new GridLayout(size+1, 2, 3, 1));
            deletPanel.setSize(500,400);
            //System.out.println("hello");
            insertPanel.setVisible(false);
            updatPanel.setVisible(false);
            selectPanel.setVisible(false);
            deletPanel.setVisible(true);
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
    private void createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu updateTables = new JMenu("UPDATE");
        JMenu insertTables = new JMenu("INSERT");
        JMenu deleteTables = new JMenu("DELETE");
        JMenu menuop = new JMenu("MENU");
        JMenu view = new JMenu("VIEW");
        JMenuItem[] updateItems = new JMenuItem[4];
        JMenuItem[] insertItems = new JMenuItem[4];
        JMenuItem[] deleteItems = new JMenuItem[4];
        JMenuItem[] viewItems = new JMenuItem[4];
        String[] tableNames = {"users", "authentication", "tweet", "retweets"};
    
        JMenuItem menu = new JMenuItem("MENU");
        JMenuItem exit = new JMenuItem("EXIT");
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                updatPanel.setVisible(false);
                deletPanel.setVisible(false);
                insertPanel.setVisible(false);
                selectPanel.setVisible(true);
            }
        }

        );
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        menuop.add(menu);
        menuop.add(exit);
        for (int i = 0; i < 4; i++) {
            String tableName = tableNames[i];
        
            updateItems[i] = new JMenuItem(tableName);
            updateItems[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(tableName);
            }
            });
        updateTables.add(updateItems[i]);
        
        insertItems[i] = new JMenuItem(tableName);
        insertItems[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(tableName);
                insertvalues(tableName);
            }
        });
        insertTables.add(insertItems[i]);
        
        deleteItems[i] = new JMenuItem(tableName);
        deleteItems[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete(tableName);
            }
        });
        deleteTables.add(deleteItems[i]);
        viewItems[i] = new JMenuItem(tableName);
        viewItems[i].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewtable(tableName);
            }
        });
        view.add(viewItems[i]);
        }
    
        menuBar.add(menuop);
        menuBar.add(updateTables);
        menuBar.add(insertTables);
        menuBar.add(deleteTables);
        menuBar.add(view);
        setJMenuBar(menuBar);
    }
    public static void main(String[] args) {
        new mainframe();
    }
}

