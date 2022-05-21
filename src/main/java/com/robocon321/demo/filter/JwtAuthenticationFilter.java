package com.robocon321.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.robocon321.demo.domain.CustomUserDetailsDomain;
import com.robocon321.demo.dto.user.RoleDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.service.impl.UserService;
import com.robocon321.demo.token.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                int userId = tokenProvider.getUserIdFromJWT(jwt);
                CustomUserDetailsDomain userDetails = userService.loadUserById(userId);
                UserDTO userDTO = new UserDTO();
                
                User user = userDetails.getUser();
                BeanUtils.copyProperties(user, userDTO);
                
				user.getRoles().forEach(role -> {
					RoleDTO roleDTO = new RoleDTO();
					BeanUtils.copyProperties(role, roleDTO);
					userDTO.getRoles().add(roleDTO);
				});
                
                if(userDetails != null) {
                    UsernamePasswordAuthenticationToken
                            authentication = new UsernamePasswordAuthenticationToken(userDTO, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                SecurityContextHolder.clearContext();;
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }

        filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
