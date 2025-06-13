<div class="header-area" style="background-color: pink;">
	<a href="${contextPath}"><img src="${contextPath}/resources/icon/logo2.svg"></a>
		<select id="country-select" name="country">
			<option value="kr" data-image="${contextPath}/resources/icon/south-korea_icon.png"${selectedCountry == 'kr' ? 'selected' : ''}>Korea</option>
			<option value="jp" data-image="${contextPath}/resources/icon/japan_icon.png"${selectedCountry == 'jp' ? 'selected' : ''}>Japan</option>
		</select>
</div>