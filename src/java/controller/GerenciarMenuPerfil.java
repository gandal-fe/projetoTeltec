
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import model.Perfil;
import model.PerfilDAO;


@WebServlet(name = "GerenciarMenuPerfil", 
        urlPatterns = {"/gerenciarMenuPerfil"})
public class GerenciarMenuPerfil extends HttpServlet {

  

  
    @Override
    protected void doGet(HttpServletRequest request, 
        HttpServletResponse response)
        throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=utf-8");
        String acao = request.getParameter("acao");
        String idPerfil = request.getParameter("idPerfil");
        String mensagem = "";
        
        Perfil p = new Perfil();
        PerfilDAO  pdao = new PerfilDAO();
        
        try {
            if(acao.equals("vincular")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                     p = pdao.getCarregarPorId(Integer.parseInt(idPerfil));
                    if(p.getIdPerfil() > 0){
                        RequestDispatcher dispatcher =
                            getServletContext().
                                getRequestDispatcher("/cadastrarMenuPerfil.jsp");
                        request.setAttribute("perfilv", p);
                        dispatcher.forward(request, response);
                    }else{
                    mensagem = "Perfil não encontrado na base de dados!";
                    }
                }else{
                    mensagem = "Acesso não Autorizado!";
                }
               
            
            }else if(acao.equals("desvincular")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                    String idMenu = request.getParameter("idMenu");
                    if(idMenu.equals("") || idMenu.isEmpty()){
                        request.setAttribute("msg", "Selecione um menu!");
                        despacharRequisicao(request, response);
                    }else{
                        if(pdao.desvincular(
                            Integer.parseInt(idMenu), Integer.parseInt(idPerfil))){
                            mensagem = "Menu desvinculado com sucesso!";
                        }else{
                            mensagem = "Falha ao desvincular o menu!";
                        }
                    } 
                }else{
                    mensagem = "Acesso não Autorizado";
                }
               
                
            
            }else{
                response.sendRedirect("index.jsp");
            }
            
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
            e.printStackTrace();
        }
        
        out.println(
            "<script type='text/javascript'>" +
            "alert('" + mensagem + "');" +
            "location.href='gerenciarMenuPerfil?acao=vincular&idPerfil="+idPerfil+"';" +
            "</script>"
        );
        
        
        
    }

    
    @Override
    protected void doPost(HttpServletRequest request, 
        HttpServletResponse response)
        throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        String idMenu = request.getParameter("idMenu");
        String idPerfil = request.getParameter("idPerfil");
        
        String mensagem = "";
        
        try {
            PerfilDAO pdao = new PerfilDAO();
            if(idMenu.equals("") || idMenu.isEmpty()){
                request.setAttribute("msg", "Escolha um menu!");
                despacharRequisicao(request, response);
            }else{
                if(pdao.vincular(Integer.parseInt(idMenu), 
                        Integer.parseInt(idPerfil))){
                    mensagem = "Menu Vinculado com sucesso!";
                }else{
                    mensagem = "Falha ao vincular o menu!";
                }
                
            
            }
            
        } catch (SQLException e) {
            mensagem = "Erro: " + e.getMessage();
            e.printStackTrace();
        }
        
        out.println(
            "<script type='text/javascript'>"+
            "alert('" + mensagem + "');" +
            "location.href='gerenciarMenuPerfil?acao=vincular&idPerfil="+idPerfil+"';"+
            "</script>");
    }
    
    private void despacharRequisicao(HttpServletRequest request, 
        HttpServletResponse response)
        throws ServletException, IOException{
        getServletContext().
                getRequestDispatcher("/cadastrarMenuPerfil.jsp").
                    forward(request, response);
      
    }

 

}
