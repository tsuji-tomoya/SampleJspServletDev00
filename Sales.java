import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import bean.EmployeeBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Sales extends HttpServlet{
    
    private static final long serialVersionUID = 1L;
    public void doGet (HttpServletRequest request,HttpServletResponse response )
            throws ServletException, IOException{
        
        response.setContentType("text/plain; charset=UTF-8");
        
        List<EmployeeBean>employeeList=new ArrayList<EmployeeBean>();
        
        try {
            //オブジェクトを使うための箱を用意する。
            InitialContext ic = new InitialContext();
            //データベースに接続をする。名前でリソースを探す。
            DataSource ds = (DataSource) ic.lookup("java:/comp/env/jdbc");
            //使える道を使えるようにする。
            Connection con = ds.getConnection();
            
            //SQLの文章を格納
            String sql = "SELECT A.EMPLOYEENAME AS 従業員名,SUM(COALESCE(B.Quantity, 0) * COALESCE(C.Price, 0)) AS 売上金額 FROM Employees A LEFT JOIN Sales B ON A.EmployeeID = B.EmployeeID LEFT JOIN Products C ON B.ProductID=C.ProductID  GROUP BY A.EMPLOYEENAME,A.EmployeeID ORDER BY A.EmployeeID";
            //SQLの形をあらかじめ決めておく。
            PreparedStatement st = con.prepareStatement(sql);
            //データを取得
            ResultSet rs = st.executeQuery();
            //１レコードずつEmployeeBeanにセットする。
            while(rs.next()) {
                EmployeeBean MEN = new EmployeeBean();
                MEN.setEmployeeName(rs.getString("従業員名"));
                MEN.setSales_amount(rs.getInt("売上金額"));
                //リストに格納
                employeeList.add(MEN);
            }
            //データを閉じる
            st.close();
            con.close();
            //エラーをキャッチする。
        }catch(Exception e) {
            e.printStackTrace();
        }
        //requestに保存する。キーをempList2にemployeeListをセットする。
        request.setAttribute("empList2", employeeList);
        
        //リクエストの中身をJSPに処理を渡すための窓口を作っている。
        RequestDispatcher rd = request.getRequestDispatcher("sales.jsp");
        //JSPに処理を任せる。
        rd.forward(request, response);
   }
    
    
}
