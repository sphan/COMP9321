
<div id="searchForm">
	<form action="search" method="POST">
		<div id="checkin">
			Check In Date: <br> Date: <select name="startday">
				<option selected>${searchDetails.startDay}</option>
				<option>1</option>
				<option>2</option>
				<option>3</option>
				<option>4</option>
				<option>5</option>
				<option>6</option>
				<option>7</option>
				<option>8</option>
				<option>9</option>
				<option>10</option>
				<option>11</option>
				<option>12</option>
				<option>13</option>
				<option>14</option>
				<option>15</option>
				<option>16</option>
				<option>17</option>
				<option>18</option>
				<option>19</option>
				<option>20</option>
				<option>21</option>
				<option>22</option>
				<option>23</option>
				<option>24</option>
				<option>25</option>
				<option>26</option>
				<option>27</option>
				<option>28</option>
				<option>29</option>
				<option>30</option>
				<option>31</option>
			</select> Month: <select name="startmonth">
				<option selected>${searchDetails.startMonth}</option>
				<option>01</option>
				<option>02</option>
				<option>03</option>
				<option>04</option>
				<option>05</option>
				<option>06</option>
				<option>07</option>
				<option>08</option>
				<option>09</option>
				<option>10</option>
				<option>11</option>
				<option>12</option>
			</select> Year: <select name="startyear">
			<option selected>${searchDetails.startYear}</option>
				<option>2014</option>
				<option>2015</option>
				<option>2016</option>
			</select>
		</div>
		<div id="checkout">
			Check Out Date: <br> Date: <select name="endday">
				<option selected>${searchDetails.endDay}</option>
				<option>1</option>
				<option>2</option>
				<option>3</option>
				<option>4</option>
				<option>5</option>
				<option>6</option>
				<option>7</option>
				<option>8</option>
				<option>9</option>
				<option>10</option>
				<option>11</option>
				<option>12</option>
				<option>13</option>
				<option>14</option>
				<option>15</option>
				<option>16</option>
				<option>17</option>
				<option>18</option>
				<option>19</option>
				<option>20</option>
				<option>21</option>
				<option>22</option>
				<option>23</option>
				<option>24</option>
				<option>25</option>
				<option>26</option>
				<option>27</option>
				<option>28</option>
				<option>29</option>
				<option>30</option>
				<option>31</option>
			</select> Month: <select name="endmonth">
			<option selected>${searchDetails.endMonth}</option>
				<option>01</option>
				<option>02</option>
				<option>03</option>
				<option>04</option>
				<option>05</option>
				<option>06</option>
				<option>07</option>
				<option>08</option>
				<option>09</option>
				<option>10</option>
				<option>11</option>
				<option>12</option>
			</select> Year: <select name="endyear">
			<option selected>${searchDetails.endYear}</option>
				<option>2014</option>
				<option>2015</option>
				<option>2016</option>
			</select>
		</div>
		<div>
			City: <select name="location">
			<option selected>${searchDetails.location}</option>
				<option>Sydney</option>
				<option>Melbourne</option>
				<option>Brisbane</option>
				<option>Adelaide</option>
				<option>Hobart</option>
			</select>
		</div>
		<div>
			Max. Price per night: <input type="number" name="maxPrice" min="0" value="${searchDetails.maxPrice}">
		</div>
		<div>
			<input type="submit" name="action" value="Search">
		</div>
	</form>

</div>