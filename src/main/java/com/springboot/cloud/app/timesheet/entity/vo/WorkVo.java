package com.springboot.cloud.app.timesheet.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.cloud.app.timesheet.entity.po.Member;
import com.springboot.cloud.common.core.entity.vo.BaseVo;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.catalina.authenticator.AuthenticatorBase;
import org.apache.catalina.connector.CoyoteAdapter;
import org.apache.catalina.core.ApplicationFilterChain;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.coyote.AbstractProcessorLight;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.http11.Http11Processor;
import org.apache.tomcat.util.net.NioEndpoint;
import org.apache.tomcat.util.net.SocketProcessorBase;
import org.apache.tomcat.util.threads.TaskThread;
import org.apache.tomcat.websocket.server.WsFilter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import javax.servlet.http.HttpServlet;

/**
 * 工作记录表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkVo extends BaseVo  {

	@ApiModelProperty(value = "工作记录表ID",example = "1")
	Long id;

	@ApiModelProperty(value = "项目ID",example = "1")
	Long pId;

	@ApiModelProperty(value = "项目名称",example = "商城项目")
	String projectName;

	@ApiModelProperty(value = "用户ID",example = "1")
	Long uId;

	@ApiModelProperty(value = "用户名称",example = "明哥")
	String name;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	@ApiModelProperty(value = "工作日期",example = "2020-11-11")
	Date workDate;

	@ApiModelProperty(value = "工作时长",example = "5")
	BigDecimal hourTime;

	@ApiModelProperty(value = "工作描述",example = "工作描述")
	String description;

}
