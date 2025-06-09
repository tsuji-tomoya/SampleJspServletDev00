import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import bean.NewProductBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewProduct extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        //ドロップダウン用の情報を格納するリストを作る
        List<NewProductBean> newProductList = new ArrayList<NewProductBean>();
        int MAX1=0;
        try {
            //データベースに接続
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
            Connection con = ds.getConnection();
            
            //sqlでカテゴリーの名前とIDを取得する
            String sql = "SELECT CATEGORYNAME,CATEGORYID FROM CATEGORIES GROUP BY CATEGORYNAME,CATEGORYID ORDER BY CATEGORYID";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            
            //取得した情報を１文ずつBeanに格納しリストに格納する
            while (rs.next()) {
                NewProductBean ProductBean = new NewProductBean();
                ProductBean.setCategoryID(rs.getInt("CategoryID"));
                ProductBean.setProductName(rs.getString("CATEGORYName"));

                newProductList.add(ProductBean);
            }
            //自動採番をするために最後の数字を取得する
            String max1 = "SELECT MAX(ProductID) AS MAX"
                    +  " FROM"
                    +  " Products";
            PreparedStatement st2 = con.prepareStatement(max1);
            ResultSet rs2 = st2.executeQuery();
            NewProductBean ProductBean2 = new NewProductBean();
            
            //自動採番用の最後の値を取得
            if(rs2.next()){
                ProductBean2.setMax(rs2.getInt("MAX"));
            }
            //最大の値から1をプラスする。
            MAX1=ProductBean2.getMax() + 1;
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //最大の値とカテゴリーリストをリクエストに保存する
        request.setAttribute("MAX",MAX1);
        request.setAttribute("NPlist", newProductList);
        
        //jspに送信、画面遷移
        RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
        rd.forward(request, response);
    }
    
    
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        
        response.setContentType("text/plain; charset=UTF-8");
        int MAX1=0;
        //送信ボタンで送られてきた情報を取得している。
        int productID = Integer.parseInt(request.getParameter("max"));
        String productName = request.getParameter("product_name");
        String  productCode = request.getParameter("item_id");
        String  productPrice = request.getParameter("price");
        String category = request.getParameter("category");
        
        //ドロップダウンリストに使う処理をもう一度書いています
        List<NewProductBean> newProductList = new ArrayList<NewProductBean>();
        
        try {
            //データベースの接続
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
            Connection con = ds.getConnection();
            
            //SQL文を作り実行する
            String sql1 = "SELECT CATEGORYNAME,CATEGORYID FROM CATEGORIES GROUP BY CATEGORYNAME,CATEGORYID ORDER BY CATEGORYID";
            PreparedStatement st1 = con.prepareStatement(sql1);
            ResultSet rs = st1.executeQuery();

            //Beanのセッターに一行ずつ情報を格納する。その後、リストに格納する
            while (rs.next()) {
                NewProductBean ProductBean = new NewProductBean();
                ProductBean.setCategoryID(rs.getInt("CategoryID"));
                ProductBean.setProductName(rs.getString("CATEGORYName"));

                newProductList.add(ProductBean);
            }
            
            st1.close();
            con.close();
        }catch(Exception e) {
            e.printStackTrace();
        }//ドロップダウンリストの処理はここまで
        
        //送信された値をリクエストに保存
        request.setAttribute("MAX",productID);
        request.setAttribute("NPlist", newProductList);
        request.setAttribute("code",productCode);
        request.setAttribute("name",productName);
        request.setAttribute("Price",productPrice);
        request.setAttribute("ID",category);
        
        //productCodeの入力チェック
        if (productCode != null && !(productCode.isEmpty())) {
            //numメソッドは自分で作ったメソッドです。try・catchで中の文字がintに変換できるのかを判定しています。
            if(!(num(productCode))) {
                request.setAttribute("error1","数字のみを入力してください");
                RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
                rd.forward(request, response);
                return;
            }
        }else {
            request.setAttribute("error2", "商品コードを入力してください");
            RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
            rd.forward(request, response);
            return;
        }

        //productNameの入力チェック
        if (productName != null && !(productName.isEmpty())) {
        }else {
            request.setAttribute("error", "名前を入力してください");
            RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
            rd.forward(request, response);
            return;
        }

        //Priceの入力チェック
        if ((productPrice != null) && !(productPrice.isEmpty())) {
            if(!num(productPrice)) {
                request.setAttribute("error3","数字のみを入力してください");
                RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
                rd.forward(request, response);
                return;
            }
                }else {
                request.setAttribute("error4", "値段を入力してしてください");
                RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
                rd.forward(request, response);
                return;
        }
        
        //この文章でテーブルを作る
        String sql = "INSERT INTO"
                + " PRODUCTS(PRODUCTID,PRODUCTCODE,PRODUCTNAME,PRICE,CATEGORYID)"
                + " VALUES(?,?,?,?,?)";
                 
        try {
        //データベースに接続
        InitialContext ic = new InitialContext();
        DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
        Connection con = ds.getConnection();
        
        //int型に変える
        int pc=Integer.parseInt(productCode);
        int pp = Integer.parseInt(productPrice);
        int cg = Integer.parseInt(category);
        
        //sql文のVALUESに数値を入れる
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, productID);
        st.setInt(2,pc);
        st.setString(3,productName);
        st.setInt(4,pp);
        st.setInt(5,cg); 
        //sqlを実行
        st.executeUpdate();
        
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        //登録完了をリクエストにセットし、画面遷移を行う
        request.setAttribute("message","登録完了");
        RequestDispatcher rd = request.getRequestDispatcher("newProduct.jsp");
        rd.forward(request, response);


    }
    
    //int型に変更ができるのかを判定するメソッド
    public boolean num(String n) {
        try {
            Integer.parseInt(n);
        }catch(Exception e) {
            e.printStackTrace();
            
            return false;
        }
        return true;
    }
    
}