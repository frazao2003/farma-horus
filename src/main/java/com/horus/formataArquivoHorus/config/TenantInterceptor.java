package com.horus.formataArquivoHorus.config;

import com.horus.formataArquivoHorus.config.CurrentTenantIdentifierResolverImpl;
import com.horus.formataArquivoHorus.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URLDecoder;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Pattern TENANT_PATTERN = Pattern.compile("^/([0-9a-zA-Z_-]+)/.*$");

    @Autowired
    private Utils utils;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        // Verifica se o caminho é a URL de erro
        if (path.equals("/error")) {
            System.out.println("Requisição para a página de erro, ignorando o processamento de tenant.");
            return true;
        }

        // Decodifica a URL para tratar caracteres especiais como "%0A"
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error decoding URL.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return false;
        }

        // Divide o caminho da URL com base na "/"
        String[] pathParts = path.split("/");


        // Pega o tenant, que está na segunda posição após o "/"
        String tenant = pathParts[2];

        // Verifica se o tenant contém apenas números
        if (utils.containsOnlyNumbers(tenant)) {
            CurrentTenantIdentifierResolverImpl.setTenant(tenant);
        } else {
            System.out.println("URL não corresponde ao padrão de tenant: " + tenant); // Log para depuração
            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid tenant in URL. Ensure that the tenant is correctly specified in the path.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        return true;  // Permite a continuação da requisição se o tenant for válido
    }





    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Limpa o tenant após a execução da requisição
        CurrentTenantIdentifierResolverImpl.clear();
    }
}
