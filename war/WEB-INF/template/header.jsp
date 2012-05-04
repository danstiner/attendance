<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<div class="skip"><a accesskey="2" href="#container">Skip Navigation</a></div>
	<div class="hwrapper" id="header">
		<div id="top-strip">
			<div class="grids-24">
				<div class="grid-8">
					<ul class="left">
						<li><a href="http://cymail.iastate.edu/">CyMail</a></li>
						<li><a href="http://exchange.iastate.edu/">Outlook</a></li>
						<li><a href="http://webct.its.iastate.edu/">WebCT</a></li>
						<li><a href="http://bb.its.iastate.edu/">Blackboard</a></li>
						<li class="last"><a href="http://accessplus.iastate.edu/">AccessPlus</a></li>
					</ul>
				</div>
				<div class="grid-16">
					<ul class="right">
						<li class="idx"><a href="http://www.iastate.edu/index/A/">A</a></li><li class="idx"><a href="http://www.iastate.edu/index/B/">B</a></li><li class="idx"><a href="http://www.iastate.edu/index/C/">C</a></li><li class="idx"><a href="http://www.iastate.edu/index/D/">D</a></li><li class="idx"><a href="http://www.iastate.edu/index/E/">E</a></li><li class="idx"><a href="http://www.iastate.edu/index/F/">F</a></li><li class="idx"><a href="http://www.iastate.edu/index/G/">G</a></li><li class="idx"><a href="http://www.iastate.edu/index/H/">H</a></li><li class="idx"><a href="http://www.iastate.edu/index/I/">I</a></li><li class="idx"><a href="http://www.iastate.edu/index/J/">J</a></li><li class="idx"><a href="http://www.iastate.edu/index/K/">K</a></li><li class="idx"><a href="http://www.iastate.edu/index/L/">L</a></li><li class="idx"><a href="http://www.iastate.edu/index/M/">M</a></li><li class="idx"><a href="http://www.iastate.edu/index/N/">N</a></li><li class="idx"><a href="http://www.iastate.edu/index/O/">O</a></li><li class="idx"><a href="http://www.iastate.edu/index/P/">P</a></li><li class="idx"><a href="http://www.iastate.edu/index/Q/">Q</a></li><li class="idx"><a href="http://www.iastate.edu/index/R/">R</a></li><li class="idx"><a href="http://www.iastate.edu/index/S/">S</a></li><li class="idx"><a href="http://www.iastate.edu/index/T/">T</a></li><li class="idx"><a href="http://www.iastate.edu/index/U/">U</a></li><li class="idx"><a href="http://www.iastate.edu/index/V/">V</a></li><li class="idx"><a href="http://www.iastate.edu/index/W/">W</a></li><li class="idx"><a href="http://www.iastate.edu/index/X/">X</a></li><li class="idx"><a href="http://www.iastate.edu/index/Y/">Y</a></li><li class="idx"><a href="http://www.iastate.edu/index/Z/">Z</a></li>
						<li class="border first"><a href="http://info.iastate.edu/">Directory</a></li>
						<li class="border"><a href="http://www.fpm.iastate.edu/maps/">Maps</a></li>
						<li class="border last"><a href="http://www.iastate.edu/contact/">Contact Us</a></li>
					</ul>
				</div>
			</div>
		</div>
		<div id="ribbon">
			<div class="grids-24">
				<div class="grid-16">
					<h1 class="nameplate">
						<a accesskey="1" href="http://www.iastate.edu/">
							<img alt="Iowa State University" src="/img/sprite.png"/>
						</a>
					</h1>
				</div>
				<div class="grid-8">
					<div class="userbox" id="userbox">
						<c:choose>
							<c:when test="${auth.user.type.student}">
								<a href="/student"><b><c:out value="${auth.user.name}" /></b></a>
								&nbsp;
								&middot;
								&nbsp;
								<a href="/auth/logout">
									<span style="text-size:small;color:lightgrey">Logout</span>
								</a>
							</c:when>
							<c:when test="${auth.user.type.ta}">
								<a href="/ta"><b><c:out value="${auth.user.name}" /></b></a>
								&nbsp;
								-
								&nbsp;
								<a href="/auth/logout">
									<span style="text-size:small;color:lightgrey">Logout</span>
								</a>
							</c:when>
							<c:when test="${auth.user.type.director}">
								<b>Director</b>
								<a href="/director"><b><c:out value="${auth.user.name}" /></b></a>
								&nbsp;
								-
								&nbsp;
								<a href="/auth/logout">
									<span style="text-size:small;color:lightgrey">Logout</span>
								</a>
							</c:when>
							<c:otherwise>
								<b>
								<a href="/auth/login" style="text-size:large;">Login</a>
								&nbsp;&middot;&nbsp;
								<a href="${auth.googleLoginURL}" style="text-size:large;">Google Login</a>
								&nbsp;&middot;&nbsp;
								</b>
								<a href="/auth/register"><span style="text-size:small;">Register</span></a>
							</c:otherwise>
						</c:choose>

					</div>
				</div>
			</div>
			<div class="grids-24">
				<div class="grid-12">
					<h2 class="site-title"><a href="/">ISU Cyclone Football "Varsity" Marching Band Attendance</a></h2>
				</div>
				<div class="grid-12">
					<h2 class="site-tagline"></h2>
				</div>
			</div>
		</div>
	</div>
	<div id="container">
		<div class="grids-24">
			<div class="grid-5 sidebar" id="left-sidebar">
				<ul class="navigation">

					<c:if test="${auth.user.type.student}">
					<li class="selected">
						<a href="/student">Student</a>
						<ul>
							<li class="first ${pagetemplate.jspath=='/student/attendance'?'selected':''}"><a href="/student/attendance">Attendance</a></li>
							<li class="${pagetemplate.jspath=='/student/forms'?'selected':''}">
								<a href="/student/forms">Forms</a>
							</li>
							<li class="${pagetemplate.jspath=='/student/messages'?'selected':''}"><a href="/student/messages">Messages</a></li>
							<li class="${pagetemplate.jspath=='/student/info'?'selected':''}"><a href="/student/info">Edit Info</a></li>
						</ul>
					</c:if>
					
					<c:if test="${auth.user.type.ta}">
					<li class="selected">
						<a href="/about/">Students</a>
						<ul>
							<li><a href="/contact/">Contact Us</a></li>
						</ul>
					</li>
					</c:if>
					
					<c:if test="${auth.user.type.director}">
					<li class="selected">
						<a href="/about/">Students</a>
						<ul>
							<li><a href="/contact/">Contact Us</a></li>
						</ul>
					</li>
					</c:if>
				</ul>
			</div>
			<div class="grid-14" id="content">
				<div class="gutter">