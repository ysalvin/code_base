<!DOCTYPE html>
<html>
<head>
	<title>assignment 2</title>
</head>
<body align=center>
	<h3>Question 8_1</h3>
		<tbody>
			<?php
			// Database connection
			include("connect.php");

			// SQL query
			$sql = "SELECT ServiceArea.name service_area_name, COUNT(Locker.locker_ID) AS locker_count, ServiceArea.service_area_ID
			FROM ServiceArea INNER JOIN Locker ON ServiceArea.service_area_ID = locker.service_area_ID
			WHERE ServiceArea.service_area_ID NOT IN(
				SELECT ServiceArea.parent_area_ID
				FROM ServiceArea
				WHERE ServiceArea.parent_area_ID IS NOT NULL
			)
			GROUP BY ServiceArea.service_area_ID
			HAVING COUNT(Locker.locker_ID)>=1
			ORDER BY COUNT(Locker.locker_ID) DESC;";

			$resultB = mysqli_query($conn,$sql) or die( "Unable to execute query:".mysqli_error());

			
			
			echo "<form action='q8.php' method='GET'>";
			echo "<select name='service_area_ID'>";

			while($rowB = mysqli_fetch_array($resultB, MYSQLI_ASSOC))
			{
				echo "<option value='".$rowB['service_area_ID']."'>";
				echo $rowB['service_area_name'], ": " , $rowB['locker_count'];
				echo "</option>";
			}
			echo "</select>";
			echo "<br>";
			echo "<br>";

			// echo "<a href='q8.php'>";
			// echo "<input type='submit'/>";
    		// echo "</a>";
			echo "<input type='submit'>";
			echo "</form>";


			mysqli_close($conn);
			?>
		</tbody>
	<!-- <br><a href='index.html'>back</a> -->
</body>
</html>



<!-- SELECT ServiceArea.name service_area_name, COUNT(Locker.locker_ID) AS locker_count
FROM ServiceArea INNER JOIN Locker ON ServiceArea.service_area_ID = locker.service_area_ID
WHERE ServiceArea.service_area_ID NOT IN(
    SELECT ServiceArea.parent_area_ID
    FROM ServiceArea
    WHERE ServiceArea.parent_area_ID IS NOT NULL
)
GROUP BY ServiceArea.service_area_ID
HAVING COUNT(Locker.locker_ID)>=1; -->
