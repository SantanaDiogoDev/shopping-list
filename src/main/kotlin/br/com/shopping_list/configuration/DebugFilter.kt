package br.com.shopping_list.configuration

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Component
class DebugFilter : GenericFilterBean() {

    private val logger = LoggerFactory.getLogger(DebugFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        logger.debug("Recebida requisição para: ${httpRequest.method} ${httpRequest.requestURI}")
        chain.doFilter(request, response)
    }
}