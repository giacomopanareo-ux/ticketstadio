package it.unife.ticketstadio.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component @RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
        String auth=req.getHeader("Authorization");
        if(auth==null||!auth.startsWith("Bearer ")){chain.doFilter(req,res);return;}
        String token=auth.substring(7);
        String username=jwtUtil.extractUsername(token);
        if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails user=userDetailsService.loadUserByUsername(username);
            if(jwtUtil.isValid(token,user)){
                var a=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                a.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(a);
            }
        }
        chain.doFilter(req,res);
    }
}
