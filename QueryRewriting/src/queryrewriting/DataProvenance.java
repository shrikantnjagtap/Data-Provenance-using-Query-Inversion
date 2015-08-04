/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package queryrewriting;

import java.util.regex.Pattern;

/**
 *
 * @author Shrikant
 */
public class DataProvenance extends javax.swing.JFrame {

    /**
     * Creates new form DataProvenance
     */
    public DataProvenance() {
        initComponents();
    }
    
    
    //Union Statement
    public String DataChange1(String sst)
    {
       int h;
         String hh,dd,jj ="";
         sst=sst.toUpperCase();
         sst=sst.replaceAll(";", "");
         String kk[];
         //String sst="SELECT A FROM R UNION select B from S UNION select C from T;"
         String sj[]=sst.split("UNION");
         h=sj.length;
         String smh[]=new String[h];
         String ss[][] = new String[h][h];
         String mm[]=new String[h];
         for(int l=0;l<h;l++)
         {
             kk=sj[l].split("FROM ");
           smh[l]=kk[1];
         }
     //    System.out.println("dd"+smh[1]);
         for(int i=65;i<65+h;i++)
             for(int j=65;j<65+h;j++)
             {
                 if(i==j)
                 {
                     hh=Character.toString((char)i);
                     ss[i-65][j-65]=hh;
                 }
                 else
                 {
                     ss[i-65][j-65]="NULL";
                 }
             }
     //    System.out.println(ss[1][2]);
         
         for(int m=0;m<h;m++)
         {
             mm[m]="(SELECT "+ss[m][m];
              for(int k=65;k<65+h;k++)
              {
                dd=Character.toString((char)k);
                mm[m]=mm[m]+" , "+ss[k-65][m]+" AS PROV_"+dd;     
              }
              mm[m]=mm[m]+" FROM "+smh[m];
              if(m<h-1)
              mm[m]=mm[m]+") UNION ALL";
              else
              mm[m]=mm[m]+");";
              jj=jj+mm[m];
         }
         System.out.println(jj);
         return jj;
    }
    
    //Select Join Group By statement
    public String DataChange(QueryMetaData qDetails,String str)
    {
        
        String str1=str.toUpperCase();
        str1=str1.replaceAll(";", "");
        String sbf[],sbg[],slp[],slf[];
        String sf,sf1,sp2,sf2,RA="R.A",RB="R.B",RC="S.C",B="b",provFinal,pr1,pr2,pr3,gby,cond1,cond2,grp,slt; 
        cond1=qDetails.getOnConditionPart().get(0).toString();
        cond2=qDetails.getOnConditionPart().get(1).toString();
        slt=qDetails.getSelectPart().get(0).getColumnName();
        slp=slt.split(Pattern.quote("("));
        slf=slp[1].split(Pattern.quote(")"));
        System.out.println("DSSD"+slf[0]);
        System.out.println("KDKD"+cond1+" "+cond2);
        gby=qDetails.getGroupByPart().get(0).getTableName()+"."+qDetails.getGroupByPart().get(0).getColumnName();
        grp=qDetails.getGroupByPart().get(0).getColumnName();
        sbf=str1.split("FROM");
        System.out.println("Sbf1 :"+sbf[1]);
        sbg=sbf[1].split("GROUP");
        //to get result as per timestamp
        String tsCondition=" where "+slf[0].split(Pattern.quote("."))[0]+".ts"+"<=(select ts from qtable where QID=(select max(QID) from qtable where qstr='"+ txtInputQuery.getText().replaceAll(";", "") +"'))";
        sf1=sbf[0]+", "+gby+" FROM"+sbg[0]+tsCondition+" Group "+sbg[1];
        System.out.println(sf1);
        provFinal=gby.replace(".","_");
        provFinal="PROV_"+provFinal;
        pr1=provFinal;
        sp2="SELECT "+gby;
        provFinal="";
        provFinal=slf[0].replace(".","_");
        provFinal="PROV_"+provFinal;
        pr1=provFinal;
        sp2=sp2+" , "+slf[0]+" AS "+provFinal;
        provFinal=cond1.replace(".","_");    
        provFinal="PROV_"+provFinal;
        pr2=provFinal;
        sp2=sp2+" , "+cond1+" AS "+provFinal;
        provFinal="";
        provFinal=cond2.replace(".","_");    
        provFinal="PROV_"+provFinal;
        pr3=provFinal;
        sp2=sp2+" , "+cond2+" AS "+provFinal;
        sf2=sp2+" FROM"+sbg[0];
        System.out.println(sf2);
        sf="SELECT "+qDetails.getSelectPart().get(0).getColumnAliasName()+" , "+pr1+" , "+pr2+" , "+pr3+" FROM ("+ sf1 +") AS ORIG LEFT JOIN (" + sf2 +tsCondition+ ") AS sub ON(ORIG." +grp+" = SUB."+grp+");";
        System.out.println(sf);
        System.out.println(qDetails.getSelectPart().get(0).getColumnAliasName());
        return sf;
    }
        
    //Nested Query
    public String DataChange3(QueryMetaData qDetails,String str)
    {
        String cmd="";
        //Input Query
        //base relations R and S with schemas R=(a) and S=(b). 
        //SELECT ∗ FROM R WHERE EXISTS (SELECT ∗ FROM S); 
        //Output Query
        
        //SELECT
        cmd+="SELECT ";
        //R.a , resub . prov_S_b , R.a AS prov_R_a
        String firstColumn=qDetails.getSelectPart().get(0).toString(); // +" AS ";
        cmd+=firstColumn + ", " + firstColumn + " AS prov_" + qDetails.getSelectPart().get(0).toString() + ", ";
        cmd+="resub.prov_" + qDetails.getSelectPart().get(1).toString();
        
        //FROM R, 
        cmd+=" FROM " + qDetails.getFromPart().get(0).getTableName(); // + " " + qDetails.getFromPart().get(0).getTableAliasName() + ",";
        //(SELECT S.b , S.b AS prov_S_b FROM S) AS resub ;
        cmd+=",(SELECT ";
        cmd+=qDetails.getSelectPart().get(1).toString() + ", "+ qDetails.getSelectPart().get(1).toString() +" AS prov_" + qDetails.getSelectPart().get(1).toString();
        cmd+=" FROM " + qDetails.getFromPart().get(1).getTableName()+ ") AS resub;"; // + " " + qDetails.getFromPart().get(1).getTableAliasName() + ") AS resub;";
        
        
        return cmd;
    }
    
    //Simple Where clause statement
    public String DataChange2(QueryMetaData qDetails, String sw) {
        
        String cmd="SELECT " + qDetails.getSelectPart().get(0).getColumnName() + " AS ";
        cmd+= qDetails.getSelectPart().get(0).getColumnAliasName() + ", " + qDetails.getFromPart().get(0).getTableName() + ".*," +qDetails.getFromPart().get(1).getTableName() + ".*";
        cmd+=" FROM " + qDetails.getFromPart().get(0).getTableName() + ", " + qDetails.getFromPart().get(1).getTableName();
        cmd+= " WHERE " + qDetails.getWherePart().get(0).getColumnName();
        //to get result as per timestamp
        String tsCondition=qDetails.getSelectPart().get(0).getColumnName().split(Pattern.quote("."))[0]+".ts"+"<=(select ts from qtable where QID=(select max(QID) from qtable where qstr='"+ txtInputQuery.getText().replaceAll(";", "") +"'))";
        cmd+= " AND " + tsCondition;
         
        return cmd;
    }      
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtInputQuery = new javax.swing.JTextField();
        btnExecute = new javax.swing.JButton();
        btnProvenance = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRewriteOutput = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Enter your query:");

        txtInputQuery.setBackground(new java.awt.Color(204, 255, 204));
        txtInputQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInputQueryActionPerformed(evt);
            }
        });

        btnExecute.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnExecute.setForeground(new java.awt.Color(0, 0, 102));
        btnExecute.setText("Execute Query");
        btnExecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecuteActionPerformed(evt);
            }
        });

        btnProvenance.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnProvenance.setForeground(new java.awt.Color(51, 0, 102));
        btnProvenance.setText("Provenance");
        btnProvenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProvenanceActionPerformed(evt);
            }
        });

        txtRewriteOutput.setBackground(new java.awt.Color(255, 255, 204));
        txtRewriteOutput.setColumns(20);
        txtRewriteOutput.setLineWrap(true);
        txtRewriteOutput.setRows(5);
        jScrollPane1.setViewportView(txtRewriteOutput);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Query after rewriting");

        jLabel3.setBackground(new java.awt.Color(204, 204, 255));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Data Provenance: Using Query Inversion");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtInputQuery)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 228, Short.MAX_VALUE)
                                .addComponent(btnExecute)
                                .addGap(32, 32, 32)
                                .addComponent(btnProvenance, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel2))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtInputQuery, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProvenance, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExecute, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecuteActionPerformed
        // TODO add your handling code here:
        QINVERSION qry=new QINVERSION();
        qry.updateQT(txtInputQuery.getText());
    }//GEN-LAST:event_btnExecuteActionPerformed

    private void btnProvenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProvenanceActionPerformed
        // TODO add your handling code here:
        String InputQuery=txtInputQuery.getText().toUpperCase();
        String ss;
        if(InputQuery.contains(" UNION ")){  
            ss=DataChange1(InputQuery); 
        }
        else if(InputQuery.contains(" JOIN "))
        {
            QueryParser qProcess=new QueryParser(InputQuery);
            QueryMetaData qDetails= qProcess.getQueryMetaData();
            ss=DataChange(qDetails,InputQuery);
        }
        else if (InputQuery.lastIndexOf("SELECT")>0)
        {
            QueryParser qProcess=new QueryParser(InputQuery);
            QueryMetaData qDetails= qProcess.getQueryMetaData();
            ss=DataChange3(qDetails,InputQuery);
        }
        else
        {
            QueryParser qProcess=new QueryParser(InputQuery);
            QueryMetaData qDetails= qProcess.getQueryMetaData();
            ss=DataChange2(qDetails,InputQuery);
        }
        txtRewriteOutput.setText(ss);
        System.out.println("FINAL :"+ss);
 
    }//GEN-LAST:event_btnProvenanceActionPerformed
                            
    private void txtInputQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInputQueryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInputQueryActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DataProvenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataProvenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataProvenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataProvenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataProvenance().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExecute;
    private javax.swing.JButton btnProvenance;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtInputQuery;
    private javax.swing.JTextArea txtRewriteOutput;
    // End of variables declaration//GEN-END:variables

   
}
