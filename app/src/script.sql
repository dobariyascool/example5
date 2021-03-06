USE [master]
GO
/****** Object:  Database [abPOS]    Script Date: 30/12/2016 2:03:06 PM ******/
CREATE DATABASE [abPOS] ON  PRIMARY 
( NAME = N'abPOS', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL10_50.MSSQLSERVER\MSSQL\DATA\abPOS.mdf' , SIZE = 14336KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'abPOS_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL10_50.MSSQLSERVER\MSSQL\DATA\abPOS_log.ldf' , SIZE = 24384KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [abPOS] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [abPOS].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [abPOS] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [abPOS] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [abPOS] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [abPOS] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [abPOS] SET ARITHABORT OFF 
GO
ALTER DATABASE [abPOS] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [abPOS] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [abPOS] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [abPOS] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [abPOS] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [abPOS] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [abPOS] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [abPOS] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [abPOS] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [abPOS] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [abPOS] SET  DISABLE_BROKER 
GO
ALTER DATABASE [abPOS] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [abPOS] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [abPOS] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [abPOS] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [abPOS] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [abPOS] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [abPOS] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [abPOS] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [abPOS] SET  MULTI_USER 
GO
ALTER DATABASE [abPOS] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [abPOS] SET DB_CHAINING OFF 
GO
USE [abPOS]
GO
/****** Object:  User [ab]    Script Date: 30/12/2016 2:03:06 PM ******/
CREATE USER [ab] FOR LOGIN [ab] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [ab]
GO
/****** Object:  StoredProcedure [dbo].[posAccountCategoryMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAccountCategoryMaster_DeleteAll]
	 @AccountCategoryMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	Declare @TotalRecords int,@UpdatedRawCount int

	DELETE
	FROM
		 posAccountCategoryMaster
	WHERE
		AccountCategoryMasterId IN (SELECT * from dbo.Parse(@AccountCategoryMasterIds, ','))
	
		AND AccountCategoryMasterId NOT IN
		(
			SELECT linktoAccountCategoryMasterId from posAccountMaster WHERE  IsDeleted =0
			AND linktoAccountCategoryMasterId IN (SELECT * from dbo.Parse(@AccountCategoryMasterIds, ','))

		)
	set @UpdatedRawCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@AccountCategoryMasterIds, ','))
		
		IF @TotalRecords = @UpdatedRawCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END
	return
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountCategoryMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAccountCategoryMaster_Insert]
	 @AccountCategoryMasterId int OUTPUT
	,@ShortName varchar(10) = NULL
	,@AccountCategoryName varchar(50)
	,@Description varchar(500) = NULL
	,@IsIncome bit = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AccountCategoryMasterId FROM posAccountCategoryMaster WHERE AccountCategoryName = @AccountCategoryName AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @AccountCategoryMasterId = AccountCategoryMasterId FROM posAccountCategoryMaster WHERE AccountCategoryName = @AccountCategoryName AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posAccountCategoryMaster
	(
		 ShortName
		,AccountCategoryName
		,Description
		,IsIncome
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @ShortName
		,@AccountCategoryName
		,@Description
		,@IsIncome
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @AccountCategoryMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountCategoryMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAccountCategoryMaster_Select]
	 @AccountCategoryMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posAccountCategoryMaster.*
	FROM
		 posAccountCategoryMaster
	WHERE
		AccountCategoryMasterId = @AccountCategoryMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountCategoryMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAccountCategoryMaster_SelectAll]
	 @AccountCategoryName varchar(50)
	,@IsIncome bit = NULL
	,@linktoBusinessmasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posAccountCategoryMaster.*
	FROM
		 posAccountCategoryMaster
	WHERE
		AccountCategoryName LIKE @AccountCategoryName + '%'
		AND ((@IsIncome IS NULL AND (IsIncome IS NULL OR IsIncome IS NOT NULL)) OR (@IsIncome IS NOT NULL AND IsIncome = @IsIncome))
		AND linktoBusinessMasterId = @linktoBusinessmasterId
	ORDER BY AccountCategoryName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountCategoryMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAccountCategoryMaster_Update]
	 @AccountCategoryMasterId int
	,@ShortName varchar(10) = NULL
	,@AccountCategoryName varchar(50)
	,@Description varchar(500) = NULL
	,@IsIncome bit = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AccountCategoryMasterId FROM posAccountCategoryMaster WHERE AccountCategoryName = @AccountCategoryName AND AccountCategoryMasterId != @AccountCategoryMasterId 
					AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posAccountCategoryMaster
	SET
		 ShortName = @ShortName
		,AccountCategoryName = @AccountCategoryName
		,Description = @Description
		,IsIncome = @IsIncome
	WHERE
		AccountCategoryMasterId = @AccountCategoryMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountCategoryMasterAccountCategoryName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAccountCategoryMasterAccountCategoryName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 AccountCategoryMasterId
		,AccountCategoryName
	FROM
		 posAccountCategoryMaster
	WHERE
		linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY AccountCategoryName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAccountMaster_DeleteAll]
	 @AccountMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posAccountMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		AccountMasterId IN (SELECT * from dbo.Parse(@AccountMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAccountMaster_Insert]
	 @AccountMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@linktoAccountCategoryMasterId int
	,@AccountName varchar(100)
	,@AccountNumber varchar(15)
	,@OpeningBalance money
	,@IsCredit bit
	,@CreditLimit money
	,@Description varchar(250) = NULL
	,@Address varchar(500) = NULL
	,@linktoCountryMasterId smallint = NULL
	,@linktoStateMasterId smallint = NULL
	,@linktoCityMasterId int = NULL
	,@linktoAreaMasterId int = NULL
	,@Zipcode varchar(10) = NULL
	,@Phone1 varchar(15) = NULL
	,@Phone2 varchar(15) = NULL
	,@Fax varchar(15) = NULL
	,@Email1 varchar(80) = NULL
	,@Email2 varchar(80) = NULL
	,@TIN varchar(15) = NULL
	,@CST varchar(15) = NULL
	,@PAN varchar(15) = NULL
	,@TDS varchar(15) = NULL
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AccountMasterId FROM posAccountMaster WHERE AccountName = @AccountName AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @AccountMasterId = AccountMasterId FROM posAccountMaster WHERE AccountName = @AccountName AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posAccountMaster
	(
		 linktoBusinessMasterId
		,linktoAccountCategoryMasterId
		,AccountName
		,AccountNumber
		,OpeningBalance
		,IsCredit
		,CreditLimit
		,Description
		,Address
		,linktoCountryMasterId
		,linktoStateMasterId
		,linktoCityMasterId
		,linktoAreaMasterId
		,Zipcode
		,Phone1
		,Phone2
		,Fax
		,Email1
		,Email2
		,TIN
		,CST
		,PAN
		,TDS
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@linktoAccountCategoryMasterId
		,@AccountName
		,@AccountNumber
		,@OpeningBalance
		,@IsCredit
		,@CreditLimit
		,@Description
		,@Address
		,@linktoCountryMasterId
		,@linktoStateMasterId
		,@linktoCityMasterId
		,@linktoAreaMasterId
		,@Zipcode
		,@Phone1
		,@Phone2
		,@Fax
		,@Email1
		,@Email2
		,@TIN
		,@CST
		,@PAN
		,@TDS
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @AccountMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAccountMaster_Select]
	 @AccountMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posAccountMaster.*
		,(SELECT AccountCategoryName FROM posAccountCategoryMaster WHERE AccountCategoryMasterId = linktoAccountCategoryMasterId) AS AccountCategory
		,(SELECT CountryName FROM posCountryMaster WHERE CountryMasterId = linktoCountryMasterId) AS Country
		,(SELECT StateName FROM posStateMaster WHERE StateMasterId = linktoStateMasterId) AS State
		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS City
		,(SELECT AreaName FROM posAreaMaster WHERE AreaMasterId = linktoAreaMasterId) AS Area
	FROM
		 posAccountMaster
	WHERE
		AccountMasterId = @AccountMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAccountMaster_SelectAll]
	 @linktoAccountCategoryMasterId int = NULL
	,@AccountName varchar(100)
	,@AccountNumber varchar(15)
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posAccountMaster.*
		,(SELECT AccountCategoryName FROM posAccountCategoryMaster WHERE AccountCategoryMasterId = linktoAccountCategoryMasterId) AS AccountCategory		
		,(SELECT CountryName FROM posCountryMaster WHERE CountryMasterId = linktoCountryMasterId) AS Country		
		,(SELECT StateName FROM posStateMaster WHERE StateMasterId = linktoStateMasterId) AS State		
		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS City		
		,(SELECT AreaName FROM posAreaMaster WHERE AreaMasterId = linktoAreaMasterId) AS Area
	FROM
		 posAccountMaster
	WHERE
		linktoAccountCategoryMasterId = ISNULL(@linktoAccountCategoryMasterId, linktoAccountCategoryMasterId)
		AND AccountName LIKE @AccountName + '%'
		AND AccountNumber LIKE @AccountNumber + '%'
		AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY AccountName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAccountMaster_Update]
	 @AccountMasterId int
	,@linktoBusinessMasterId smallint
	,@linktoAccountCategoryMasterId int
	,@AccountName varchar(100)
	,@AccountNumber varchar(15)
	,@OpeningBalance money
	,@IsCredit bit
	,@CreditLimit money
	,@Description varchar(250) = NULL
	,@Address varchar(500) = NULL
	,@linktoCountryMasterId smallint = NULL
	,@linktoStateMasterId smallint = NULL
	,@linktoCityMasterId int = NULL
	,@linktoAreaMasterId int = NULL
	,@Zipcode varchar(10) = NULL
	,@Phone1 varchar(15) = NULL
	,@Phone2 varchar(15) = NULL
	,@Fax varchar(15) = NULL
	,@Email1 varchar(80) = NULL
	,@Email2 varchar(80) = NULL
	,@TIN varchar(15) = NULL
	,@CST varchar(15) = NULL
	,@PAN varchar(15) = NULL
	,@TDS varchar(15) = NULL
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AccountMasterId FROM posAccountMaster WHERE AccountName = @AccountName AND AccountMasterId != @AccountMasterId AND IsDeleted = 0
					AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posAccountMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,linktoAccountCategoryMasterId = @linktoAccountCategoryMasterId
		,AccountName = @AccountName
		,AccountNumber = @AccountNumber
		,OpeningBalance = @OpeningBalance
		,IsCredit = @IsCredit
		,CreditLimit = @CreditLimit
		,Description = @Description
		,Address = @Address
		,linktoCountryMasterId = @linktoCountryMasterId
		,linktoStateMasterId = @linktoStateMasterId
		,linktoCityMasterId = @linktoCityMasterId
		,linktoAreaMasterId = @linktoAreaMasterId
		,Zipcode = @Zipcode
		,Phone1 = @Phone1
		,Phone2 = @Phone2
		,Fax = @Fax
		,Email1 = @Email1
		,Email2 = @Email2
		,TIN = @TIN
		,CST = @CST
		,PAN = @PAN
		,TDS = @TDS
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		AccountMasterId = @AccountMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountMasterAccountName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAccountMasterAccountName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 AccountMasterId
		,AccountName
	FROM
		 posAccountMaster
	WHERE
		IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY AccountName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAccountMasterAccountNameByAccountCategoryMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posAccountMasterAccountNameByAccountCategoryMasterId_SelectAll  
CREATE PROCEDURE [dbo].[posAccountMasterAccountNameByAccountCategoryMasterId_SelectAll]
	@linktoAccountCategoryMasterId Int,
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 AccountMasterId
		,AccountName
	FROM
		 posAccountMaster
	WHERE
	linktoAccountCategoryMasterId = @linktoAccountCategoryMasterId AND linktoBusinessMasterId = @linktoBusinessMasterId
	AND	IsDeleted = 0
	ORDER BY AccountName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessItemTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessItemTran_Delete]
	@linktoAddLessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	DELETE
	FROM
		 posAddLessItemTran
	WHERE
		linktoAddLessMasterId = @linktoAddLessMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1		
	END
	ELSE
	BEGIN
		SET @Status = 0	
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessItemTran_InsertAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessItemTran_InsertAll]
	 @linktoAddLessMasterId smallint
	,@linktoItemMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	BEGIN TRANSACTION

	DELETE
	FROM
		 posAddLessItemTran
	WHERE
		linktoAddLessMasterId = @linktoAddLessMasterId

	INSERT INTO posAddLessItemTran
	(
		 linktoAddLessMasterId
		,linktoItemMasterId
	)
	SELECT
		 @linktoAddLessMasterId
		,*
	FROM
		 dbo.Parse(@linktoItemMasterIds, ',')

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
		ROLLBACK TRANSACTION
	END
	ELSE
	BEGIN
		SET @Status = 0
		COMMIT TRANSACTION
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessItemTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessItemTran_SelectAll]
	 @linktoAddLessMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posAddLessItemTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
	FROM
		 posAddLessItemTran
	WHERE
		linktoAddLessMasterId = @linktoAddLessMasterId
	ORDER BY AddLessItemTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessMaster_DeleteAll]
	 @AddLessMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRawCount int

	DELETE
	FROM
		 posAddLessMaster
	WHERE
		AddLessMasterId IN (SELECT * from dbo.Parse(@AddLessMasterIds, ','))
		AND AddLessMasterId NOT IN
		(
			SELECT linktoAddLessMasterId FROM posAddLessItemTran WHERE IsEnabled = 1 AND 
			linktoAddLessMasterId IN (SELECT * from dbo.Parse(@AddLessMasterIds, ','))
		)
		
	SET @UpdatedRawCount = @@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@AddLessMasterIds, ','))
		IF @TotalRecords = @UpdatedRawCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessMaster_Insert]
	 @AddLessMasterId smallint OUTPUT
	,@Name varchar(20)
	,@CalculatedOn smallint
	,@Amount money
	,@IsPercentage bit
	,@IsRounding bit
	,@IsSale bit
	,@IsForAllItems bit
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AddLessMasterId FROM posAddLessMaster WHERE Name = @Name AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @AddLessMasterId = AddLessMasterId FROM posAddLessMaster WHERE Name = @Name AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posAddLessMaster
	(
		 Name
		,CalculatedOn
		,Amount
		,IsPercentage
		,IsRounding
		,IsSale
		,linktoBusinessMasterId
		,IsForAllItems
		,IsEnabled
	)
	VALUES
	(
		 @Name
		,@CalculatedOn
		,@Amount
		,@IsPercentage
		,@IsRounding
		,@IsSale
		,@linktoBusinessMasterId
		,@IsForAllItems
		,@IsEnabled
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @AddLessMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessMaster_Select]
	 @AddLessMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posAddLessMaster.*
	FROM
		 posAddLessMaster
	WHERE
		AddLessMasterId = @AddLessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessMaster_SelectAll]
	 @IsSale bit = NULL
	,@IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posAddLessMaster.*,case when CalculatedOn = 0 then 'Gross Amount' else 'Net Amount' end CalculatedOnText
	FROM
		 posAddLessMaster
	WHERE
		((@IsSale IS NULL AND (IsSale IS NULL OR IsSale IS NOT NULL)) OR (@IsSale IS NOT NULL AND IsSale = @IsSale))
		AND ((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessMaster_Update]
	 @AddLessMasterId smallint
	,@Name varchar(20)
	,@CalculatedOn smallint
	,@Amount money
	,@IsPercentage bit
	,@IsRounding bit
	,@IsSale bit
	,@linktoBusinessMasterId smallint
	,@IsForAllItems bit
	,@IsEnabled bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AddLessMasterId FROM posAddLessMaster WHERE Name = @Name AND AddLessMasterId != @AddLessMasterId AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posAddLessMaster
	SET
		 Name = @Name
		,CalculatedOn = @CalculatedOn
		,Amount = @Amount
		,IsPercentage = @IsPercentage
		,IsRounding = @IsRounding
		,IsSale = @IsSale
		,IsForAllItems=@IsForAllItems
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,IsEnabled = @IsEnabled
	WHERE
		AddLessMasterId = @AddLessMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAddLessMasterName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAddLessMasterName_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 AddLessMasterId
		,Name
	FROM
		 posAddLessMaster
	WHERE
		IsEnabled = 1
	ORDER BY Name

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAmentitiesMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAmentitiesMaster_DeleteAll]
	 @AmentitiesMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posAmentitiesMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		AmentitiesMasterId IN (SELECT * from dbo.Parse(@AmentitiesMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAmentitiesMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAmentitiesMaster_Insert]
	 @AmentitiesMasterId smallint OUTPUT
	,@linktoBusinessMasterId smallint
	,@AmentiesName varchar(100)
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AmentitiesMasterId FROM posAmentitiesMaster WHERE AmentiesName = @AmentiesName AND IsDeleted = 0)
	BEGIN
		SELECT @AmentitiesMasterId = AmentitiesMasterId FROM posAmentitiesMaster WHERE AmentiesName = @AmentiesName
		SET @Status = -2
		RETURN
	END
	INSERT INTO posAmentitiesMaster
	(
		 linktoBusinessMasterId
		,AmentiesName
		,Description
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@AmentiesName
		,@Description
		,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @AmentitiesMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAmentitiesMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAmentitiesMaster_Select]
	 @AmentitiesMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posAmentitiesMaster.*
	FROM
		 posAmentitiesMaster
	WHERE
		AmentitiesMasterId = @AmentitiesMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAmentitiesMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAmentitiesMaster_SelectAll]
	 @AmentiesName varchar(100) = NULL
	,@IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posAmentitiesMaster.*
	FROM
		 posAmentitiesMaster
	WHERE
		AmentiesName LIKE ISNULL(@AmentiesName,'') + '%'
		AND IsEnabled = ISNULL(@IsEnabled, IsEnabled)
		AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY AmentiesName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAmentitiesMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAmentitiesMaster_Update]
	 @AmentitiesMasterId smallint
	,@linktoBusinessMasterId smallint
	,@AmentiesName varchar(100)
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AmentitiesMasterId FROM posAmentitiesMaster WHERE AmentiesName = @AmentiesName AND AmentitiesMasterId != @AmentitiesMasterId AND IsDeleted = 0)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posAmentitiesMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,AmentiesName = @AmentiesName
		,Description = @Description
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		AmentitiesMasterId = @AmentitiesMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAmentitiesMasterAmentiesName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAmentitiesMasterAmentiesName_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 AmentitiesMasterId
		,AmentiesName
	FROM
		 posAmentitiesMaster
	WHERE
		IsEnabled = 1
		AND IsDeleted = 0
	ORDER BY AmentiesName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAppThemeMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAppThemeMaster_Select]
	 @linktoBusinessMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posAppThemeMaster.*
	FROM
		 posAppThemeMaster
	WHERE
		linktoBusinessMasterId = @linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAppThemeMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAppThemeMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posAppThemeMaster.*
	FROM
		 posAppThemeMaster
	
	ORDER BY AppThemeMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAreaMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAreaMaster_DeleteAll]
	 @AreaMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
		Declare @TotalRecords int,@RowCount int
		SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@AreaMasterIds, ','))
	UPDATE
		posAreaMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		AreaMasterId IN (SELECT * from dbo.Parse(@AreaMasterIds, ','))
		AND AreaMasterId NOT IN
		(
			SELECT CET.linktoAreaMasterId FROM posCustomerAddressTran CET WHERE CET.IsDeleted = 0 AND IsEnabled = 1 
			AND CET.linktoAreaMasterId  IN (SELECT * from dbo.Parse(@AreaMasterIds, ','))
		)
		SET @RowCount=@@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		IF @TotalRecords = @RowCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAreaMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posAreaMaster_Insert]
	 @AreaMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@AreaName varchar(50)
	,@ZipCode varchar(10)
	,@linktoCityMasterId int
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AreaMasterId FROM posAreaMaster WHERE AreaName = @AreaName AND IsDeleted = 0)
	BEGIN
		SELECT @AreaMasterId = AreaMasterId FROM posAreaMaster WHERE AreaName = @AreaName
		SET @Status = -2
		RETURN
	END
	INSERT INTO posAreaMaster
	(
		linktoBusinessMasterId
		,AreaName
		,ZipCode
		,linktoCityMasterId
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
	     @linktoBusinessMasterId
		,@AreaName
		,@ZipCode
		,@linktoCityMasterId
		,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @AreaMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAreaMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAreaMaster_Select]
	 @AreaMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posAreaMaster.*,StateMasterId,linktoCountryMasterId
		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS CityName
	FROM
		 posAreaMaster,posCityMaster,posStateMaster
	WHERE
		AreaMasterId = @AreaMasterId AND CityMasterId = linktoCityMasterId AND linktoStateMasterId = StateMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAreaMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAreaMaster_SelectAll]
	@IsEnabled bit,
	@linktoCountryMasterId smallint = NULL,
	@linktoStateMasterId smallint = NULL,
	@linktoCityMasterId int = NULL	,
	@linktoBusinessMasterId smallint 
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posAreaMaster.*,CityName		
	FROM
		 posAreaMaster,posCityMaster,posStateMaster 
	WHERE 
		posAreaMaster.IsEnabled = @IsEnabled AND posAreaMaster.IsDeleted=0
		AND posAreaMaster.linktoCityMasterId = CityMasterId AND linktoStateMasterId = StateMasterId
		AND linktoCountryMasterId = ISNULL(@linktoCountryMasterId,linktoCountryMasterId)
		AND linktoStateMasterId = ISNULL(@linktoStateMasterId,linktoStateMasterId)
		AND linktoCityMasterId = ISNULL(@linktoCityMasterId,linktoCityMasterId)
		AND posAreaMaster.linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY AreaName
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAreaMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAreaMaster_Update]
	 @AreaMasterId int
	,@AreaName varchar(50)
	,@ZipCode varchar(10)
	,@linktoCityMasterId int
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT AreaMasterId FROM posAreaMaster WHERE AreaName = @AreaName AND AreaMasterId != @AreaMasterId AND IsDeleted = 0)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posAreaMaster
	SET
		 AreaName = @AreaName
		,ZipCode = @ZipCode
		,linktoCityMasterId = @linktoCityMasterId
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		AreaMasterId = @AreaMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAreaMasterAreaName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAreaMasterAreaName_SelectAll]
	@linktoCityMasterId int 
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 AreaMasterId
		,AreaName
	FROM
		 posAreaMaster
	WHERE
		IsEnabled = 1
		AND linktoCityMasterId = @linktoCityMasterId
		AND IsDeleted=0
	ORDER BY AreaName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posAreaMasterCityWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posAreaMasterCityWise_SelectAll]
    
	 @linktoCityMasterId int
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posAreaMaster.*

		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS CityName
	FROM
		 posAreaMaster
	WHERE 
		 
		linktoCityMasterId = @linktoCityMasterId  
		AND IsDeleted=0

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBackupMaster_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBackupMaster_Delete]
	 @BackupMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posBackupMaster
	WHERE
		BackupMasterId = @BackupMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posBackupMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBackupMaster_Insert]
	 @BackupMasterId int OUTPUT
	,@BackupDateTime datetime
	,@BackupPath varchar(250)
	,@IsAutoBackup bit
	,@linktoCounterMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	INSERT INTO posBackupMaster
	(
		 BackupDateTime
		,BackupPath
		,IsAutoBackup
		,linktoCounterMasterId
	)
	VALUES
	(
		 @BackupDateTime
		,@BackupPath
		,@IsAutoBackup
		,@linktoCounterMasterId
	)

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
	BEGIN
		SET @BackupMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posBackupMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBackupMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		posBackupMaster.*
	FROM 
		posBackupMaster
	ORDER BY
		BackupDateTime DESC
	

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posBackupMasterByBackupDateTime_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBackupMasterByBackupDateTime_Select]
	@BackupDateTime datetime
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posBackupMaster.*
	FROM
		posBackupMaster
	WHERE
		CONVERT(varchar(8), BackupDateTime, 112) = CONVERT(varchar(8), @BackupDateTime, 112)
	

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posBankBookMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBankBookMaster_DeleteAll]
	 @BankBookMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE	
		 posBankBookMaster
	SET 
		IsDeleted=1
	WHERE
		BankBookMasterId IN (SELECT * from dbo.Parse(@BankBookMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBankBookMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBankBookMaster_Insert]
	 @BankBookMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@linktoAccountMasterIdBank int
	,@BankBookNumber varchar(10)
	,@BankBookDate date
	,@VoucherNumber varchar(20)
	,@IsPaid bit
	,@linktoAccountMasterId int
	,@Amount money
	,@ChequeNumber varchar(10) = NULL
	,@linktoBankMasterId int = NULL
	,@Remark varchar(500) = NULL
	,@CreateDateTime datetime
	,@IsDeleted bit=0
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posBankBookMaster
	(
		 linktoBusinessMasterId
		,linktoAccountMasterIdBank
		,BankBookNumber
		,BankBookDate
		,VoucherNumber
		,IsPaid
		,linktoAccountMasterId
		,Amount
		,ChequeNumber
		,linktoBankMasterId
		,Remark
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,IsDeleted
		
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@linktoAccountMasterIdBank
		,@BankBookNumber
		,@BankBookDate
		,@VoucherNumber
		,@IsPaid
		,@linktoAccountMasterId
		,@Amount
		,@ChequeNumber
		,@linktoBankMasterId
		,@Remark
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@IsDeleted
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @BankBookMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBankBookMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBankBookMaster_Select]
	 @BankBookMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posBankBookMaster.*
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterIdBank) AS AccountBank
		,(SELECT linktoAccountCategoryMasterId FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterIdBank) AS linktoAccountCategoryMasterId
		,(SELECT linktoAccountCategoryMasterId FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterId) AS linktoAccountCategoryMasterId2
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterId) AS Account
		,(SELECT BankName FROM posBankMaster WHERE BankMasterId = linktoBankMasterId) AS Bank
		
	FROM
		 posBankBookMaster
	WHERE
		BankBookMasterId = @BankBookMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBankBookMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBankBookMaster_SelectAll]
	 @linktoAccountMasterIdBank int = NULL
	,@BankBookDate date = NULL
	,@VoucherNumber varchar(20)
	,@IsPaid bit = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posBankBookMaster.*
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterIdBank) AS AccountBank		
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterId) AS Account		
		,(SELECT BankName FROM posBankMaster WHERE BankMasterId = linktoBankMasterId) AS Bank
	FROM
		 posBankBookMaster
	WHERE
		linktoAccountMasterIdBank = ISNULL(@linktoAccountMasterIdBank, linktoAccountMasterIdBank)
		AND CONVERT(varchar(8), BankBookDate, 112) = CONVERT(varchar(8), @BankBookDate, 112)
		AND VoucherNumber LIKE @VoucherNumber + '%'
		AND ((@IsPaid IS NULL AND (IsPaid IS NULL OR IsPaid IS NOT NULL)) OR (@IsPaid IS NOT NULL AND IsPaid = @IsPaid))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
		AND IsDeleted=0
	ORDER BY BankBookMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBankBookMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBankBookMaster_Update]
	 @BankBookMasterId int
	,@linktoBusinessMasterId smallint
	,@linktoAccountMasterIdBank int
	,@BankBookNumber varchar(10)
	,@BankBookDate date
	,@VoucherNumber varchar(20)
	,@IsPaid bit
	,@linktoAccountMasterId int
	,@Amount money
	,@ChequeNumber varchar(10) = NULL
	,@linktoBankMasterId int = NULL
	,@Remark varchar(500) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posBankBookMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,linktoAccountMasterIdBank = @linktoAccountMasterIdBank
		,BankBookNumber = @BankBookNumber
		,BankBookDate = @BankBookDate
		,VoucherNumber = @VoucherNumber
		,IsPaid = @IsPaid
		,linktoAccountMasterId = @linktoAccountMasterId
		,Amount = @Amount
		,ChequeNumber = @ChequeNumber
		,linktoBankMasterId = @linktoBankMasterId
		,Remark = @Remark
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		BankBookMasterId = @BankBookMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBankBookMasterBankBookNumber_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBankBookMasterBankBookNumber_Select]
	@linktoBusinessMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		MAX(CONVERT(int, BankBookNumber))+ 1 AS RecieptNumber
	FROM
		 posBankBookMaster	
	WHERE 
		linktoBusinessMasterId = @linktoBusinessMasterId

	RETURN
END









GO
/****** Object:  StoredProcedure [dbo].[posBankBookMasterBankName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBankBookMasterBankName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN
SET NOCOUNT ON;
 
   SELECT  *
   FROM posBankMaster
   WHERE
         linktoBusinessMasterId = @linktoBusinessMasterId AND IsEnabled = 1 AND Isdeleted = 0
   ORDER BY
          BankName
   RETURN
END

GO
/****** Object:  StoredProcedure [dbo].[posBankMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBankMaster_DeleteAll]
	 @BankMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	Declare @TotalRecords int,@UpdatedRowCount int
	SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@BankMasterIds, ','))

	UPDATE
		posBankMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		BankMasterId IN (SELECT * from dbo.Parse(@BankMasterIds, ','))
		AND BankMasterId NOT IN
		(
			SELECT linktoBankMasterId from posBankBookMaster where IsDeleted = 0
			AND linktoBankMasterId IN (SELECT * from dbo.Parse(@BankMasterIds, ','))
		)

	SET @UpdatedRowCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		IF @TotalRecords = @UpdatedRowCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END  

	RETURN
	End

GO
/****** Object:  StoredProcedure [dbo].[posBankMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBankMaster_Insert]
	 @BankMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@ShortName varchar(10)
	,@BankName varchar(100)
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT BankMasterId FROM posBankMaster WHERE BankName = @BankName AND linktoBusinessMasterId = @linktoBusinessMasterId And IsDeleted = 0)
	BEGIN
		SELECT @BankMasterId = BankMasterId FROM posBankMaster WHERE BankName = @BankName AND linktoBusinessMasterId = @linktoBusinessMasterId And IsDeleted = 0
		SET @Status = -2
		RETURN
	END
	INSERT INTO posBankMaster
	(
		 linktoBusinessMasterId
		,ShortName
		,BankName
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@ShortName
		,@BankName
	    ,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
	)

	IF @@ERROR <> 0
	BEGIN
	     SET @Status  =-1
    END
	ELSE
	BEGIN
		 SET @BankMasterId = @@IDENTITY
	     SET @Status = 0
	END   
RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBankMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBankMaster_Select]
@BankMasterId int,
@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON;
   
	SELECT 
		 posBankMaster.*
		 ,(SELECT BankName from posBankMaster where linktoBusinessMasterId=@linktoBusinessMasterId) 
	FROM
		posBankMaster
	WHERE
		 BankMasterId = @BankMasterId
Return
End
GO
/****** Object:  StoredProcedure [dbo].[posBankMaster_SelectaAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE  [dbo].[posBankMaster_SelectaAll]
	@BankName varchar(100) = NULL,
	@IsEnabled bit,
	@linktoBusinessMasterId smallint

AS
BEGIN
     -- Search bank by bankname 
	SET NOCOUNT ON;
	SELECT
		  *
	FROM
		posBankMaster
	 
	 WHERE  BankName LIKE ISNULL(@BankName,'') + '%' 	And IsEnabled = @IsEnabled And IsDeleted= 0 AND linktoBusinessMasterId=@linktoBusinessMasterId  
  	 ORDER BY BankName

	 RETURN
END

GO
/****** Object:  StoredProcedure [dbo].[posBankMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBankMaster_Update]
     @BankMasterId int
	,@linktoBusinessMasterId smallint
	,@ShortName varchar(10)
	,@BankName varchar(100)
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
SET NOCOUNT ON;

    -- Update statements for procedure here
	IF EXISTS(SELECT BankMasterId FROM posBankMaster WHERE BankName = @BankName  And BankMasterId !=@BankMasterId And linktoBusinessMasterId = @linktoBusinessMasterId AND IsDeleted = 0)
	BEGIN
		SELECT @BankMasterId = BankMasterId FROM posBankMaster WHERE BankName = @BankName  And BankMasterId !=@BankMasterId And linktoBusinessMasterId = @linktoBusinessMasterId And IsDeleted = 0
		SET @Status = -2
		RETURN
	END

 Update posBankMaster
 SET
		linktoBusinessMasterId= @linktoBusinessMasterId,
		ShortName = @ShortName,
		BankName =@BankName,
		IsEnabled=@IsEnabled,
		IsDeleted= @IsDeleted,
		UpdateDateTime =@UpdateDateTime,
		linktoUserMasterIdUpdatedBy =@linktoUserMasterIdUpdatedBy 
    WHERE 
         BankMasterId = @BankMasterId
     IF @@ERROR <> 0
     BEGIN
          SET @Status = -1
	 END
	 ELSE
	 BEGIN
	       SET @Status= 0
	 END 
	 RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posBankMasterBankName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBankMasterBankName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 BankMasterId
		,BankName
	FROM
		 posBankMaster
	WHERE 
		linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY BankName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBookingMaster_DeleteAll]
	 @BookingMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE
		posBookingMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		BookingMasterId IN (SELECT * from dbo.Parse(@BookingMasterIds, ','))
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBookingMaster_Insert]
	 @BookingMasterId int OUTPUT
	,@FromDate date
	,@ToDate date
	,@FromTime time = NULL
	,@ToTime time = NULL	
	,@linktoCustomerMasterId int
	,@NoOfAdults smallint
	,@NoOfChildren smallint
	,@TotalAmount money
	,@DiscountPercentage smallint
	,@DiscountAmount money
	,@ExtraAmount money
	,@NetAmount money
	,@PaidAmount money
	,@BalanceAmount money
	,@Remark varchar(500) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoBusinessMasterId smallint
	,@IsHourly bit
	,@IsDeleted bit
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posBookingMaster
	(
		 FromDate
		,ToDate
		,FromTime
		,ToTime
		,linktoCustomerMasterId
		,NoOfAdults
		,NoOfChildren
		,TotalAmount
		,DiscountPercentage
		,DiscountAmount
		,ExtraAmount
		,NetAmount
		,PaidAmount
		,BalanceAmount
		,Remark
		,IsHourly
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoBusinessMasterId
		,IsDeleted
		
	)
	VALUES
	(
		 @FromDate
		,@ToDate
		,@FromTime
		,@ToTime
		,@linktoCustomerMasterId
		,@NoOfAdults
		,@NoOfChildren
		,@TotalAmount
		,@DiscountPercentage
		,@DiscountAmount
		,@ExtraAmount
		,@NetAmount
		,@PaidAmount
		,@BalanceAmount
		,@Remark
		,@IsHourly
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoBusinessMasterId
		,@IsDeleted
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @BookingMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--posBookingMaster_Select 5
CREATE PROCEDURE [dbo].[posBookingMaster_Select]
	 @BookingMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posBookingMaster.*		
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer
	FROM
		 posBookingMaster
	WHERE
		BookingMasterId = @BookingMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- posBookingMaster_SelectAll '15-02-2016 00:00:00','15-02-2016 00:00:00',0
CREATE PROCEDURE [dbo].[posBookingMaster_SelectAll]
	 @FromDate date = NULL
	,@ToDate date = NULL
	,@linktoCustomerMasterId int = NULL
	,@linktoBusinessMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posBookingMaster.*
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer
		,STUFF((
				SELECT DISTINCT ',' + CONVERT(VARCHAR,TableName)  
				FROM posBookingTableTran BTT
				JOIN posTableMaster TM ON TM.TableMasterId = BTT.linktoTableMasterId
				WHERE BTT.linktoBookingMasterId = BookingMasterId
		FOR XML PATH(''), TYPE   
		).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As TableNames
	FROM
		 posBookingMaster
	WHERE
	   (
	     (CONVERT(varchar(8), @FromDate,112) BETWEEN CONVERT(varchar(8), FromDate, 112)  AND  CONVERT(varchar(8), ToDate, 112))
		  OR
		   (CONVERT(varchar(8), @ToDate,112) BETWEEN CONVERT(varchar(8), FromDate, 112)  AND  CONVERT(varchar(8), ToDate, 112))
	    )
		AND linktoCustomerMasterId = ISNULL(@linktoCustomerMasterId, linktoCustomerMasterId)
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY BookingMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBookingMaster_Update]
	 @BookingMasterId int
	,@FromDate date
	,@ToDate date
	,@FromTime time = NULL
	,@ToTime time = NULL
	,@linktoCustomerMasterId int
	,@NoOfAdults smallint
	,@NoOfChildren smallint
	,@TotalAmount money
	,@DiscountPercentage smallint
	,@DiscountAmount money
	,@ExtraAmount money
	,@NetAmount money
	,@PaidAmount money
	,@BalanceAmount money
	,@IsHourly bit
	,@Remark varchar(500) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoBusinessMasterId smallint
	,@IsDeleted bit
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posBookingMaster
	SET
		 FromDate = @FromDate
		,ToDate = @ToDate
		,FromTime = @FromTime
		,ToTime = @ToTime
		,linktoCustomerMasterId = @linktoCustomerMasterId
		,NoOfAdults = @NoOfAdults
		,NoOfChildren = @NoOfChildren
		,TotalAmount = @TotalAmount
		,DiscountPercentage = @DiscountPercentage
		,DiscountAmount = @DiscountAmount
		,ExtraAmount = @ExtraAmount
		,NetAmount = @NetAmount
		,PaidAmount = @PaidAmount
		,BalanceAmount = @BalanceAmount
		,Remark = @Remark
		,IsHourly=@IsHourly
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,IsDeleted = @IsDeleted
		
	WHERE
		BookingMasterId = @BookingMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingMaster_Verify]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posBookingMaster_Verify 12,0,'2016-02-15','2016-02-15','03:00:00','04:00:00',1
CREATE PROCEDURE [dbo].[posBookingMaster_Verify]
	 
	   @linktoTableMasterIds varchar(max)
	  ,@BookingMasterId int = NULL
	  ,@ToDate date
	  ,@FromDate date
	  ,@FromTime time(7) 
	  ,@ToTime time(7) 
	  ,@ISHourly bit
	
	
AS
BEGIN
	SET NOCOUNT ON

	
	IF @ISHourly = 1
	BEGIN	
		SELECT DISTINCT
			BookingMasterId
			,STUFF((
				SELECT DISTINCT ',' + CONVERT(VARCHAR,TableName)  
				FROM posBookingTableTran BTT
				JOIN posTableMaster TM ON TM.TableMasterId = BTT.linktoTableMasterId
				WHERE BTT.linktoBookingMasterId = BookingMasterId
		FOR XML PATH(''), TYPE   
		).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As TableNames 
		,CustomerName
		FROM posBookingMaster BM 
		JOIN posBookingTableTran BTT ON BTT.linktoBookingMasterId = BM.BookingMasterId		
		JOIN posCustomerMaster CM ON CM.CustomerMasterId = BM.linktoCustomerMasterId
		WHERE 
			(
				 (
					(
						(
								(CONVERT(VARCHAR(8),@FromDate,112)  BETWEEN CONVERT(VARCHAR(8),FromDate,112)   AND  CONVERT(VARCHAR(8),ToDate,112) )
							OR	(CONVERT(VARCHAR(8),@ToDate,112) BETWEEN  CONVERT(VARCHAR(8),FromDate,112)   AND CONVERT(VARCHAR(8),ToDate,112) )
						)
						AND 
						(
								(CONVERT(VARCHAR(8),DATEADD(MINUTE, 1,@FromTime),108) BETWEEN CONVERT(VARCHAR(8), FromTime,108) AND CONVERT(VARCHAR(8),ToTime,108) )
							OR  (CONVERT(VARCHAR(8),DATEADD(MINUTE, -1,@ToTime) ,108) BETWEEN CONVERT(VARCHAR(8),FromTime,108) AND CONVERT(VARCHAR(8),ToTime,108))
						)
					 )
					 AND IsHourly = 1
				  )	
				  OR
				  (
					(
						(CONVERT(VARCHAR(8),@FromDate,112) BETWEEN  CONVERT(VARCHAR(8), FromDate,112) AND  CONVERT(VARCHAR(8),ToDate,112)) 
					OR  
						(CONVERT(VARCHAR(8),@ToDate,112) BETWEEN  CONVERT(VARCHAR(8),FromDate,112)   AND CONVERT(VARCHAR(8),ToDate,112))
					)
					AND IsHourly = 0
				  )
			 )		
			 AND BTT.linktoTableMasterId IN (SELECT parsevalue FROM dbo.Parse(@linktoTableMasterIds,','))
			 AND BookingMasterId != ISNULL(@BookingMasterId,BookingMasterId)
			 AND BM.IsDeleted = 0 
	END
	ELSE
	BEGIN
		SELECT DISTINCT
			BookingMasterId 
			,STUFF((
				SELECT DISTINCT ',' + CONVERT(VARCHAR,TableName)  
				FROM posBookingTableTran BTT
				JOIN posTableMaster TM ON TM.TableMasterId = BTT.linktoTableMasterId
				WHERE BTT.linktoBookingMasterId = BookingMasterId
		FOR XML PATH(''), TYPE   
		).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As TableNames
		,CustomerName
		FROM posBookingMaster BM 
		JOIN posBookingTableTran BTT ON BTT.linktoBookingMasterId = BM.BookingMasterId
		JOIN posCustomerMaster CM ON CM.CustomerMasterId = BM.linktoCustomerMasterId
		WHERE 
			 ((CONVERT(VARCHAR(8),@FromDate,112) BETWEEN  CONVERT(VARCHAR(8), FromDate,112) AND  CONVERT(VARCHAR(8),ToDate,112) ) 
			OR  
			 (CONVERT(VARCHAR(8),@ToDate,112) BETWEEN  CONVERT(VARCHAR(8),FromDate,112)   AND CONVERT(VARCHAR(8),ToDate,112) ))

			AND BTT.linktoTableMasterId IN (SELECT parsevalue FROM dbo.Parse(@linktoTableMasterIds,','))
			AND BookingMasterId != ISNULL(@BookingMasterId,BookingMasterId)
			AND BM.IsDeleted = 0
	END
	
	

	RETURN
END



--SELECT 
	--	BookingMasterId FROM posBookingMaster 
 --   WHERE 
		
	--	  ((((  CONVERT(VARCHAR(8),@FromDate,112)  BETWEEN CONVERT(VARCHAR(8),FromDate,112)   AND  CONVERT(VARCHAR(8),ToDate,112) ) OR (CONVERT(VARCHAR(8),@ToDate,112) BETWEEN  CONVERT(VARCHAR(8),FromDate,112)   AND CONVERT(VARCHAR(8),ToDate,112) ))
	
	--	 AND ((CONVERT(VARCHAR(8),DATEADD(MINUTE, 1,@FromTime),108) BETWEEN   CONVERT(VARCHAR(8), FromTime,108)   AND  CONVERT(VARCHAR(8),ToTime,108) ) OR (CONVERT(VARCHAR(8),DATEADD(MINUTE, -1,@ToTime) ,108) BETWEEN CONVERT(VARCHAR(8),FromTime,108) AND   CONVERT(VARCHAR(8),ToTime,108) )) )
	--	 OR (
	--	 (  CONVERT(VARCHAR(8),@FromDate,112)  BETWEEN CONVERT(VARCHAR(8),FromDate,112)   AND  CONVERT(VARCHAR(8),ToDate,112) ) OR (CONVERT(VARCHAR(8),@ToDate,112) BETWEEN  CONVERT(VARCHAR(8),FromDate,112)   AND CONVERT(VARCHAR(8),ToDate,112) ) ))
	--	 AND linktoTableMasterId = @linktoTableMasterId
	--	 AND BookingMasterId != ISNULL(@BookingMasterId,BookingMasterId)
	--	 AND IsDeleted = 0
	--AND ((CONVERT(VARCHAR(8),@FromTime,108) BETWEEN   CONVERT(VARCHAR(8), FromTime,108)   AND  CONVERT(VARCHAR(8),ToTime,108) ) OR (CONVERT(VARCHAR(8),@ToTime,108) BETWEEN CONVERT(VARCHAR(8),FromTime,108) AND   CONVERT(VARCHAR(8),ToTime,108) )) 
		-- AND ((CONVERT(VARCHAR(8),@FromTime,108) BETWEEN   CONVERT(VARCHAR(8), DATEADD(SECOND, -1,FromTime),108)   AND  CONVERT(VARCHAR(8),  DATEADD(SECOND, -1,ToTime) ,108) ) OR (CONVERT(VARCHAR(8),@ToTime,108) BETWEEN CONVERT(VARCHAR(8),DATEADD(SECOND, -1,FromTime),108) AND   CONVERT(VARCHAR(8),DATEADD(SECOND, -1,ToTime),108) )) 


GO
/****** Object:  StoredProcedure [dbo].[posBookingMasterDailyChart_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posBookingMasterDailyChart_SelectAll '2016-01-27','2016-02-26',1
CREATE PROCEDURE [dbo].[posBookingMasterDailyChart_SelectAll]
	@FromDate DateTime,
	@ToDate DateTime,
	@linktoBusinessMasterId smallint	
AS
BEGIN
	
	DECLARE @cols as varchar(max),@query as varchar(max),@SortOder int

	CREATE TABLE #TempTable(thedate varchar(10),BookingValue int,TableName varchar(50),TableMasterID int,SortOrder int,IsHourly bit)

	set @SortOder = 0

	WHILE @FromDate <= @ToDate
	BEGIN

		INSERT INTO #TempTable(thedate,BookingValue,TableName,TableMasterID,SortOrder,IsHourly)
		SELECT 
			Convert(varchar(10),@FromDate,105),CASE WHEN BM.BookingMasterId > 0 THEN BM.BookingMasterId ELSE 0 END,TM.TableName,TM.TableMasterId,@SortOder,0
		FROM 
			posTableMaster TM
		LEFT JOIN
			posBookingTableTran BTT ON BTT.linktoTableMasterId = TM.TableMasterId
		LEFT JOIN 
			posBookingMaster BM ON bm.BookingMasterId = BTT.linktoBookingMasterId and BM.IsDeleted = 0
			AND (convert(varchar(8),@FromDate,112) BETWEEN convert(varchar(8),FromDate,112) AND convert(varchar(8),ToDate,112))
		WHERE 
			tm.IsEnabled = 1 
			AND tm.IsDeleted = 0
			AND IsBookingAvailable = 1 
			AND TM.linktoBusinessMasterId = @linktoBusinessMasterId

	  SET @FromDate = DATEADD(DAY, 1, @FromDate)
	  SET @SortOder = @SortOder + 1
	END



	SELECT @cols = STUFF((SELECT ',' + '[' + CONVERT(VARCHAR,thedate) + ']'
				FROM #TempTable		
				GROUP BY SortOrder,thedate
				ORDER BY SortOrder
				FOR XML PATH(''), TYPE
				).value('.', 'NVARCHAR(MAX)') 
		   ,1,1,'')



	SET @query =	'SELECT TableMasterID,IsHourly,TableName,' + @cols + ' FROM
					(
						SELECT thedate,BookingValue ,TableName,TableMasterID,IsHourly
						FROM #TempTable 
					)A
					PIVOT
					(
						MAX(BookingValue)
						FOR thedate in (' + @Cols + ')
					)pvt'

	EXEC(@query)



	DROP TABLE #TempTable
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingMasterHourlyChart_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posBookingMasterHourlyChart_SelectAll 1,'2016-01-22'
CREATE PROCEDURE [dbo].[posBookingMasterHourlyChart_SelectAll]	
	@FromDate DateTime,
	@linktoBusinessMasterId smallint
AS
BEGIN
	Declare @TotalTimeSlots int,@cols as varchar(max),@query as varchar(max),@count int,@FromTimeSlot int,@ToTimeSlot int,@TotRecords int

	set @TotalTimeSlots = 0	
	set @TotRecords = 1

	CREATE TABLE #TempTable(Id int identity(1,1),TableName varchar(50),TableMasterID int,IsHourly bit,FromTime time(7),ToTime time(7),BookingMasterId int)
	CREATE TABLE #TempTable1(TableName varchar(50),TableMasterID int,IsHourly bit,FromToTime int,BookingMasterId int)
	CREATE TABLE #TempMainTable(thedate varchar(10),BookingValue varchar(max),TableName varchar(50),TableMasterID int,TimeSlot int,IsHourly bit)

	insert into #TempTable(TableName,TableMasterID,IsHourly,FromTime,ToTime,BookingMasterId)
	SELECT 		
		TM.TableName,TM.TableMasterId,IsHourly,FromTime,ToTime,BookingMasterId
	FROM 
		posTableMaster TM
	JOIN
			posBookingTableTran BTT ON BTT.linktoTableMasterId = TM.TableMasterId
			AND linktoBookingMasterId IN (SELECT BM1.BookingMasterId FROM posBookingMaster BM1 WHERE (convert(varchar(8),@FromDate,112) BETWEEN convert(varchar(8),FromDate,112) AND convert(varchar(8),ToDate,112)))
	JOIN 
		posBookingMaster BM ON BM.BookingMasterId = BTT.linktoBookingMasterId AND BM.IsDeleted = 0
			AND (convert(varchar(8),@FromDate,112) BETWEEN convert(varchar(8),FromDate,112) AND convert(varchar(8),ToDate,112))
	WHERE 
		tm.IsEnabled = 1 AND tm.IsDeleted = 0 AND IsHourly = 1 AND IsBookingAvailable = 1 AND TM.linktoBusinessMasterId = @linktoBusinessMasterId

	
	select @count = count(*) from #TempTable


	While @TotRecords <= @count
	Begin
		select 	@FromTimeSlot = (datepart(hh,FromTime)),@ToTimeSlot = (datepart(hh,ToTime)) from #TempTable where Id = @TotRecords

		if(@FromTimeSlot > @ToTimeSlot)
		begin
			insert into #TempTable1(TableName,TableMasterID,IsHourly,FromToTime,BookingMasterId)
			select TableName,TableMasterID,IsHourly,0,BookingMasterId from #TempTable
			where Id = @TotRecords

			while (@FromTimeSlot <= 23)
			begin
				insert into #TempTable1(TableName,TableMasterID,IsHourly,FromToTime,BookingMasterId)
				select TableName,TableMasterID,IsHourly,@FromTimeSlot,BookingMasterId from #TempTable
				where Id = @TotRecords
				SET @FromTimeSlot = @FromTimeSlot + 1
			end
		end
		ELSE
		BEGIN
			while (@FromTimeSlot < @ToTimeSlot)
			begin
				insert into #TempTable1(TableName,TableMasterID,IsHourly,FromToTime,BookingMasterId)
				select TableName,TableMasterID,IsHourly,@FromTimeSlot,BookingMasterId from #TempTable
				where Id = @TotRecords
				SET @FromTimeSlot = @FromTimeSlot + 1
			end
		END
		SET @TotRecords = @TotRecords + 1
	
	end

	WHILE @TotalTimeSlots < 24
	BEGIN
		insert into #TempTable1(TableName,TableMasterID,IsHourly,FromToTime,BookingMasterId)
		SELECT 		
			TM.TableName,TM.TableMasterId,IsHourly,@TotalTimeSlots,BookingMasterId
		FROM 
			posTableMaster TM
		JOIN
				posBookingTableTran BTT ON BTT.linktoTableMasterId = TM.TableMasterId
				AND linktoBookingMasterId IN (SELECT BM1.BookingMasterId FROM posBookingMaster BM1 WHERE (convert(varchar(8),@FromDate,112) BETWEEN convert(varchar(8),FromDate,112) AND convert(varchar(8),ToDate,112)))
		JOIN 
			posBookingMaster BM ON BM.BookingMasterId = BTT.linktoBookingMasterId AND BM.IsDeleted = 0
				AND (convert(varchar(8),@FromDate,112) BETWEEN convert(varchar(8),FromDate,112) AND convert(varchar(8),ToDate,112))
		WHERE 
			tm.IsEnabled = 1 AND tm.IsDeleted = 0 AND  IsHourly = 0 AND IsBookingAvailable = 1 AND TM.linktoBusinessMasterId = @linktoBusinessMasterId
		SET @TotalTimeSlots = @TotalTimeSlots + 1
	END

	 SET @TotalTimeSlots = 0

	WHILE @TotalTimeSlots < 24
	BEGIN

		INSERT INTO #TempMainTable(thedate,BookingValue,TableName,TableMasterID,TimeSlot)
		SELECT DISTINCT
			'T' + convert(varchar(10),@TotalTimeSlots),
			CASE WHEN IsHourly = 0 THEN 1 
			ELSE CASE WHEN FromToTime IS NOT NULL THEN 1 ELSE 0 END END,
			TM.TableName,TM.TableMasterId,@TotalTimeSlots
		FROM 
			posTableMaster TM
		LEFT JOIN
				#TempTable1 TT ON TT.TableMasterID = TM.TableMasterId AND FromToTime = @TotalTimeSlots
		WHERE 
			tm.IsEnabled = 1 AND tm.IsDeleted = 0 AND IsBookingAvailable = 1 AND TM.linktoBusinessMasterId = @linktoBusinessMasterId

	  SET @TotalTimeSlots = @TotalTimeSlots + 1
	END


	SELECT @cols = STUFF((SELECT ',' + '[' + thedate + ']'
            FROM #TempMainTable
			GROUP BY TimeSlot,thedate
			ORDER BY TimeSlot	
            FOR XML PATH(''), TYPE
            ).value('.', 'NVARCHAR(MAX)') 
       ,1,1,'')



	SET @query =	'SELECT DISTINCT TableMasterId,TableName,' + @cols + ' FROM
					(
						SELECT DISTINCT thedate,BookingValue ,TableName,TableMasterId
						FROM #TempMainTable 
					)A
					PIVOT
					(
						MAX(BookingValue)
						FOR thedate in (' + @Cols + ')
					)pvt'
	EXEC (@query)
	drop table #TempMainTable
	drop table #TempTable1
	drop table #TempTable
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingPaymentTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBookingPaymentTran_Insert]
	 @BookingPaymentTranId bigint OUTPUT
	,@linktoBookingMasterId int
	,@linktoPaymentTypeMasterId smallint
	,@linktoCustomerMasterId int = NULL
	,@PaymentDateTime datetime
	,@AmountPaid money
	,@Remark varchar(250) = NULL
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posBookingPaymentTran
	(
		 linktoBookingMasterId
		,linktoPaymentTypeMasterId
		,linktoCustomerMasterId
		,PaymentDateTime
		,AmountPaid
		,Remark
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoBookingMasterId
		,@linktoPaymentTypeMasterId
		,@linktoCustomerMasterId
		,@PaymentDateTime
		,@AmountPaid
		,@Remark
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @BookingPaymentTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingPaymentTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBookingPaymentTran_SelectAll]

@linktoBookingMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posBookingPaymentTran.*			
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType				
	FROM
		 posBookingPaymentTran
	WHERE
		IsDeleted = 0 AND linktoBookingMasterId=@linktoBookingMasterId AND linktoPaymentTypeMasterId > 0
	UNION ALL
		SELECT
		posBookingPaymentTran.*	
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer
	FROM
		 posBookingPaymentTran
	WHERE
		IsDeleted = 0 AND linktoBookingMasterId=@linktoBookingMasterId AND linktoCustomerMasterId > 0

	ORDER BY BookingPaymentTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingPaymentTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBookingPaymentTran_Update]
	 @BookingPaymentTranId bigint
	,@linktoBookingMasterId int
	,@linktoPaymentTypeMasterId smallint
	,@linktoCustomerMasterId int = NULL
	,@PaymentDateTime datetime
	,@AmountPaid money
	,@Remark varchar(250) = NULL
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posBookingPaymentTran
	SET
		 linktoBookingMasterId = @linktoBookingMasterId
		,linktoPaymentTypeMasterId = @linktoPaymentTypeMasterId
		,linktoCustomerMasterId = @linktoCustomerMasterId
		,PaymentDateTime = @PaymentDateTime
		,AmountPaid = @AmountPaid
		,Remark = @Remark
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		BookingPaymentTranId = @BookingPaymentTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingTableTran_InsertAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBookingTableTran_InsertAll]
	 @linktoBookingMasterId int
	,@linktoTableMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	BEGIN TRANSACTION

	DELETE
	FROM
		 posBookingTableTran
	WHERE
		linktoBookingMasterId = @linktoBookingMasterId

	INSERT INTO posBookingTableTran
	(
		 linktoBookingMasterId
		,linktoTableMasterId
	)
	SELECT
		 @linktoBookingMasterId
		,*
	FROM
		 dbo.Parse(@linktoTableMasterIds, ',')

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
		ROLLBACK TRANSACTION
	END
	ELSE
	BEGIN
		SET @Status = 0
		COMMIT TRANSACTION
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBookingTableTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posBookingTableTran_SelectAll 0,1,1,2

CREATE PROCEDURE [dbo].[posBookingTableTran_SelectAll]
	 @linktoBookingMasterId int
	 ,@linktoCounterMasterId smallint
	 ,@linktoOrderTypeMasterId smallint
	 ,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT DISTINCT
		TM.TableMasterId,TM.TableName,BTT.BookingTableTranId,TM.DailyBookingRate,TM.HourlyBookingRate	
	FROM
		posTableMaster TM	
	JOIN
		posCounterTableTran CTT ON CTT.linktoTableMasterId = TM.TableMasterId 
	LEFT JOIN
		posBookingTableTran BTT ON BTT.linktoTableMasterId = TM.TableMasterId AND BTT.linktoBookingMasterId = @linktoBookingMasterId
	
	WHERE 
		TM.IsEnabled = 1 
		AND TM.IsDeleted = 0
		AND CTT.linktoCounterMasterId = @linktoCounterMasterId 
		AND IsBookingAvailable = 1
		AND TM.linktoOrderTypeMasterId = @linktoOrderTypeMasterId
		AND TM.linktoBusinessMasterId = @linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessDescription_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessDescription_DeleteAll]
	 @BusinessDescriptionIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		 posBusinessDescription
	WHERE
		BusinessDescriptionId IN (SELECT * from dbo.Parse(@BusinessDescriptionIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessDescription_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessDescription_Insert]
	 @BusinessDescriptionId smallint OUTPUT
	,@Title varchar(250)
	,@Description varchar(MAX)
	,@linktoBusinessMasterId smallint
	,@IsDefault bit
	,@linktoUserMasterIdCreatedBy smallint
	,@CreateDateTime datetime
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF;

	IF EXISTS(SELECT BusinessDescriptionId FROM posBusinessDescription WHERE Title = @Title)
	BEGIN
		SELECT @BusinessDescriptionId = BusinessDescriptionId FROM posBusinessDescription WHERE Title = @Title
		SET @Status = -2
		RETURN
	END
	INSERT INTO posBusinessDescription
	(
		 Title
		,Description
		,linktoBusinessMasterId
		,IsDefault
		,linktoUserMasterIdCreatedBy
		,CreateDateTime
		
	)
	VALUES
	(
		 @Title
		,@Description
		,@linktoBusinessMasterId
		,@IsDefault
		,@linktoUserMasterIdCreatedBy
		,@CreateDateTime
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @BusinessDescriptionId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessDescription_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessDescription_Select]
	 @BusinessDescriptionId smallint = NULL
	 ,@Title varchar(250) = NULL
	 ,@linktoBusinessMasterId smallint = NULL
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 BD.*
	FROM
		 posBusinessDescription BD
	WHERE
		BusinessDescriptionId = ISNULL(@BusinessDescriptionId,BusinessDescriptionId)
		and Title = ISNULL(@Title,Title)
		and linktoBusinessMasterId = ISNULL(@linktoBusinessMasterId,linktoBusinessMasterId)

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessDescription_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessDescription_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posBusinessDescription.*
	FROM
		 posBusinessDescription
	WHERE
		linktoBusinessMasterId = @linktoBusinessMasterId
	
	ORDER BY Title

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessDescription_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBusinessDescription_Update]
	 @BusinessDescriptionId smallint
	,@Title varchar(250)
	,@Description varchar(MAX)
	,@linktoBusinessMasterId smallint
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@UpdateDateTime datetime = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT BusinessDescriptionId FROM posBusinessDescription WHERE Title = @Title AND BusinessDescriptionId != @BusinessDescriptionId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posBusinessDescription
	SET
		 Title = @Title
		,Description = @Description
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,UpdateDateTime = @UpdateDateTime
		
	WHERE
		BusinessDescriptionId = @BusinessDescriptionId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessGalleryTran_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessGalleryTran_DeleteAll]
	 @BusinessGalleryTranIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		 posBusinessGalleryTran
	WHERE
		BusinessGalleryTranId IN (SELECT * from dbo.Parse(@BusinessGalleryTranIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessGalleryTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBusinessGalleryTran_Insert]
	 @BusinessGalleryTranId int OUTPUT
	,@ImageTitle varchar(50) = NULL
	,@ImageName varchar(100) = NULL
	,@linktoBusinessMasterId int
	,@SortOrder smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posBusinessGalleryTran
	(
		 ImageTitle
		,ImageName
		,linktoBusinessMasterId
		,SortOrder
	)
	VALUES
	(
		 @ImageTitle
		,@ImageName
		,@linktoBusinessMasterId
		,@SortOrder
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @BusinessGalleryTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessGalleryTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessGalleryTran_Select]
	 @BusinessGalleryTranId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posBusinessGalleryTran.*
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posBusinessGalleryTran
	WHERE
		BusinessGalleryTranId = @BusinessGalleryTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessGalleryTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessGalleryTran_SelectAll]
 @linktoBusinessMasterId smallint = NULL
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posBusinessGalleryTran.*
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posBusinessGalleryTran

	WHERE 
		linktoBusinessMasterId = ISNULL(@linktoBusinessMasterId,linktoBusinessMasterId)
	ORDER BY CASE WHEN SortOrder IS NULL THEN 1 ELSE 0 END, SortOrder

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessGalleryTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessGalleryTran_Update]
	 @BusinessGalleryTranId int
	,@ImageTitle varchar(50) = NULL
	,@ImageName varchar(100) = NULL
	,@linktoBusinessMasterId int
	,@SortOrder smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posBusinessGalleryTran
	SET
		 ImageTitle = @ImageTitle
		,ImageName = @ImageName
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,SortOrder = @SortOrder
	WHERE
		BusinessGalleryTranId = @BusinessGalleryTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessHoursTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessHoursTran_Update]
	 @BusinessHoursTranId smallint
	,@DayOfWeek smallint
	,@OpeningTime time
	,@ClosingTime time
	,@linktoBusinessMasterId smallint
	,@BreakStartTime time = null
	,@BreakEndTime time = null
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posBusinessHoursTran
	SET
		 DayOfWeek = @DayOfWeek
		,OpeningTime = @OpeningTime
		,ClosingTime = @ClosingTime
		,BreakStartTime=@BreakStartTime
		,BreakEndTime=@BreakEndTime
		,linktoBusinessMasterId = @linktoBusinessMasterId
	WHERE
		BusinessHoursTranId = @BusinessHoursTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessHoursTranBusinessWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create PROCEDURE [dbo].[posBusinessHoursTranBusinessWise_SelectAll]
   @linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posBusinessHoursTran.*

			,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posBusinessHoursTran
	WHERE 
		linktoBusinessMasterId = @linktoBusinessMasterId
	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessInfoAnswerMasterByBusinessTypeMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessInfoAnswerMasterByBusinessTypeMasterId_SelectAll]
	@linktoBusinessTypeMasterId smallint
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
        BQ.Question as BusinessInfoQuestion,
		BQ.QuestionType
		,BQ.BusinessInfoQuestionMasterId
		,CASE WHEN 
				BQ.QuestionType = 1 
		THEN  
			(SELECT BIA.Answer FROM posBusinessInfoAnswerTran BIA WHERE BIA.linktoBusinessInfoQuestionMasterId = BQ.BusinessInfoQuestionMasterId AND BIA.linktoBusinessMasterId = @linktoBusinessMasterId AND BIA.Answer IS NOT NULL)
		ELSE 
			(STUFF((SELECT distinct ', ' + CAST(BA.Answer As varchar(50)) FROM posBusinessInfoAnswerMaster BA join posBusinessInfoAnswerTran BIA on BIA.linktoBusinessInfoAnswerMasterId = BA.BusinessInfoAnswerMasterId
		       WHERE BIA.linktoBusinessInfoQuestionMasterId = BQ.BusinessInfoQuestionMasterId AND BA.IsEnabled = 1 AND BIA.linktoBusinessMasterId = @linktoBusinessMasterId FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'),1,2,''))end As Answer
	FROM
		posBusinessInfoQuestionMaster BQ 

	WHERE
		BQ.IsEnabled = 1
		and BQ.linktoBusinessTypeMasterId = @linktoBusinessTypeMasterId
	ORDER BY
		BQ.SortOrder
		
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessInfoAnswerMasterBylinktoQuestionMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessInfoAnswerMasterBylinktoQuestionMasterId_SelectAll]
	 @linktoBusinessInfoQuestionMasterId int
	 	,@linktoBusinessMasterId smallint
	,@linktoBusinessTypeMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		BIA.*,BIAT.BusinessInfoAnswerTranId
		,(SELECT Question FROM posBusinessInfoQuestionMaster WHERE BusinessInfoQuestionMasterId = BIA.linktoBusinessInfoQuestionMasterId and linktoBusinessTypeMasterId=@linktoBusinessTypeMasterId) AS BusinessInfoQuestion
	FROM
		posBusinessInfoAnswerMaster BIA
	LEFT JOIN
		posBusinessInfoAnswerTran BIAT ON BIAT.linktoBusinessInfoAnswerMasterId = BIA.BusinessInfoAnswerMasterId AND BIAT.linktoBusinessInfoQuestionMasterId = @linktoBusinessInfoQuestionMasterId AND linktoBusinessMasterId=@linktoBusinessMasterId
				
	WHERE
		BIA.linktoBusinessInfoQuestionMasterId = @linktoBusinessInfoQuestionMasterId
	ORDER BY BusinessInfoAnswerMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessInfoAnswerTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posBusinessInfoAnswerTran_Delete]
	 @linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posBusinessInfoAnswerTran
	WHERE
		linktoBusinessMasterId = @linktoBusinessMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessInfoAnswerTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessInfoAnswerTran_Insert]
	 @BusinessInfoAnswerTranId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@linktoBusinessInfoQuestionMasterId int
	,@linktoBusinessInfoAnswerMasterId int = NULL
	,@Answer varchar(50) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posBusinessInfoAnswerTran
	(
		 linktoBusinessMasterId
		,linktoBusinessInfoQuestionMasterId
		,linktoBusinessInfoAnswerMasterId
		,Answer
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@linktoBusinessInfoQuestionMasterId
		,@linktoBusinessInfoAnswerMasterId
		,@Answer
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @BusinessInfoAnswerTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessInfoAnswerTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessInfoAnswerTran_Update]
	 @BusinessInfoAnswerTranId int
	,@linktoBusinessMasterId smallint
	,@linktoBusinessInfoQuestionMasterId int
	,@linktoBusinessInfoAnswerMasterId int = NULL
	,@Answer varchar(50) = NULL

	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posBusinessInfoAnswerTran
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,linktoBusinessInfoQuestionMasterId = @linktoBusinessInfoQuestionMasterId
		,linktoBusinessInfoAnswerMasterId = @linktoBusinessInfoAnswerMasterId
		,Answer = @Answer
		
		
	WHERE
		BusinessInfoAnswerTranId = @BusinessInfoAnswerTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessInfoQuestionMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessInfoQuestionMaster_SelectAll]
	 @IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint
	,@linktoBusinessTypeMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		BIQM.*,BIAT.Answer
		,(SELECT BusinessType FROM posBusinessTypeMaster WHERE BusinessTypeMasterId = linktoBusinessTypeMasterId) AS BusinessType
	FROM
		posBusinessInfoQuestionMaster BIQM
	LEFT JOIN
		posBusinessInfoAnswerTran BIAT ON BIAT.linktoBusinessInfoQuestionMasterId = BIQM.BusinessInfoQuestionMasterId AND BIAT.linktoBusinessInfoAnswerMasterId IS NULL AND linktoBusinessMasterId=@linktoBusinessMasterId
	WHERE
		IsEnabled = ISNULL(@IsEnabled, IsEnabled)
		AND linktoBusinessTypeMasterId=@linktoBusinessTypeMasterId
	ORDER BY CASE WHEN SortOrder IS NULL THEN 1 ELSE 0 END, SortOrder

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBusinessMaster_Insert]
	 @BusinessMasterId smallint OUTPUT
	,@BusinessName varchar(80)
	,@BusinessShortName varchar(10) = NULL
	,@Address varchar(500)
	,@Phone1 varchar(15)
	,@Phone2 varchar(15) = NULL
	,@Email varchar(80) = NULL
	,@Fax varchar(15) = NULL
	,@Website varchar(80) = NULL
	,@linktoCountryMasterId smallint
	,@linktoStateMasterId smallint
	,@linktoCityMasterId int	
	,@ZipCode varchar(10)
	,@ImageName varchar(100) = NULL
	,@ExtraText varchar(100) = NULL
	,@TIN varchar(15) = NULL
	,@TINRegistrationDate date = NULL
	,@CST varchar(15) = NULL
	,@CSTRegistrationDate date = NULL
	,@PAN varchar(15) = NULL
	,@PANRegistrationDate date = NULL
	,@TDS varchar(15) = NULL
	,@TDSRegistrationDate date = NULL
	,@linktoBusinessTypeMasterId smallint
	,@UniqueId varchar(6)
	,@IsEnabled bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF	

	IF EXISTS (SELECT BusinessMasterId FROM posBusinessMaster WHERE BusinessName = @BusinessName OR UniqueId = @UniqueId)
	BEGIN
		SELECT @BusinessMasterId = BusinessMasterId FROM posBusinessMaster WHERE BusinessName = @BusinessName OR UniqueId = @UniqueId
		SET @Status = -2
		RETURN
	END

	INSERT INTO [dbo].[posBusinessMaster]
           ([BusinessName]
           ,[BusinessShortName]
           ,[Address]
           ,[Phone1]
           ,[Phone2]
           ,[Email]
           ,[Fax]
           ,[Website]
           ,[linktoCountryMasterId]
           ,[linktoStateMasterId]          
           ,[ZipCode]
           ,[ImageName]
           ,[ExtraText]
           ,[TIN]
           ,[TINRegistrationDate]
           ,[CST]
           ,[CSTRegistrationDate]
           ,[PAN]
           ,[PANRegistrationDate]
           ,[TDS]
           ,[TDSRegistrationDate]          
           ,[IsEnabled]
		   ,UniqueId
		   ,linktoBusinessTypeMasterId
		   ,linktoCityMasterId
		   
		   )
     VALUES           
	   (
			 @BusinessName
			,@BusinessShortName
			,@Address
			,@Phone1
			,@Phone2
			,@Email
			,@Fax
			,@Website
			,@linktoCountryMasterId
			,@linktoStateMasterId			
			,@ZipCode
			,@ImageName
			,@ExtraText
			,@TIN
			,@TINRegistrationDate
			,@CST
			,@CSTRegistrationDate
			,@PAN
			,@PANRegistrationDate
			,@TDS
			,@TDSRegistrationDate
			,@IsEnabled
			,@UniqueId
			,@linktoBusinessTypeMasterId
			,@linktoCityMasterId
		)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET	@BusinessMasterId=@@IDENTITY
		SET @Status = 0
		
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessMaster_Select]
	 @BusinessMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posBusinessMaster.*
		,(SELECT CountryName FROM posCountryMaster WHERE CountryMasterId = linktoCountryMasterId) AS Country
		,(SELECT StateName FROM posStateMaster WHERE StateMasterId = linktoStateMasterId) AS State
		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS City
		,(SELECT BusinessType FROM posBusinessTypeMaster WHERE BusinessTypeMasterId = linktoBusinessTypeMasterId) AS BusinessType
	FROM
		 posBusinessMaster
	WHERE
		BusinessMasterId = @BusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBusinessMaster_SelectAll]
	 @IsEnabled bit = NULL,
	 @BusinessMasterId smallint = NULL

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posBusinessMaster.*
		,(SELECT CountryName FROM posCountryMaster WHERE CountryMasterId = linktoCountryMasterId) AS Country		
		,(SELECT StateName FROM posStateMaster WHERE StateMasterId = linktoStateMasterId) AS State		
		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS City
		,(SELECT BusinessType FROM posBusinessTypeMaster WHERE BusinessTypeMasterId = linktoBusinessTypeMasterId) AS BusinessType
	FROM
		 posBusinessMaster
	WHERE
		((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND BusinessMasterId = ISNULL(@BusinessMasterId,BusinessMasterId)
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posBusinessMaster_Update]
	 @BusinessMasterId smallint
	,@BusinessShortName varchar(10) = NULL
	,@Address varchar(500)
	,@Phone1 varchar(15)
	,@Phone2 varchar(15) = NULL
	,@Email varchar(80) = NULL
	,@Fax varchar(15) = NULL
	,@Website varchar(80) = NULL
	,@ZipCode varchar(10)
	,@ImageName varchar(100) = NULL
	,@ExtraText varchar(100) = NULL
	,@TIN varchar(15) = NULL
	,@TINRegistrationDate date = NULL
	,@CST varchar(15) = NULL
	,@CSTRegistrationDate date = NULL
	,@PAN varchar(15) = NULL
	,@PANRegistrationDate date = NULL
	,@TDS varchar(15) = NULL
	,@TDSRegistrationDate date = NULL	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	
	UPDATE posBusinessMaster
	SET	
		 BusinessShortName = @BusinessShortName
		,Address = @Address
		,Phone1 = @Phone1
		,Phone2 = @Phone2
		,Email = @Email
		,Fax = @Fax
		,Website = @Website
		,ZipCode = @ZipCode
		,ImageName = @ImageName
		,ExtraText = @ExtraText
		,TIN = @TIN
		,TINRegistrationDate = @TINRegistrationDate
		,CST = @CST
		,CSTRegistrationDate = @CSTRegistrationDate
		,PAN = @PAN
		,PANRegistrationDate = @PANRegistrationDate
		,TDS = @TDS
		,TDSRegistrationDate = @TDSRegistrationDate
	WHERE
		BusinessMasterId = @BusinessMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessMasterByUniqueId_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessMasterByUniqueId_Select]
	 @UniqueId varchar(6)
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 BM.UniqueId
	FROM
		 posBusinessMaster BM
	WHERE
		BM.UniqueId=@UniqueId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posBusinessMasterDefaults_InsertAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posBusinessMasterDefaults_InsertAll]
	 @BusinessMasterId smallint
	,@UserMasterId smallint OUTPUT
	,@CountryMasterId smallint
	,@State varchar(50)
	,@City varchar(50)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

    DECLARE @RoleMasterId smallint
			,@Country varchar(50)
			,@StateMasterId smallint
			,@CityMasterId int

	SELECT @Country = CountryName FROM posCountryMaster WHERE CountryMasterId = @CountryMasterId

    INSERT INTO [dbo].[posRoleMaster] ([Role],[Description],[linktoBusinessMasterId],[IsEnabled],[IsDeleted],[CreateDateTime],[linktoUserMasterIdCreatedBy],[UpdateDateTime],[linktoUserMasterIdUpdatedBy]) VALUES ('Admin',NULL,@BusinessMasterId,1,0,GETDATE(),1,NULL,NULL);
    SET @RoleMasterId = @@IDENTITY

    INSERT INTO [dbo].[posUserMaster] ([Username],[Password],[linktoRoleMasterId],[CreateDateTime],[linktoUserMasterIdCreatedBy],[UpdateDateTime],[linktoUserMasterIdUpdatedBy],[LastLoginDateTime],[LoginFailCount],[LastLockoutDateTime],[LastPasswordChangedDateTime],[Comment],[linktoBusinessMasterId],[IsEnabled],[IsDeleted]) VALUES ('Admin','Admin',@RoleMasterId,GETDATE(),1,NULL,NULL,NULL,0,NULL,NULL,NULL,@BusinessMasterId,1,0);
    SET @UserMasterId = @@IDENTITY

	INSERT INTO [dbo].[posStateMaster] ([linktoBusinessMasterId],[StateName],[StateCode],[linktoCountryMasterId],[IsEnabled],[IsDeleted],[CreateDateTime],[linktoUserMasterIdCreatedBy],[UpdateDateTime],[linktoUserMasterIdUpdatedBy]) VALUES (@BusinessMasterId,@State,'',@CountryMasterId,1,0,GETDATE(),@UserMasterId,NULL,NULL);
	SET @StateMasterId = @@IDENTITY

	INSERT INTO [dbo].[posCityMaster] ([linktoBusinessMasterId],[CityName],[CityCode],[linktoStateMasterId],[IsEnabled],[IsDeleted],[CreateDateTime],[linktoUserMasterIdCreatedBy],[UpdateDateTime],[linktoUserMasterIdUpdatedBy]) VALUES (@BusinessMasterId,@City,'',@StateMasterId,1,0,GETDATE(),@UserMasterId,NULL,NULL);
	SET @CityMasterId = @@IDENTITY

	UPDATE posBusinessMaster SET linktoStateMasterId = @StateMasterId, linktoCityMasterId = @CityMasterId WHERE BusinessMasterId = @BusinessMasterId

    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (1.0000,@BusinessMasterId);
    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (2.0000,@BusinessMasterId);
    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (5.0000,@BusinessMasterId);
    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (10.0000,@BusinessMasterId);
    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (20.0000,@BusinessMasterId);
    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (50.0000,@BusinessMasterId);
    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (100.0000,@BusinessMasterId);
    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (500.0000,1);
    INSERT INTO [dbo].[posDenominationMaster] ([Denomination],[linktoBusinessMasterId]) VALUES (1000.0000,1);

    INSERT INTO [dbo].[posBusinessHoursTran] ([DayOfWeek],[OpeningTime],[ClosingTime],[BreakStartTime],[BreakEndTime],[linktoBusinessMasterId]) VALUES (0,'00:00:00','23:59:59',NULL,NULL,@BusinessMasterId);
    INSERT INTO [dbo].[posBusinessHoursTran] ([DayOfWeek],[OpeningTime],[ClosingTime],[BreakStartTime],[BreakEndTime],[linktoBusinessMasterId]) VALUES (1,'00:00:00','23:59:59',NULL,NULL,@BusinessMasterId);
    INSERT INTO [dbo].[posBusinessHoursTran] ([DayOfWeek],[OpeningTime],[ClosingTime],[BreakStartTime],[BreakEndTime],[linktoBusinessMasterId]) VALUES (2,'00:00:00','23:59:59',NULL,NULL,@BusinessMasterId);
    INSERT INTO [dbo].[posBusinessHoursTran] ([DayOfWeek],[OpeningTime],[ClosingTime],[BreakStartTime],[BreakEndTime],[linktoBusinessMasterId]) VALUES (3,'00:00:00','23:59:59',NULL,NULL,@BusinessMasterId);
    INSERT INTO [dbo].[posBusinessHoursTran] ([DayOfWeek],[OpeningTime],[ClosingTime],[BreakStartTime],[BreakEndTime],[linktoBusinessMasterId]) VALUES (4,'00:00:00','23:59:59',NULL,NULL,@BusinessMasterId);
    INSERT INTO [dbo].[posBusinessHoursTran] ([DayOfWeek],[OpeningTime],[ClosingTime],[BreakStartTime],[BreakEndTime],[linktoBusinessMasterId]) VALUES (5,'00:00:00','23:59:59',NULL,NULL,@BusinessMasterId);
    INSERT INTO [dbo].[posBusinessHoursTran] ([DayOfWeek],[OpeningTime],[ClosingTime],[BreakStartTime],[BreakEndTime],[linktoBusinessMasterId]) VALUES (6,'00:00:00','23:59:59',NULL,NULL,@BusinessMasterId);

    INSERT INTO [dbo].[posBusinessDescription] ([Title],[Description],[linktoBusinessMasterId],[IsDefault],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('About Us','',@BusinessMasterId,1,@UserMasterId,GETDATE(),NULL,NULL);
    INSERT INTO [dbo].[posBusinessDescription] ([Title],[Description],[linktoBusinessMasterId],[IsDefault],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('Privacy Policy','',@BusinessMasterId,1,@UserMasterId,GETDATE(),NULL,NULL);
    INSERT INTO [dbo].[posBusinessDescription] ([Title],[Description],[linktoBusinessMasterId],[IsDefault],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('Terms of Service','',@BusinessMasterId,1,@UserMasterId,GETDATE(),NULL,NULL);

    INSERT INTO [dbo].[posRateCaptionMaster] ([RateName],[RateCaption],[linktoBusinessMasterId],[RateIndex],[IsEnabled]) VALUES ('Rate 1','Rate 1',@BusinessMasterId,1,0);
    INSERT INTO [dbo].[posRateCaptionMaster] ([RateName],[RateCaption],[linktoBusinessMasterId],[RateIndex],[IsEnabled]) VALUES ('Rate 2','Rate 2',@BusinessMasterId,2,0);
    INSERT INTO [dbo].[posRateCaptionMaster] ([RateName],[RateCaption],[linktoBusinessMasterId],[RateIndex],[IsEnabled]) VALUES ('Rate 3','Rate 3',@BusinessMasterId,3,0);
    INSERT INTO [dbo].[posRateCaptionMaster] ([RateName],[RateCaption],[linktoBusinessMasterId],[RateIndex],[IsEnabled]) VALUES ('Rate 4','Rate 4',@BusinessMasterId,4,0);
    INSERT INTO [dbo].[posRateCaptionMaster] ([RateName],[RateCaption],[linktoBusinessMasterId],[RateIndex],[IsEnabled]) VALUES ('Rate 5','Rate 5',@BusinessMasterId,5,0);

    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (1,'Theme','Office 2010 - Black','Office 2010 - Black',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (2,'Show Confirmation on Exit','True','True',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (3,'Is Automatic Backup','True','True',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (4,'Backup Path','D:\BackUp','',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (5,'Backup Path 2','E:\','',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (6,'Allow Blank Password','False','False',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (7,'Server User Name','','',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (8,'Server Password','','',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (9,'Server Path','','',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (10,'Default Country',@Country,@Country,@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (11,'Default State',@State,@State,@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (12,'Default City',@City,@City,@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (13,'Short Date','MM/dd/yyyy','MM/dd/yyyy',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (14,'Long Date','dd MMMM, yyyy','dd MMMM, yyyy',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (15,'Short Time','h:mm tt','h:mm tt',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (16,'Long Time','hh:mm:ss tt','hh:mm:ss tt',@BusinessMasterId);
    INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (17,'Auto Correct User Inputs','Title Case','Title Case',@BusinessMasterId);
	INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (18,'Currency','','',@BusinessMasterId);
	INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (19,'Same Tax for all Items','True','True',@BusinessMasterId);
	INSERT INTO [dbo].[posSettingMaster] ([SettingMasterId],[Setting],[Value],[DefaultValue],[linktoBusinessMasterId]) VALUES (20,'Apply Tax after Discount','True','True',@BusinessMasterId);

    INSERT INTO [dbo].[posPaymentTypeMaster] ([ShortName],[PaymentType],[Description],[linktoPaymentTypeCategoryMasterId],[IsDefault],[IsEnabled],[linktoBusinessMasterId],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('CASH','Cash',NULL,1,1,1,@BusinessMasterId,@UserMasterId,GETDATE(),NULL,NULL);
    INSERT INTO [dbo].[posPaymentTypeMaster] ([ShortName],[PaymentType],[Description],[linktoPaymentTypeCategoryMasterId],[IsDefault],[IsEnabled],[linktoBusinessMasterId],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('CHEQUE','Cheque',NULL,2,1,1,@BusinessMasterId,@UserMasterId,GETDATE(),NULL,NULL);
    INSERT INTO [dbo].[posPaymentTypeMaster] ([ShortName],[PaymentType],[Description],[linktoPaymentTypeCategoryMasterId],[IsDefault],[IsEnabled],[linktoBusinessMasterId],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('BD','Bed Debts',NULL,5,1,1,@BusinessMasterId,@UserMasterId,GETDATE(),NULL,NULL);
    INSERT INTO [dbo].[posPaymentTypeMaster] ([ShortName],[PaymentType],[Description],[linktoPaymentTypeCategoryMasterId],[IsDefault],[IsEnabled],[linktoBusinessMasterId],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('COMP','Complementary',NULL,4,1,1,@BusinessMasterId,@UserMasterId,GETDATE(),NULL,NULL);
    INSERT INTO [dbo].[posPaymentTypeMaster] ([ShortName],[PaymentType],[Description],[linktoPaymentTypeCategoryMasterId],[IsDefault],[IsEnabled],[linktoBusinessMasterId],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('VISA','Visa',NULL,3,1,1,@BusinessMasterId,@UserMasterId,GETDATE(),NULL,NULL);
    INSERT INTO [dbo].[posPaymentTypeMaster] ([ShortName],[PaymentType],[Description],[linktoPaymentTypeCategoryMasterId],[IsDefault],[IsEnabled],[linktoBusinessMasterId],[linktoUserMasterIdCreatedBy],[CreateDateTime],[linktoUserMasterIdUpdatedBy],[UpdateDateTime]) VALUES ('MC','Master Card',NULL,3,1,1,@BusinessMasterId,@UserMasterId,GETDATE(),NULL,NULL);

    IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END









GO
/****** Object:  StoredProcedure [dbo].[posBusinessTypeMasterBusinessType_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--CREATE PROCEDURE posBusinessTypeMaster_SelectAll
--AS
--BEGIN

--	SET NOCOUNT ON

--	SELECT
--		 posBusinessTypeMaster.*
--	FROM
--		 posBusinessTypeMaster
	
--	ORDER BY BusinessType

--	RETURN
--END
--GO

CREATE PROCEDURE [dbo].[posBusinessTypeMasterBusinessType_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 BusinessTypeMasterId
		,BusinessType
	FROM
		 posBusinessTypeMaster
	ORDER BY BusinessType

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCashBookMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCashBookMaster_DeleteAll]
	 @CashBookMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE	
		 posCashBookMaster
	SET
		IsDeleted=1
	WHERE
		CashBookMasterId IN (SELECT * from dbo.Parse(@CashBookMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCashBookMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCashBookMaster_Insert]
	 @CashBookMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@linktoAccountMasterIdCash int
	,@CashBookNumber varchar(10)
	,@CashBookDate date
	,@VoucherNumber varchar(20)
	,@IsPaid bit
	,@linktoAccountMasterId int
	,@Amount money
	,@Remark varchar(500) = NULL
	,@CreateDateTime datetime
	,@IsDeleted bit=0
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posCashBookMaster
	(
		 linktoBusinessMasterId
		,linktoAccountMasterIdCash
		,CashBookNumber
		,CashBookDate
		,VoucherNumber
		,IsPaid
		,linktoAccountMasterId
		,Amount
		,Remark
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,IsDeleted
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@linktoAccountMasterIdCash
		,@CashBookNumber
		,@CashBookDate
		,@VoucherNumber
		,@IsPaid
		,@linktoAccountMasterId
		,@Amount
		,@Remark
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@IsDeleted
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CashBookMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCashBookMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCashBookMaster_Select]
	 @CashBookMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posCashBookMaster.*
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterIdCash) AS AccountCash
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterId) AS Account
		,(SELECT linktoAccountCategoryMasterId  FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterIdCash) AS AccountCashMasterId
		,(SELECT linktoAccountCategoryMasterId FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterId) AS AccountMasterId
	FROM
		 posCashBookMaster
	WHERE
		CashBookMasterId = @CashBookMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCashBookMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCashBookMaster_SelectAll]
	 @linktoAccountMasterIdCash int = NULL
	,@CashBookDate date = NULL
	,@VoucherNumber varchar(20)
	,@IsPaid bit = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posCashBookMaster.*
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterIdCash) AS AccountCash		
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterId) AS Account
	FROM
		 posCashBookMaster
	WHERE
		linktoAccountMasterIdCash = ISNULL(@linktoAccountMasterIdCash, linktoAccountMasterIdCash)
		AND CONVERT(varchar(8), CashBookDate, 112) = CONVERT(varchar(8), @CashBookDate, 112)
		AND VoucherNumber LIKE @VoucherNumber + '%'
		AND ((@IsPaid IS NULL AND (IsPaid IS NULL OR IsPaid IS NOT NULL)) OR (@IsPaid IS NOT NULL AND IsPaid = @IsPaid))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
		AND IsDeleted=0
	ORDER BY CashBookMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCashBookMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCashBookMaster_Update]
	 @CashBookMasterId int
	,@linktoBusinessMasterId smallint
	,@linktoAccountMasterIdCash int
	,@CashBookNumber varchar(10)
	,@CashBookDate date
	,@VoucherNumber varchar(20)
	,@IsPaid bit
	,@linktoAccountMasterId int
	,@Amount money
	,@Remark varchar(500) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posCashBookMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,linktoAccountMasterIdCash = @linktoAccountMasterIdCash
		,CashBookNumber = @CashBookNumber
		,CashBookDate = @CashBookDate
		,VoucherNumber = @VoucherNumber
		,IsPaid = @IsPaid
		,linktoAccountMasterId = @linktoAccountMasterId
		,Amount = @Amount
		,Remark = @Remark
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		CashBookMasterId = @CashBookMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCashBookMasterBankBookMasterForLedger_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCashBookMasterBankBookMasterForLedger_SelectAll]
	@linktoAccountMasterId int
	,@FromDate date
	,@ToDate date
	,@linktoBusinessMasterId smallint
AS
BEGIN
	SELECT * FROM
	(
		SELECT 
			CashBookDate,VoucherNumber,linktoAccountMasterId,AccountName,CASE WHEN IsPaid = 1 THEN Amount ELSE 0 END DebitAmount,CASE WHEN IsPaid = 0 THEN Amount ELSE 0 END CreditAmount,
			CM.linktoBusinessMasterId 
		FROM 
			posCashBookMaster CM
		JOIN
			posAccountMaster AM ON AM.AccountMasterId = CM.linktoAccountMasterId		
		WHERE CM.linktoAccountMasterIdCash = @linktoAccountMasterId AND CM.IsDeleted=0
	UNION ALL
		SELECT 
			BankBookDate,VoucherNumber,linktoAccountMasterId,AccountName,CASE WHEN IsPaid = 1 THEN Amount ELSE 0 END DebitAmount,CASE WHEN IsPaid = 0 THEN Amount ELSE 0 END CreditAmount,
			BM.linktoBusinessMasterId  
		FROM 
			posBankBookMaster BM
		JOIN
			posAccountMaster AM ON AM.AccountMasterId = BM.linktoAccountMasterId
		WHERE BM.linktoAccountMasterIdBank = @linktoAccountMasterId AND BM.IsDeleted=0

	)TempTable
	WHERE Convert(varchar(8),CashBookDate,112) BETWEEN  Convert(varchar(8),@FromDate,112) AND Convert(varchar(8),@ToDate,112)
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY CashBookDate,linktoAccountMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posCashBookMasterMaxCashBookNumber_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCashBookMasterMaxCashBookNumber_Select] 
	@linktoBusinessMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		MAX(Convert(int,CashBookMasterId))+ 1 AS CashBookNumber
	FROM
		 posCashBookMaster 
	WHERE 
		linktoBusinessMasterId = @linktoBusinessMasterId
	RETURN
END









GO
/****** Object:  StoredProcedure [dbo].[posCategoryMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCategoryMaster_DeleteAll  '3',0
CREATE PROCEDURE [dbo].[posCategoryMaster_DeleteAll]
	 @CategoryMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int
	 DECLARE @RowCount int
	set @TotalRecords=(SELECT COUNT(*) FROM dbo.Parse(@CategoryMasterIds, ','))

	UPDATE
		posCategoryMaster
	SET IsDeleted = 1
	WHERE
		CategoryMasterId IN (SELECT * from dbo.Parse(@CategoryMasterIds, ','))
		AND CategoryMasterId NOT IN 
		(
			SELECT linktoCategoryMasterId FROM posItemMaster WHERE IsDeleted = 0 AND IsEnabled = 1 
			AND linktoCategoryMasterId IN (SELECT * FROM dbo.Parse(@CategoryMasterIds, ','))
		)

		SET @RowCount=@@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
			if @TotalRecords = @RowCount
				BEGIN
					SET @Status = 0
				END
				ELSE
				BEGIN
					SET @Status = -2
				END
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCategoryMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCategoryMaster_Insert]
	 @CategoryMasterId smallint OUTPUT
	,@CategoryName varchar(80)
	,@linktoCategoryMasterIdParent smallint = NULL
	,@ImageName varchar(100) = NULL
	,@CategoryColor varchar(6) = NULL
	,@Description varchar(500) = NULL
	,@linktoBusinessMasterId smallint
	,@SortOrder smallint = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@IsRawMaterial bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CategoryMasterId FROM posCategoryMaster WHERE CategoryName = @CategoryName AND IsRawMaterial = @IsRawMaterial AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @CategoryMasterId = CategoryMasterId FROM posCategoryMaster WHERE CategoryName = @CategoryName AND IsRawMaterial = @IsRawMaterial   AND IsDeleted = 0 
				 AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posCategoryMaster
	(
		 CategoryName
		,linktoCategoryMasterIdParent
		,ImageName
		,CategoryColor
		,Description
		,linktoBusinessMasterId
		,SortOrder
		,IsEnabled
		,IsRawMaterial
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @CategoryName
		,@linktoCategoryMasterIdParent
		,@ImageName
		,@CategoryColor
		,@Description
		,@linktoBusinessMasterId
		,@SortOrder
		,@IsEnabled
		,@IsRawMaterial
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CategoryMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCategoryMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCategoryMaster_Select]
	 @CategoryMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posCategoryMaster.*
	FROM
		 posCategoryMaster
	WHERE
		CategoryMasterId = @CategoryMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCategoryMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCategoryMaster_SelectAll]
	 @CategoryName varchar(80)= NULL
	,@IsEnabled bit = NULL
	,@IsRawMaterial bit = NULL
	,@linktoBusinessMasterId smallint
	,@IsHiddenForCustomer bit = NULL
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posCategoryMaster.*
	FROM
		 posCategoryMaster
	WHERE
		CategoryName LIKE   ISNULL(@CategoryName,'') + '%'
		AND ((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND IsDeleted = 0
		AND ((@IsRawMaterial IS NULL AND (IsRawMaterial IS NULL OR IsRawMaterial IS NOT NULL)) OR (@IsRawMaterial IS NOT NULL AND IsRawMaterial = @IsRawMaterial))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
		AND ((@IsHiddenForCustomer = 0) OR (@IsHiddenForCustomer = 1 AND IsHiddenForCustomer <> @IsHiddenForCustomer))
	ORDER BY CASE WHEN SortOrder IS NULL THEN 1 ELSE 0 END, SortOrder

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCategoryMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCategoryMaster_Update]
	 @CategoryMasterId smallint
	,@CategoryName varchar(80)
	,@linktoCategoryMasterIdParent smallint = NULL
	,@ImageName varchar(100) = NULL
	,@CategoryColor varchar(6) = NULL
	,@Description varchar(500) = NULL
	,@linktoBusinessMasterId smallint
	,@SortOrder smallint = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@IsRawMaterial bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CategoryMasterId FROM posCategoryMaster WHERE CategoryName = @CategoryName AND CategoryMasterId != @CategoryMasterId AND IsRawMaterial = @IsRawMaterial AND IsDeleted = 0 AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posCategoryMaster
	SET
		 CategoryName = @CategoryName
		,linktoCategoryMasterIdParent = @linktoCategoryMasterIdParent
		,ImageName = @ImageName
		,CategoryColor = @CategoryColor
		,Description = @Description
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,SortOrder = @SortOrder
		,IsEnabled = @IsEnabled
		,IsRawMaterial = @IsRawMaterial 
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		CategoryMasterId = @CategoryMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCategoryMasterByMostSellingCategory_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCategoryMasterByMostSellingCategory_SelectAll]
	-- Add the parameters for the stored procedure here
	 @FromDate datetime
	,@ToDate datetime
	,@linktoBusinessMasterId smallint
AS
BEGIN
	
	SET NOCOUNT ON;

    SELECT 
	CM.CategoryMasterId,CM.CategoryName,COUNT(linktoCategoryMasterId) AS SellingCount 
	FROM 
		posCategoryMaster CM
	LEFT JOIN
		(
			SELECT DISTINCT 
				SIM.linktoCategoryMasterId,SIM.ItemMasterId
			FROM 
				posSalesItemTran SIT 
			JOIN  
				posSalesMaster SM ON SM.SalesMasterId = SIT.linktoSalesMasterId 
					AND Convert(varchar(8),BillDateTime,112) BETWEEN Convert(varchar(8),@FromDate,112) AND Convert(varchar(8),@ToDate,112)
			JOIN
				posItemMaster SIM ON SIM.ItemMasterId = SIT.linktoItemMasterId
		) Temp ON linktoCategoryMasterId = CM.CategoryMasterId
	WHERE 
		CM.linktoBusinessMasterId = @linktoBusinessMasterId 
		AND CM.IsEnabled = 1 AND CM.IsDeleted = 0 AND CM.IsRawMaterial = 0
	GROUP BY CM.CategoryMasterId,CM.CategoryName
	ORDER BY SellingCount DESC
END



GO
/****** Object:  StoredProcedure [dbo].[posCategoryMasterCategoryName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCategoryMasterCategoryName_SelectAll]
	@IsRowMaterial bit ,
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 CategoryMasterId
		,CategoryName
	FROM
		 posCategoryMaster
	WHERE
		IsRawMaterial= @IsRowMaterial 
		AND IsEnabled = 1
		AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
		
	ORDER BY CategoryName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCityMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCityMaster_DeleteAll]
	 @CityMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	Declare @TotalRecords int,@RawCount int
	SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@CityMasterIds, ','))
	UPDATE
		posCityMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		CityMasterId IN (SELECT * from dbo.Parse(@CityMasterIds, ','))
		AND CityMasterId NOT IN
		(
			SELECT linktoCityMasterId FROM posAreaMaster WHERE linktoCityMasterId IN (SELECT * from dbo.Parse(@CityMasterIds, ',')) AND IsDeleted=0
		)
		SET @RawCount = @@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		IF @TotalRecords = @RawCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCityMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCityMaster_Insert]
	 @CityMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@CityName varchar(50)
	,@CityCode varchar(3)
	,@linktoStateMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CityMasterId FROM posCityMaster WHERE CityName = @CityName AND IsDeleted = 0)
	BEGIN
		SELECT @CityMasterId = CityMasterId FROM posCityMaster WHERE CityName = @CityName
		SET @Status = -2
		RETURN
	END
	INSERT INTO posCityMaster
	(
		 linktoBusinessMasterId
		,CityName
		,CityCode
		,linktoStateMasterId
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@CityName
		,@CityCode
		,@linktoStateMasterId
		,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CityMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCityMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCityMaster_Select]
		@CityMasterId int
	 AS
BEGIN

	SET NOCOUNT ON 

	SELECT   
		posCityMaster.*
		,StateName
		,CountryName
		,linktoCountryMasterId
	FROM 
		posCityMaster, posStateMaster, posCountryMaster
	WHERE 
		CityMasterId = @CityMasterId
		AND StateMasterId = linktoStateMasterId
		AND CountryMasterId = linktoCountryMasterId
 
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCityMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCityMaster_SelectAll]
	@IsEnabled bit,
	@linktoCountryMasterId smallint = NULL,
	@linktoStateMasterId smallint = NULL,
	@linktoBusinessMasterId smallint	
	 
AS
BEGIN

	SET NOCOUNT ON 

	SELECT   
			posCityMaster.*
			,StateName
			,CountryName
		FROM 
			posCityMaster, posStateMaster, posCountryMaster
		WHERE 
			posCityMaster.IsEnabled = @IsEnabled 
			AND posCityMaster.IsDeleted = 0	
			AND StateMasterId = linktoStateMasterId
			AND CountryMasterId = linktoCountryMasterId
			AND posCityMaster.linktoStateMasterId = ISNULL(@linktoStateMasterId,linktoStateMasterId)
			AND linktoCountryMasterId = ISNULL(@linktoCountryMasterId,linktoCountryMasterId)
			ANd posCityMaster.linktoBusinessMasterId=@linktoBusinessMasterId
		ORDER BY CityName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCityMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCityMaster_Update]
	 @CityMasterId int
	,@CityName varchar(50)
	,@CityCode varchar(3)
	,@linktoStateMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CityMasterId FROM posCityMaster WHERE CityName = @CityName AND CityMasterId != @CityMasterId AND IsDeleted = 0)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posCityMaster
	SET
		 CityName = @CityName
		,CityCode = @CityCode
		,linktoStateMasterId = @linktoStateMasterId
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		CityMasterId = @CityMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCityMasterCityName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCityMasterCityName_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 CityMasterId
		,CityName
	FROM
		posCityMaster
	WHERE
		IsEnabled = 1
		AND IsDeleted=0
	ORDER BY CityName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCityMasterStatewise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCityMasterStatewise_SelectAll]
			 
			 @linktoStateMasterId smallint
	 AS
BEGIN

	SET NOCOUNT ON 

	SELECT   
			posCityMaster.*
			,(SELECT StateName FROM posStateMaster WHERE StateMasterId = linktoStateMasterId) AS StateName
		FROM 
			posCityMaster 
		WHERE 
			 
			linktoStateMasterId = @linktoStateMasterId  
			AND IsEnabled = 1
			AND IsDeleted=0
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenDescriptionTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenDescriptionTran_Delete]
	 @CodeGenDescriptionTranId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posCodeGenDescriptionTran
	WHERE
		CodeGenDescriptionTranId = @CodeGenDescriptionTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenDescriptionTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCodeGenDescriptionTran_Insert]
	 @CodeGenDescriptionTranId int OUTPUT
	,@Description varchar(50)
	,@linktoCodeGenMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posCodeGenDescriptionTran
	(
		 Description
		,linktoCodeGenMasterId
	)
	VALUES
	(
		 @Description
		,@linktoCodeGenMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CodeGenDescriptionTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenDescriptionTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenDescriptionTran_Select]
	 @CodeGenDescriptionTranId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posCodeGenDescriptionTran.*
	FROM
		 posCodeGenDescriptionTran
	WHERE
		CodeGenDescriptionTranId = @CodeGenDescriptionTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenDescriptionTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenDescriptionTran_SelectAll]
	 @linktoCodeGenMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posCodeGenDescriptionTran.*
	FROM
		 posCodeGenDescriptionTran
	WHERE
		linktoCodeGenMasterId = @linktoCodeGenMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenDescriptionTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenDescriptionTran_Update]
	 @CodeGenDescriptionTranId int
	,@Description varchar(50)
	,@linktoCodeGenMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posCodeGenDescriptionTran
	SET
		 Description = @Description
		,linktoCodeGenMasterId = @linktoCodeGenMasterId
	WHERE
		CodeGenDescriptionTranId = @CodeGenDescriptionTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenMaster_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenMaster_Delete]
	 @CodeGenMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posCodeGenMaster
	WHERE
		CodeGenMasterId = @CodeGenMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCodeGenMaster_Insert]
	 @CodeGenMasterId smallint OUTPUT
	,@CodeGen varchar(50)
	,@Description varchar(500) = NULL
	,@CodeGenDate date = NULL
	,@CodeGenTime time
	,@Salary numeric(10, 2)
	,@Amount money = NULL
	,@ImageName varchar(50) = NULL
	,@Color varchar(6) = NULL
	,@StarRating smallint = NULL
	,@linktoCodeGenUserMasterIdCreatedBy int
	,@CreateDateTime datetime
	,@IsEnabled bit
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CodeGenMasterId FROM posCodeGenMaster WHERE CodeGen = @CodeGen)
	BEGIN
		SELECT @CodeGenMasterId = CodeGenMasterId FROM posCodeGenMaster WHERE CodeGen = @CodeGen
		SET @Status = -2
		RETURN
	END
	INSERT INTO posCodeGenMaster
	(
		 CodeGen
		,Description
		,CodeGenDate
		,CodeGenTime
		,Salary
		,Amount
		,ImageName
		,Color
		,StarRating
		,linktoCodeGenUserMasterIdCreatedBy
		,CreateDateTime
		,IsEnabled
		
	)
	VALUES
	(
		 @CodeGen
		,@Description
		,@CodeGenDate
		,@CodeGenTime
		,@Salary
		,@Amount
		,@ImageName
		,@Color
		,@StarRating
		,@linktoCodeGenUserMasterIdCreatedBy
		,@CreateDateTime
		,@IsEnabled
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CodeGenMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenMaster_Select]
	 @CodeGenMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posCodeGenMaster.*
	FROM
		 posCodeGenMaster
	WHERE
		CodeGenMasterId = @CodeGenMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenMaster_SelectAll]
	 @CodeGen varchar(50)
	,@CodeGenDate date = NULL
	,@StarRating smallint = NULL
	,@IsEnabled bit = NULL

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posCodeGenMaster.*
	FROM
		 posCodeGenMaster
	WHERE
		CodeGen LIKE @CodeGen + '%'
		AND ((@CodeGenDate IS NULL AND (CodeGenDate IS NULL OR CodeGenDate IS NOT NULL)) OR (@CodeGenDate IS NOT NULL AND CONVERT(varchar(8), CodeGenDate, 112) = CONVERT(varchar(8), @CodeGenDate, 112)))
		AND ((@StarRating IS NULL AND (StarRating IS NULL OR StarRating IS NOT NULL)) OR (@StarRating IS NOT NULL AND StarRating = @StarRating))
		AND IsEnabled = ISNULL(@IsEnabled, IsEnabled)

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenMaster_Update]
	 @CodeGenMasterId smallint
	,@CodeGen varchar(50)
	,@Description varchar(500) = NULL
	,@CodeGenDate date = NULL
	,@CodeGenTime time
	,@Salary numeric(10, 2)
	,@Amount money = NULL
	,@ImageName varchar(50) = NULL
	,@Color varchar(6) = NULL
	,@StarRating smallint = NULL
	,@linktoCodeGenUserMasterIdUpdatedBy int = NULL
	,@UpdateDateTime datetime = NULL
	,@IsEnabled bit
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CodeGenMasterId FROM posCodeGenMaster WHERE CodeGen = @CodeGen AND CodeGenMasterId != @CodeGenMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posCodeGenMaster
	SET
		 CodeGen = @CodeGen
		,Description = @Description
		,CodeGenDate = @CodeGenDate
		,CodeGenTime = @CodeGenTime
		,Salary = @Salary
		,Amount = @Amount
		,ImageName = @ImageName
		,Color = @Color
		,StarRating = @StarRating
		,linktoCodeGenUserMasterIdUpdatedBy = @linktoCodeGenUserMasterIdUpdatedBy
		,UpdateDateTime = @UpdateDateTime
		,IsEnabled = @IsEnabled
		
	WHERE
		CodeGenMasterId = @CodeGenMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCodeGenMasterCodeGen_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCodeGenMasterCodeGen_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 CodeGenMasterId
		,CodeGen
	FROM
		 posCodeGenMaster
	WHERE
		IsEnabled = 1
	ORDER BY CodeGen

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterDayEndTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterDayEndTran_Insert]
	 @CounterDayEndTranId int OUTPUT
	,@linktoCounterMasterId smallint
	,@DayEndDateTime datetime
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posCounterDayEndTran
	(
		 linktoCounterMasterId
		,DayEndDateTime
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
	)
	VALUES
	(
		 @linktoCounterMasterId
		,@DayEndDateTime
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CounterDayEndTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterDayEndTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--  posCounterDayEndTran_Select 1
CREATE PROCEDURE [dbo].[posCounterDayEndTran_Select]
	 @linktoCounterMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT TOP 1
		 posCounterDayEndTran.*
	FROM
		 posCounterDayEndTran
	WHERE
		linktoCounterMasterId = @linktoCounterMasterId
	ORDER BY CounterDayEndTranId DESC

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterDayEndTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterDayEndTran_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posCounterDayEndTran.*
		,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS Counter		
		,(SELECT Username FROM posUserMaster WHERE UserMasterId = linktoUserMasterIdCreatedBy) AS UserCreatedBy
	FROM
		 posCounterDayEndTran
	
	ORDER BY CounterDayEndTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterDayEndTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterDayEndTran_Update]
	 @CounterDayEndTranId int
	,@linktoCounterMasterId smallint
	,@DayEndDateTime datetime
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posCounterDayEndTran
	SET
		 linktoCounterMasterId = @linktoCounterMasterId
		,DayEndDateTime = @DayEndDateTime
		
	WHERE
		CounterDayEndTranId = @CounterDayEndTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemRateTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCounterItemRateTran_Delete]
	 @CounterItemRateTranId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posCounterItemRateTran
	WHERE
		CounterItemRateTranId = @CounterItemRateTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END

GO
/****** Object:  StoredProcedure [dbo].[posCounterItemRateTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterItemRateTran_Insert]
	 @CounterItemRateTranId smallint OUTPUT
	,@linktoCounterMasterId smallint	
	,@DineInRateIndex smallint = NULL
	,@TakeAwayRateIndex smallint = NULL
	,@HomeDeliveryRateIndex smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	IF EXISTS(SELECT CounterItemRateTranId FROM posCounterItemRateTran WHERE linktoCounterMasterId = @linktoCounterMasterId)
	BEGIN
		SELECT @CounterItemRateTranId = CounterItemRateTranId FROM posCounterItemRateTran WHERE linktoCounterMasterId = @linktoCounterMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posCounterItemRateTran
	(
		 linktoCounterMasterId		
		,DineInRateIndex
		,TakeAwayRateIndex
		,HomeDeliveryRateIndex
	)
	VALUES
	(
		 @linktoCounterMasterId	
		,@DineInRateIndex
		,@TakeAwayRateIndex
		,@HomeDeliveryRateIndex
	)
	
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CounterItemRateTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemRateTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posCounterItemRateTran_Select 1,1,1
CREATE PROCEDURE [dbo].[posCounterItemRateTran_Select]
	@linktoCounterMasterId smallint,
	@linktoOrderTypeMasterId smallint,
	@linktoBusinessMasterId smallint
AS
BEGIN
	SELECT 
		ItemRateIndex,
		(SELECT CASE WHEN RateCaption IS NULL OR RateCaption = '' THEN RateName ELSE RateCaption END  
			FROM posRateCaptionMaster RCM WHERE RCM.RateIndex = ItemRateIndex AND linktoBusinessMasterId = @linktoBusinessMasterId AND IsEnabled = 1) As RateName
	FROM
	(
		SELECT 
			CASE WHEN @linktoOrderTypeMasterId = 1 THEN DineInRateIndex ELSE
			CASE WHEN @linktoOrderTypeMasterId = 2 THEN TakeAwayRateIndex ELSE
			HomeDeliveryRateIndex END END As ItemRateIndex
		FROM
			posCounterItemRateTran 
		WHERE 
			linktoCounterMasterId = @linktoCounterMasterId
	)Temp
	END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemRateTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCounterItemRateTran_SelectAll 2
CREATE PROCEDURE [dbo].[posCounterItemRateTran_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		CIT.*,
		CM.CounterName,
		(SELECT CASE WHEN RateCaption = '' THEN RateName ELSE RateCaption END 
			FROM posRateCaptionMaster WHERE DineInRateIndex = RateIndex AND linktoBusinessMasterId = @linktoBusinessMasterId) AS DineInRateName,
		(SELECT CASE WHEN RateCaption = '' THEN RateName ELSE RateCaption END 
			FROM posRateCaptionMaster WHERE TakeAwayRateIndex = RateIndex AND linktoBusinessMasterId = @linktoBusinessMasterId) AS TakeAwayRateName,
		(SELECT CASE WHEN RateCaption = '' THEN RateName ELSE RateCaption END 
			FROM posRateCaptionMaster WHERE HomeDeliveryRateIndex = RateIndex AND linktoBusinessMasterId = @linktoBusinessMasterId) AS HomeDeliveryRateName
	FROM 
		posCounterItemRateTran CIT,posCounterMaster CM
	WHERE 
		CM.linktoBusinessMasterId = @linktoBusinessMasterId AND CIT.linktoCounterMasterId = CM.CounterMasterId
	
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemRateTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterItemRateTran_Update]
	 @CounterItemRateTranId smallint
	,@linktoCounterMasterId smallint	
	,@DineInRateIndex smallint = NULL
	,@TakeAwayRateIndex smallint = NULL
	,@HomeDeliveryRateIndex smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF


	IF EXISTS(SELECT CounterItemRateTranId FROM posCounterItemRateTran WHERE linktoCounterMasterId = @linktoCounterMasterId
						AND CounterItemRateTranId != @CounterItemRateTranId)
	BEGIN
		SELECT @CounterItemRateTranId = CounterItemRateTranId FROM posCounterItemRateTran WHERE linktoCounterMasterId = @linktoCounterMasterId
				AND CounterItemRateTranId != @CounterItemRateTranId
		SET @Status = -2
		RETURN
	END

	UPDATE posCounterItemRateTran
	SET
		 linktoCounterMasterId = @linktoCounterMasterId		
		,DineInRateIndex = @DineInRateIndex
		,TakeAwayRateIndex = @TakeAwayRateIndex
		,HomeDeliveryRateIndex = @HomeDeliveryRateIndex
	WHERE
		CounterItemRateTranId = @CounterItemRateTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterItemTran_Insert]
	 @linktoCounterMasterId smallint 
	,@linktoItemMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF NOT EXISTS(SELECT CounterItemTranId FROM posCounterItemTran WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoItemMasterId = @linktoItemMasterId)
	BEGIN
		INSERT INTO posCounterItemTran 
		(
			 linktoCounterMasterId
			,linktoItemMasterId
		)
		SELECT
			 @linktoCounterMasterId,
			 @linktoItemMasterId  
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemTran_InsertAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterItemTran_InsertAll]
	
	 @linktoCounterMasterId smallint 
	 ,@linktoItemMasterIds varchar(1000) = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		 posCounterItemTran
	WHERE
		linktoCounterMasterId =@linktoCounterMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0

		IF @linktoItemMasterIds IS NOT NULL 
		BEGIN
			INSERT INTO posCounterItemTran 
			(
				linktoCounterMasterId
				,linktoItemMasterId
			)
			SELECT
				@linktoCounterMasterId
				,parsevalue 
			FROM
				dbo.parse(@LinktoItemMasterIds,',')
		END
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterItemTran_SelectAll]
	@linktoCounterMasterId smallint,
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT DISTINCT
		 Category,ItemName,ItemCode,ItemMasterId,CounterItemTranId,linktoCounterMasterId,linktoItemMasterId,ItemType
	FROM
	(
		SELECT DISTINCT
	
		CM.CategoryName  AS Category,
		IM.ItemName,IM.ItemCode,IM.ItemMasterId,IM.ItemType, CIT.*

		FROM			
			posItemMaster IM 	
		JOIN	posCategoryMaster CM ON  CM.CategoryMasterId = IM.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
		LEFT JOIN	
			posCounterItemTran CIT  ON ItemMasterId = CIT.linktoItemMasterId AND CIT.linktoCounterMasterId = @linktoCounterMasterId
		WHERE 
			IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
			IM.ItemType IN (0,3)  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
	UNION ALL
		SELECT DISTINCT
	
				CM.CategoryName  AS Category,
				IM.ItemName,IM.ItemCode,IM.ItemMasterId,IM.ItemType, CIT.*

			FROM			
				posItemMaster IM 
			JOIN 
					posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
			JOIN	posCategoryMaster CM ON  CM.CategoryMasterId = ICT.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
			LEFT JOIN	
				posCounterItemTran CIT  ON ItemMasterId = CIT.linktoItemMasterId AND CIT.linktoCounterMasterId = @linktoCounterMasterId
			WHERE 
				IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
				IM.ItemType IN (0,3) AND CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
	)TempTable
	ORDER BY Category


	--SELECT DISTINCT
	--(SELECT CM.CategoryName FROM posCategoryMaster CM WHERE  CM.CategoryMasterId = IM.linktoCategoryMasterId  AND CM.IsRawMaterial = 0  AND  CM.IsDeleted = 0 )AS Category
	
	--	,IM.ItemName,IM.ItemMasterId, CIT.*

	--FROM			
	--	posItemMaster IM 
	--JOIN 
	--	posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
			
	--LEFT JOIN	
	--	posCounterItemTran CIT  ON ItemMasterId = CIT.linktoItemMasterId AND CIT.linktoCounterMasterId = @linktoCounterMasterId
	--WHERE 
	--	IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
	--	IM.ItemType = 0  
	--ORDER BY Category
	
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemTranWithAddlessTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posCounterItemTranWithAddlessTran_SelectAll 1
CREATE PROCEDURE [dbo].[posCounterItemTranWithAddlessTran_SelectAll]
	@linktoAddLessMasterId smallint = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT DISTINCT
		 Category,ItemName,ItemCode,ItemMasterId,AddLessItemTranId,linktoAddLessMasterId,linktoItemMasterId,ItemType
	FROM
	(
		SELECT DISTINCT
	
		CM.CategoryName  AS Category,
		IM.ItemName,IM.ItemCode,IM.ItemMasterId,IM.ItemType, AIT.*

		FROM			
			posItemMaster IM 	
		JOIN	
			posCategoryMaster CM ON  CM.CategoryMasterId = IM.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
		LEFT JOIN	
			posAddLessItemTran AIT  ON ItemMasterId = AIT.linktoItemMasterId AND AIT.linktoAddLessMasterId = @linktoAddLessMasterId
		WHERE 
			IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
			IM.ItemType IN (0,3)  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
	UNION ALL
		SELECT DISTINCT

			CM.CategoryName  AS Category,
			IM.ItemName,IM.ItemCode,IM.ItemMasterId,IM.ItemType, AIT.*

			FROM			
				posItemMaster IM 
			JOIN 
					posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
			JOIN	
					posCategoryMaster CM ON  CM.CategoryMasterId = ICT.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
			LEFT JOIN	
				posAddLessItemTran AIT  ON ItemMasterId = AIT.linktoItemMasterId AND AIT.linktoAddLessMasterId = @linktoAddLessMasterId
			WHERE 
				IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
				IM.ItemType IN (0,3) AND CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
	)TempTable
	ORDER BY Category

	
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemTranWithOfferItemsTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posCounterItemTranWithOfferItemsTran_SelectAll 9
CREATE PROCEDURE [dbo].[posCounterItemTranWithOfferItemsTran_SelectAll]
@linktoOfferMasterId smallint = null,
@OfferItemType smallint
AS
BEGIN

SET NOCOUNT ON

			SELECT DISTINCT
			CM.CategoryName  AS Category,
			IM.ItemName,IM.ItemMasterId
			,IM.ItemCode
			--, CIT.linktoCounterMasterId
			--,CIT.linktoItemMasterId
			,OIT.OfferItemsTranId
			,OIT.linktoOfferMasterId

			FROM			
				posItemMaster IM 	
			JOIN	posCategoryMaster CM ON  CM.CategoryMasterId = IM.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
			--LEFT JOIN	
				--posCounterItemTran CIT  ON IM.ItemMasterId = CIT.linktoItemMasterId 
			LEFT JOIN 
			posOfferItemsTran  OIT ON OIT.linktoItemMasterId=IM.ItemMasterId AND linktoOfferMasterId=ISNULL(@linktoOfferMasterId,0) AND OfferItemType = @OfferItemType
			WHERE 
				IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
				IM.ItemType = 0  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1
	UNION ALL
		SELECT DISTINCT
				CM.CategoryName  AS Category,
				IM.ItemName,IM.ItemMasterId
				,IM.ItemCode
				--, CIT.linktoCounterMasterId
				--,CIT.linktoItemMasterId
				,OIT.OfferItemsTranId
				,OIT.linktoOfferMasterId

			FROM			
				posItemMaster IM 
			JOIN 
					posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
			JOIN	posCategoryMaster CM ON  CM.CategoryMasterId = ICT.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
			--LEFT JOIN	
			--	posCounterItemTran CIT  ON ItemMasterId = CIT.linktoItemMasterId 
				LEFT JOIN 
				posOfferItemsTran  OIT ON OIT.linktoItemMasterId=IM.ItemMasterId  AND linktoOfferMasterId=ISNULL(@linktoOfferMasterId,0) AND OfferItemType = @OfferItemType
			WHERE 
				IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
				IM.ItemType = 0 AND CM.IsDeleted = 0 AND CM.IsEnabled = 1

		ORDER BY Category 
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterItemTranWithOfferItemsTranByCounter_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posCounterItemTranWithOfferItemsTranByCounter_SelectAll NULL,'1',2

CREATE PROCEDURE [dbo].[posCounterItemTranWithOfferItemsTranByCounter_SelectAll]
@linktoOfferMasterId smallint = null
,@linktoCounterMasterIds varchar(50)
,@OfferItemType smallint
,@linktoBusinessMasterId smallint
AS
BEGIN

SET NOCOUNT ON

SELECT DISTINCT ItemName,ItemMasterId,ItemCode,OfferItemsTranId,linktoOfferMasterId,CategoryName,linktoCounterMasterIds
FROM
(		
	SELECT DISTINCT
			
			IM.ItemName,IM.ItemMasterId
			,IM.ItemCode
			,OIT.OfferItemsTranId
			,OIT.linktoOfferMasterId
			,CM.CategoryName
			,STUFF((
				SELECT DISTINCT ',' + CONVERT(VARCHAR,CTT1.linktoCounterMasterId)  
				FROM posCounterItemTran CTT1
				WHERE CTT1.linktoItemMasterId = IM.ItemMasterId
				FOR XML PATH(''), TYPE   
				).value('.', 'VARCHAR(MAX)')   
				,1,1,'') As linktoCounterMasterIds
			FROM	
			posItemMaster IM 	
			JOIN 
					posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
			JOIN	posCategoryMaster CM ON  CM.CategoryMasterId = ICT.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
			JOIN	
				posCounterItemTran CIT  ON IM.ItemMasterId = CIT.linktoItemMasterId and linktoCounterMasterId in  (SELECT * from dbo.Parse(@linktoCounterMasterIds, ','))
			LEFT JOIN 
			posOfferItemsTran  OIT ON OIT.linktoItemMasterId=IM.ItemMasterId AND linktoOfferMasterId=@linktoOfferMasterId AND OIT.OfferItemType=@OfferItemType
			WHERE 
				
				IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
				IM.ItemType = 0  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
UNION ALL
		SELECT DISTINCT
			
		IM.ItemName,IM.ItemMasterId,IM.ItemCode,OIT.OfferItemsTranId,OIT.linktoOfferMasterId,CM.CategoryName
		,STUFF((
			SELECT DISTINCT ',' + CONVERT(VARCHAR,CTT1.linktoCounterMasterId)  
			FROM posCounterItemTran CTT1
			WHERE CTT1.linktoItemMasterId = IM.ItemMasterId
			FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')   
			,1,1,'') As linktoCounterMasterIds
		FROM	
			posItemMaster IM
		JOIN	
			posCategoryMaster CM ON  CM.CategoryMasterId = IM.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
		JOIN	
			posCounterItemTran CIT  ON IM.ItemMasterId = CIT.linktoItemMasterId and linktoCounterMasterId in  (SELECT * from dbo.Parse(@linktoCounterMasterIds, ','))
		LEFT JOIN 
			posOfferItemsTran  OIT ON OIT.linktoItemMasterId=IM.ItemMasterId AND linktoOfferMasterId=@linktoOfferMasterId AND OIT.OfferItemType=@OfferItemType
		WHERE 
				
			IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
			IM.ItemType = 0  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
)TempTable
ORDER BY CategoryName 


	--UNION ALL
	--	SELECT DISTINCT
	--			CM.CategoryName  AS Category,
	--			IM.ItemName,IM.ItemMasterId
	--			, CIT.linktoCounterMasterId
	--			--,CIT.linktoItemMasterId
	--			,OIT.OfferItemsTranId
	--			,OIT.linktoOfferMasterId

	--		FROM			
	--			posItemMaster IM 
	--		JOIN 
	--				posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
	--		JOIN	posCategoryMaster CM ON  CM.CategoryMasterId = ICT.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
	--			LEFT JOIN	
	--			posCounterItemTran CIT  ON IM.ItemMasterId = CIT.linktoItemMasterId and linktoCounterMasterId=@linktoCounterMasterId
	--			LEFT JOIN 
	--			posOfferItemsTran  OIT ON OIT.linktoItemMasterId=IM.ItemMasterId  AND linktoOfferMasterId=@linktoOfferMasterId
	--		WHERE 
	--			IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
	--			IM.ItemType = 0 AND CM.IsDeleted = 0 AND CM.IsEnabled = 1

	--	ORDER BY Category 
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterMaster_DeleteAll]
	 @CounterMasterIds varchar(1000)
	 ,@linktoUserMasterIdUpdatedBy smallint
	 ,@UpdateDateTime datetime
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRowCount int
	SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@CounterMasterIds, ','))

	UPDATE
		posCounterMaster
	SET 
		IsDeleted = 1,
		linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy,
		UpdateDateTime = @UpdateDateTime
	WHERE
		CounterMasterId IN (SELECT * from dbo.Parse(@CounterMasterIds, ','))
		AND CounterMasterId NOT IN
		(
			SELECT linktoCounterMasterId from posCounterItemTran where IsDeleted = 0 AND IsEnabled = 1 
			AND linktoCounterMasterId IN (SELECT * from dbo.Parse(@CounterMasterIds, ','))
		)

	SET @UpdatedRowCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		IF @TotalRecords = @UpdatedRowCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END  
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterMaster_Insert]
	 @CounterMasterId smallint OUTPUT
	,@ShortName varchar(10)
	,@CounterName varchar(100)
	,@Description varchar(500) = NULL
	,@ImageName varchar(100) = NULL
	,@CounterColor varchar(6) = NULL
	,@linktoDepartmentMasterId smallint
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CounterMasterId FROM posCounterMaster WHERE CounterName = @CounterName AND IsDeleted=0 AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @CounterMasterId = CounterMasterId FROM posCounterMaster WHERE CounterName = @CounterName AND IsDeleted=0 AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posCounterMaster
	(
		 ShortName
		,CounterName
		,Description
		,ImageName
		,CounterColor
		,linktoDepartmentMasterId
		,linktoBusinessMasterId
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @ShortName
		,@CounterName
		,@Description
		,@ImageName
		,@CounterColor
		,@linktoDepartmentMasterId
		,@linktoBusinessMasterId
		,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	SET @CounterMasterId = @@IDENTITY

	INSERT INTO posUserCounterTran
	(
		 linktoUserMasterId
		,linktoCounterMasterId
	)
	SELECT UserMasterId, @CounterMasterId
	FROM posUserMaster, posRoleMaster 
	WHERE linktoRoleMasterId = RoleMasterId 
	   AND Role = 'Admin'
	   AND posUserMaster.linktoBusinessMasterId = @linktoBusinessMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posCounterMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCounterMaster_Select]
	 @CounterMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posCounterMaster.*
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department
	FROM
		 posCounterMaster
	WHERE
		CounterMasterId = @CounterMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCounterMaster_SelectAll  NULL NULL 1
CREATE PROCEDURE [dbo].[posCounterMaster_SelectAll]
	  @IsEnabled bit = NULL
	 ,@linktoBusinessMasterId smallint = NULL
	 ,@linktoUserMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posCounterMaster.*, UCT.linktoUserMasterId
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department
	FROM
		 posCounterMaster
	JOIN posUserCounterTran UCT on UCT.linktoUserMasterId=@linktoUserMasterId AND UCT.linktoCounterMasterId=CounterMasterId

	WHERE
		((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND linktoBusinessMasterId = ISNULL(@linktoBusinessMasterId,linktoBusinessMasterId)
		AND IsDeleted=0
	ORDER BY CounterName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterMaster_Update]
	 @CounterMasterId smallint
	,@ShortName varchar(10)
	,@CounterName varchar(100)
	,@Description varchar(500) = NULL
	,@ImageName varchar(100) = NULL
	,@CounterColor varchar(6) = NULL
	,@linktoDepartmentMasterId smallint
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CounterMasterId FROM posCounterMaster WHERE CounterName = @CounterName AND CounterMasterId != @CounterMasterId AND IsDeleted=0 AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posCounterMaster
	SET
		 ShortName = @ShortName
		,CounterName = @CounterName
		,Description = @Description
		,ImageName = @ImageName
		,CounterColor = @CounterColor
		,linktoDepartmentMasterId = @linktoDepartmentMasterId
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		CounterMasterId = @CounterMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterMasterCounterName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCounterMasterCounterName_SelectAll 2
CREATE PROCEDURE [dbo].[posCounterMasterCounterName_SelectAll]
	@linkToUserMasterId smallint = null,
	@linkToBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
	DISTINCT
		 CounterMasterId
		,CounterName
		 ,CounterColor
		 ,(convert(varchar(500),CounterMasterId) + '_' + CounterColor) Color
	FROM
		 posCounterMaster
		JOIN posUserCounterTran UCT ON UCT.linktoCounterMasterId=CounterMasterId AND linktoUserMasterId=ISNULL(@linkToUserMasterId,linktoUserMasterId)
	WHERE
		IsEnabled = 1
		AND IsDeleted = 0 AND linktoBusinessMasterId = @linkToBusinessMasterId
	ORDER BY CounterName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterPrinterTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterPrinterTran_Insert]
	 @CounterPrinterTranId smallint OUTPUT
	,@PrinterName varchar(50)
	,@Copy smallint
	,@Size smallint
	,@IsReceiptPrinter bit
	,@linktoCounterMasterId smallint
	,@linktoCategoryMasterId smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	IF @IsReceiptPrinter = 1
	BEGIN
			IF EXISTS(SELECT CounterPrinterTranId FROM posCounterPrinterTran WHERE linktoCounterMasterId = @linktoCounterMasterId AND IsReceiptPrinter =1)
				BEGIN
				SET @Status = -2
				RETURN
		    END
	END
	
	ELSE
			BEGIN
			IF EXISTS(SELECT CounterPrinterTranId FROM posCounterPrinterTran WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCategoryMasterId = @linktoCategoryMasterId AND IsReceiptPrinter =0)
			BEGIN
					SET @Status = -2
					RETURN
					
			END 
			END

	INSERT INTO posCounterPrinterTran
						(
							 PrinterName
							,Copy
							,Size
							,IsReceiptPrinter
							,linktoCounterMasterId
							,linktoCategoryMasterId
						)
						VALUES
						(
							 @PrinterName
							,@Copy
							,@Size
							,@IsReceiptPrinter
							,@linktoCounterMasterId
							,@linktoCategoryMasterId
						)
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CounterPrinterTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterPrinterTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCounterPrinterTran_SelectAll 0,15,5
CREATE PROCEDURE [dbo].[posCounterPrinterTran_SelectAll]
	@IsReceiptPrinter bit = NULL
   -- ,@linktoCategoryMasterId smallint = null
	--,@linktoCounterMasterId smallint = null
	,@linktoUserMasterId smallint
AS
BEGIN

	SET NOCOUNT ON
	
	IF @IsReceiptPrinter = 1
	BEGIN
		SELECT DISTINCT CounterMasterId,CounterName,ISNULL(PrinterName,'') AS PrinterName,CounterPrinterTranId,IsReceiptPrinter,cpt.linktoCounterMasterId,linktoCategoryMasterId ,Size,Copy,'' AS CategoryName
		FROM posCounterMaster CM
			LEFT JOIN posCounterPrinterTran CPT ON CPT.linktoCounterMasterId = CM.CounterMasterId AND IsReceiptPrinter = @IsReceiptPrinter
			JOIN posUserCounterTran UCT ON UCT.linktoCounterMasterId =CM.CounterMasterId  AND UCT.linktoUserMasterId=@linktoUserMasterId
		WHERE CM.IsDeleted = 0 AND CM.IsEnabled = 1
		--AND CounterMasterId = CASE WHEN  @linktoCounterMasterId = 0  THEN CounterMasterId ELSE @linktoCounterMasterId END
		ORDER BY CounterName
	END 
	ELSE
	BEGIN
		SELECT DISTINCT CounterMasterId,CounterName,ISNULL(PrinterName,'') AS  PrinterName,CounterPrinterTranId,IsReceiptPrinter,cpt.linktoCounterMasterId,linktoCategoryMasterId ,Size,Copy,linktoCategoryMasterId,
		CASE WHEN linktoCategoryMasterId = 0 THEN '- ALL -' ELSE CategoryName END CategoryName
		FROM posCounterMaster CM
			LEFT JOIN posCounterPrinterTran CPT ON CPT.linktoCounterMasterId = CM.CounterMasterId AND IsReceiptPrinter = @IsReceiptPrinter 		
			LEFT JOIN posCategoryMaster CatM ON CatM.CategoryMasterId = CPT.linktoCategoryMasterId 
			JOIN posUserCounterTran UCT ON UCT.linktoCounterMasterId =CM.CounterMasterId AND UCT.linktoUserMasterId=@linktoUserMasterId
		WHERE CM.IsDeleted = 0 AND CM.IsEnabled = 1
		--AND CounterMasterId = CASE WHEN  @linktoCounterMasterId = 0  THEN CounterMasterId ELSE @linktoCounterMasterId END
		ORDER BY CounterName, CategoryName
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterPrinterTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterPrinterTran_Update]
	 @CounterPrinterTranId smallint
	,@PrinterName varchar(50)
	,@Copy smallint
	,@Size smallint
	,@IsReceiptPrinter bit
	,@linktoCounterMasterId smallint
	,@linktoCategoryMasterId smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	--	IF @IsReceiptPrinter = 1
	--BEGIN
	--		IF EXISTS(SELECT CounterPrinterTranId FROM posCounterPrinterTran WHERE linktoCounterMasterId = @linktoCounterMasterId AND IsReceiptPrinter =1)
	--			BEGIN
	--			SET @Status = -2
	--			RETURN
	--	    END
	--END
	UPDATE posCounterPrinterTran
	SET
		 PrinterName = @PrinterName
		,Copy = @Copy
		,Size = @Size
		--,IsReceiptPrinter = @IsReceiptPrinter
		--,linktoCounterMasterId = @linktoCounterMasterId
		--,linktoCategoryMasterId = @linktoCategoryMasterId
	WHERE
		CounterPrinterTranId = @CounterPrinterTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterPrinterTranByCounter_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterPrinterTranByCounter_SelectAll]
	 @IsReceiptPrinter bit = NULL
	,@linktoCounterMasterId smallint
AS
BEGIN
    
    SELECT
	    *
	   ,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS CounterName
	   ,(SELECT CategoryName FROM posCategoryMaster WHERE CategoryMasterId = linktoCategoryMasterId) AS CategoryName
    FROM
	   posCounterPrinterTran
    WHERE
	   IsReceiptPrinter = @IsReceiptPrinter
	   AND linktoCounterMasterId = @linktoCounterMasterId

END



GO
/****** Object:  StoredProcedure [dbo].[posCounterSettingValueTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterSettingValueTran_Delete]
	 @linktoCounterMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posCounterSettingValueTran
	WHERE
		linktoCounterMasterId = @linktoCounterMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterSettingValueTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterSettingValueTran_Insert]
	 @CounterSettingValueTranId smallint OUTPUT
	,@linktoCounterMasterId smallint
	,@linktoCounterSettingMasterId smallint
	,@Value varchar(500)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posCounterSettingValueTran
	(
		 linktoCounterMasterId
		,linktoCounterSettingMasterId
		,Value
	)
	VALUES
	(
		 @linktoCounterMasterId
		,@linktoCounterSettingMasterId
		,@Value
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CounterSettingValueTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterSettingValueTranCounterWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCounterSettingValueTranCounterWise_SelectAll 18  

CREATE PROCEDURE [dbo].[posCounterSettingValueTranCounterWise_SelectAll]
	@linktoCounterMasterId smallint  
AS
BEGIN
	SET NOCOUNT ON
SELECT
		CSVT.CounterSettingValueTranId,
		CSVT.linktoCounterMasterId,
		CSM.CounterSettingMasterId,
		ISNULL(CSVT.Value, CSM.DefaultValue) AS value
	FROM
		posCounterSettingMaster CSM		 
	LEFT JOIN 
		posCounterSettingValueTran CSVT ON CSVT.linktoCounterSettingMasterId = CSM.CounterSettingMasterId AND linktoCounterMasterId = @linktoCounterMasterId	
	ORDER BY CounterSettingValueTranId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterSettingValueTranDayEndCounter_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCounterSettingValueTranDayEndCounter_SelectAll 6,'2016-02-03 14:54:00',1
CREATE PROCEDURE [dbo].[posCounterSettingValueTranDayEndCounter_SelectAll]
	@linktoCounterSettingMasterId smallint,
	@CurrentDate DateTime,
	@linktoUserMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT CounterMasterId,CounterName,DayEndDateTime
	FROM
	(
		SELECT 
			row_number() over(partition by CDT.linktoCounterMasterId order by CDT.DayEndDateTime desc) As rownum,
			CM.CounterMasterId,CM.CounterName,CDT.DayEndDateTime,SCVT.Value
		FROM 
			posCounterMaster CM
		JOIN
			posUserCounterTran UCT ON UCT.linktoCounterMasterId = CM.CounterMasterId
		JOIN
			posCounterSettingValueTran SCVT ON SCVT.linktoCounterMasterId = UCT.linktoCounterMasterId
		JOIN 
			posCounterDayEndTran CDT ON CDT.linktoCounterMasterId = SCVT.linktoCounterMasterId 				
		WHERE
			SCVT.linktoCounterSettingMasterId = @linktoCounterSettingMasterId AND UCT.linktoUserMasterId = @linktoUserMasterId
	)TempTable
	WHERE rownum = 1 AND (Convert(varchar(8),DATEADD(day, 1, DayEndDateTime),112) <= Convert(varchar(8),@CurrentDate,112)
				AND Convert(time(7),Value,108) <= Convert(time(7),@CurrentDate,108))
	UNION ALL
	SELECT DISTINCT
		CM.CounterMasterId,CM.CounterName,@CurrentDate AS DayEndDateTime
	FROM 
		posCounterMaster CM
	JOIN
		posUserCounterTran UCT ON UCT.linktoCounterMasterId = CM.CounterMasterId
	JOIN
		posCounterSettingValueTran SCVT ON SCVT.linktoCounterMasterId = UCT.linktoCounterMasterId
	WHERE 
		CounterMasterId NOT IN (SELECT linktoCounterMasterId FROM posCounterDayEndTran)
		AND UCT.linktoUserMasterId = @linktoUserMasterId AND SCVT.linktoCounterSettingMasterId = @linktoCounterSettingMasterId
		AND Convert(time(7),SCVT.Value,108) < Convert(time(7),@CurrentDate,108)
	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posCounterTableTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterTableTran_Insert]
	 @TableMasterIds varchar(1000),
	 @CounterMasterID smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE FROM	posCounterTableTran WHERE linktoCounterMasterId = @CounterMasterID


	INSERT INTO 
		posCounterTableTran(linktoCounterMasterId,linktoTableMasterId)
	SELECT
		@CounterMasterID,parsevalue
	FROM dbo.Parse(@TableMasterIds,',')
	

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterTableTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posCounterTableTran_SelectAll 1,1

CREATE PROCEDURE [dbo].[posCounterTableTran_SelectAll]
	@CounterMasterID as smallint
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT DISTINCT	
		TM.TableMasterId,TM.TableName,CT.CounterTableTranId,CT.linktoCounterMasterId,CT.linktoTableMasterId,OT.OrderType	
	FROM 
		posTableMaster TM 	
	JOIN
		posOrderTypeMaster OT ON OT.OrderTypeMasterId = TM.linktoOrderTypeMasterId
	LEFT JOIN 
		posCounterTableTran CT on CT.linktoTableMasterId = TM.TableMasterId AND CT.linktoCounterMasterId = @CounterMasterID 
	WHERE 
		TM.IsEnabled = 1 
		AND TM.IsDeleted = 0
		AND linktoBusinessMasterId = @linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCounterTableTranConfiguration_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCounterTableTranConfiguration_Insert]
	 @TableName varchar(50)
	 ,@CounterName varchar(100)
	 ,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DECLARE @linktotableMasterId smallint,@linktoCounterMasterId smallint

	SELECT @linktotableMasterId = TableMasterId FROM posTableMaster WHERE TableName = @TableName AND IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId

	SELECT @linktoCounterMasterId = CounterMasterId FROM posCounterMaster WHERE CounterName = @CounterName AND IsDeleted = 0 AND IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId

	INSERT INTO 
		posCounterTableTran(linktoCounterMasterId,linktoTableMasterId)
	VALUES
		(@linktoCounterMasterId,@linktotableMasterId)
	

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCountryMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCountryMaster_Select]
	 @CountryMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posCountryMaster.*
	FROM
		 posCountryMaster
	WHERE
		CountryMasterId = @CountryMasterId

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posCountryMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCountryMaster_SelectAll]
	 @IsEnabled bit
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posCountryMaster.*
		  
	FROM
		posCountryMaster

	WHERE 
		IsEnabled=@IsEnabled
	 
	  
	ORDER BY CountryName

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posCountryMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCountryMaster_Update]
	 @CountryMasterId smallint
	,@IsEnabled bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posCountryMaster
	SET
		 
		IsEnabled = @IsEnabled
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		CountryMasterId = @CountryMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posCountryMasterCountryName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCountryMasterCountryName_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 CountryMasterId
		,CountryName
	FROM
		posCountryMaster
	WHERE
	    IsEnabled = 1	 
	ORDER BY CountryName

	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posCustomerAddressTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerAddressTran_Insert]
	 @CustomerAddressTranId int OUTPUT
	,@linktoCustomerMasterId int
	,@Address varchar(500) = NULL
	,@linktoCountryMasterId smallint = NULL
	,@linktoStateMasterId smallint = NULL
	,@linktoCityMasterId int = NULL
	,@linktoAreaMasterId int = NULL
	,@ZipCode varchar(10) = NULL
	,@IsPrimary bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@IsDeleted bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posCustomerAddressTran
	(
		 linktoCustomerMasterId
		,Address
		,linktoCountryMasterId
		,linktoStateMasterId
		,linktoCityMasterId
		,linktoAreaMasterId
		,ZipCode
		,IsPrimary
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,IsDeleted
	)
	VALUES
	(
		 @linktoCustomerMasterId
		,@Address
		,@linktoCountryMasterId
		,@linktoStateMasterId
		,@linktoCityMasterId
		,@linktoAreaMasterId
		,@ZipCode
		,@IsPrimary
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@IsDeleted
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CustomerAddressTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerAddressTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerAddressTran_SelectAll]
	 @linktoCustomerMasterId int 
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posCustomerAddressTran.*
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer		
		,(SELECT CountryName FROM posCountryMaster WHERE CountryMasterId = linktoCountryMasterId) AS Country		
		,(SELECT StateName FROM posStateMaster WHERE StateMasterId = linktoStateMasterId) AS State		
		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS City		
		,(SELECT AreaName FROM posAreaMaster WHERE AreaMasterId = linktoAreaMasterId) AS Area
	FROM
		 posCustomerAddressTran
	WHERE
		linktoCustomerMasterId=@linktoCustomerMasterId
		AND IsDeleted = 0
	ORDER BY CustomerAddressTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerAddressTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerAddressTran_Update]
	 @CustomerAddressTranId int
	,@linktoCustomerMasterId int
	,@Address varchar(500) = NULL
	,@linktoCountryMasterId smallint = NULL
	,@linktoStateMasterId smallint = NULL
	,@linktoCityMasterId int = NULL
	,@linktoAreaMasterId int = NULL
	,@ZipCode varchar(10) = NULL
	,@IsPrimary bit
	,@IsDeleted bit
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posCustomerAddressTran
	SET
		 linktoCustomerMasterId = @linktoCustomerMasterId
		,Address = @Address
		,linktoCountryMasterId = @linktoCountryMasterId
		,linktoStateMasterId = @linktoStateMasterId
		,linktoCityMasterId = @linktoCityMasterId
		,linktoAreaMasterId = @linktoAreaMasterId
		,ZipCode = @ZipCode
		,IsPrimary = @IsPrimary
		,IsDeleted = @IsDeleted
		
	WHERE
		CustomerAddressTranId = @CustomerAddressTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerInvoiceMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerInvoiceMaster_DeleteAll]
	 
	@CustomerInvoiceMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	Declare @TotalRecords int,@UpdatedRowCount int

	SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@CustomerInvoiceMasterIds, ','))

	UPDATE
		posCustomerInvoiceMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		CustomerInvoiceMasterId IN (SELECT * from dbo.Parse(@CustomerInvoiceMasterIds, ','))
		AND CustomerInvoiceMasterId NOT IN
		(
			SELECT linktoCustomerInvoiceMasterId from posCustomerPaymentTran where IsDeleted = 0 
			AND linktoCustomerInvoiceMasterId IN (SELECT * from dbo.Parse(@CustomerInvoiceMasterIds, ','))
		)

	SET @UpdatedRowCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		IF @TotalRecords = @UpdatedRowCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerInvoiceMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerInvoiceMaster_Insert]
	 @CustomerInvoiceMasterId int OUTPUT
	,@linktoCustomerMasterId int
	,@InvoiceNo varchar(20)
	,@InvoiceDate datetime
	,@DueDate datetime
	,@Remark varchar(250) = NULL
	,@TotalAmount money
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT CustomerInvoiceMasterId FROM posCustomerInvoiceMaster WHERE InvoiceNo = @InvoiceNo AND IsDeleted = 0)
	BEGIN
		SELECT @CustomerInvoiceMasterId = CustomerInvoiceMasterId FROM posCustomerInvoiceMaster WHERE InvoiceNo = @InvoiceNo
		SET @Status = -2
		RETURN
	END
	INSERT INTO posCustomerInvoiceMaster
	(
		 linktoCustomerMasterId
		,InvoiceNo
		,InvoiceDate
		,DueDate
		,Remark
		,TotalAmount
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoCustomerMasterId
		,@InvoiceNo
		,@InvoiceDate
		,@DueDate
		,@Remark
		,@TotalAmount
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CustomerInvoiceMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerInvoiceMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCustomerInvoiceMaster_SelectAll 62,'','20160311','20160712'
CREATE PROCEDURE [dbo].[posCustomerInvoiceMaster_SelectAll]
@linktoCustomerMasterId int
,@InvoiceNo varchar(20)
,@FromDate date
,@ToDate date
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		CIM.*
		,(SELECT CustomerName FROM posCustomerMaster WHERE linktoCustomerMasterId = CustomerMasterId) AS Customer
		,(SELECT CASE WHEN SUM(CPT.TotalAmount) = CIM.TotalAmount THEN 1 ELSE 0 END 
			FROM posCustomerPaymentTran CPT 
			WHERE CPT.linktoCustomerInvoiceMasterId = CIM.CustomerInvoiceMasterId AND CPT.IsDeleted = 0 ) As IsPayment
	FROM
		 posCustomerInvoiceMaster CIM
	WHERE
		CIM.IsDeleted = 0
		AND CIM.InvoiceNo Like '%' + @InvoiceNo + '%'
		AND CONVERT(varchar(8), CIM.InvoiceDate ,112) BETWEEN CONVERT(varchar(8), @FromDate ,112) AND CONVERT(varchar(8), @ToDate ,112)
		AND CIM.linktoCustomerMasterId = @linktoCustomerMasterId
		
	ORDER BY InvoiceNo

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerInvoiceMasterInvoiceNo_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCustomerInvoiceMasterInvoiceNo_SelectAll 62,'20150616','20160617'
CREATE PROCEDURE [dbo].[posCustomerInvoiceMasterInvoiceNo_SelectAll]
@linktoCustomerMasterId int,
@FromDate datetime,
@Todate Datetime


AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 CustomerInvoiceMasterId
		,InvoiceNo
		,TotalAmount
	FROM
		 posCustomerInvoiceMaster
	WHERE
		IsDeleted = 0
		AND CONVERT(varchar(8),InvoiceDate ,112) BETWEEN CONVERT(varchar(8),@FromDate ,112) AND CONVERT(varchar(8),@Todate ,112)
		AND linktoCustomerMasterId = @linktoCustomerMasterId
	ORDER BY InvoiceNo

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerInvoiceRemaingPaymentAmount_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posCustomerInvoiceRemaingPaymentAmount_Select 2,0
CREATE PROCEDURE [dbo].[posCustomerInvoiceRemaingPaymentAmount_Select]
	@CustomerInvoiceMasterId int,
	@CustomerPaymentTranId int
 AS
BEGIN
	SELECT
		(CIM.TotalAmount -  CASE WHEN SUM(CPT.TotalAmount) IS NULL THEN 0 ELSE SUM(CPT.TotalAmount) END) AS ReaminigAmount
	FROM 
		posCustomerInvoiceMaster  CIM
	LEFT JOIN 
		posCustomerPaymentTran CPT ON linktoCustomerInvoiceMasterId = CustomerInvoiceMasterId AND CPT.CustomerPaymentTranId != @CustomerPaymentTranId AND CPT.IsDeleted = 0
	WHERE 
		CustomerInvoiceMasterId = @CustomerInvoiceMasterId AND CIM.IsDeleted = 0
	GROUP BY 
		CustomerInvoiceMasterId,CIM.TotalAmount
	
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerInvoiceTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerInvoiceTran_Insert]
	 @CustomerInvoiceTranId int OUTPUT
	,@linktoCustomerInvoiceMasterId int
	,@linktoSalesMasterId bigint
	,@TotalAmount money
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posCustomerInvoiceTran
	(
		 linktoCustomerInvoiceMasterId
		,linktoSalesMasterId
		,TotalAmount
	)
	VALUES
	(
		 @linktoCustomerInvoiceMasterId
		,@linktoSalesMasterId
		,@TotalAmount
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CustomerInvoiceTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerInvoiceTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerInvoiceTran_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posCustomerInvoiceTran.*
		,(SELECT InvoiceNo FROM posCustomerInvoiceMaster WHERE CustomerInvoiceMasterId = linktoCustomerInvoiceMasterId) AS CustomerInvoice		
		,(SELECT BillNumber FROM posSalesMaster WHERE SalesMasterId = linktoSalesMasterId) AS Sales
	FROM
		 posCustomerInvoiceTran
	
	ORDER BY CustomerInvoiceTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMaster_DeleteAll]
	 @CustomerMasterIds varchar(1000)
	 ,@linktoUserMasterIdUpdatedBy smallint
	 ,@UpdateDateTime datetime
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRawCount int

	UPDATE
		posCustomerMaster
	SET 
		IsDeleted = 1,
		linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy,
		UpdateDateTime = @UpdateDateTime
	WHERE
		CustomerMasterId IN (SELECT * from dbo.Parse(@CustomerMasterIds, ','))
		AND CustomerMasterId NOT IN
		(
			SELECT linktoCustomerMasterId from posSalesMaster WHERE IsEnabled=1 AND IsDeleted =0
			AND linktoCustomerMasterId IN (SELECT * from dbo.Parse(@CustomerMasterIds, ','))

		)
		set @UpdatedRawCount = @@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@CustomerMasterIds, ','))
		
		IF @TotalRecords = @UpdatedRawCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMaster_Insert]
	 @CustomerMasterId int OUTPUT
	,@CustomerType smallint 
	,@ShortName varchar(10)
	,@CustomerName varchar(100)
	,@Description varchar(500) = NULL
	,@ContactPersonName varchar(100) = NULL
	,@Designation varchar(50) = NULL
	,@Birthdate datetime = null
	,@AnniversaryDate datetime = null
	,@Phone1 varchar(15)
	,@IsPhone1DND bit
	,@Phone2 varchar(15) = NULL
	,@IsPhone2DND bit = NULL
	,@Email1 varchar(80) = NULL
	,@Email2 varchar(80) = NULL
	,@Fax varchar(15) = NULL
	,@ImageName varchar(100) = NULL
	,@IsFavourite bit = NULL
	,@IsCredit bit
	,@OpeningBalance money
	,@CreditDays smallint
	,@CreditBalance money
	,@CreditLimit money
	,@linktoBusinessMasterId smallint
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoMembershipTypeMasterId smallint = NULL
	,@TotalPoints smallint = NULL
	,@MembershipCardExpiryDate datetime = NULL
	,@MembershipCardNumber varchar(50) = NULL
	,@linktoSourceMasterId smallint
	,@Gender varchar(6) = NULL
	,@Password varchar(25) = NULL
	,@LastLoginDateTime datetime = NULL
	,@Status smallint OUTPUT
	,@TotalAmount Money= null
	
AS
BEGIN
	SET NOCOUNT OFF

	SET @Email1 = ISNULL(@Email1, '')

	IF EXISTS(SELECT CustomerMasterId FROM posCustomerMaster WHERE ((@Phone1 <> '' AND Phone1 = @Phone1) OR (@Email1 <> '' AND Email1 = @Email1)) 
		AND IsDeleted = 0 AND CustomerType=@CustomerType AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @CustomerMasterId = CustomerMasterId FROM posCustomerMaster WHERE  ((@Phone1 <> '' AND Phone1 = @Phone1) OR (@Email1 <> '' AND Email1 = @Email1)) 
		AND IsDeleted = 0 AND CustomerType=@CustomerType AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posCustomerMaster
	(
	     CustomerType
		 ,ShortName
		,CustomerName
		,Description
		,ContactPersonName
		,Designation
		,Birthdate
		,AnniversaryDate
		,Phone1
		,IsPhone1DND
		,Phone2
		,IsPhone2DND
		,Email1
		,Email2
		,Fax
		,ImageName
		,IsFavourite
		,IsCredit
		,OpeningBalance
		,CreditDays
		,CreditBalance
		,CreditLimit
		,linktoBusinessMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,IsEnabled
		,IsDeleted
		,linktoMembershipTypeMasterId
		,TotalPoints
		,MembershipCardExpiryDate
		,MembershipCardNumber
		,linktoSourceMasterId
		,Gender
		,Password
		,LastLoginDateTime
		,TotalAmount
	)
	VALUES
	(
	     @CustomerType
		 ,@ShortName
		,@CustomerName
		,@Description
		,@ContactPersonName
		,@Designation	
		,@Birthdate 
	    ,@AnniversaryDate		
		,@Phone1
		,@IsPhone1DND
		,@Phone2
		,@IsPhone2DND
		,@Email1
		,@Email2
		,@Fax
		,@ImageName
		,@IsFavourite
		,@IsCredit
		,@OpeningBalance
		,@CreditDays
		,@CreditBalance
		,@CreditLimit
		,@linktoBusinessMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@IsEnabled
		,@IsDeleted
		,@linktoMembershipTypeMasterId
		,@TotalPoints
		,@MembershipCardExpiryDate
		,@MembershipCardNumber
		,@linktoSourceMasterId
		,@Gender
		,@Password
		,@LastLoginDateTime
		,@TotalAmount
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CustomerMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCustomerMaster_Select 55,null
CREATE PROCEDURE [dbo].[posCustomerMaster_Select]
	 @CustomerMasterId int = NULL
	,@Email1 varchar(80) = NULL
AS
BEGIN
	SET NOCOUNT ON

	SELECT 
		CustomerMasterId,ShortName,CustomerName,Description,ContactPersonName,Designation,Phone1,IsPhone1DND,Phone2,IsPhone2DND,Email1,Email2,
		Fax,ImageName,Age,BirthDate,AnniversaryDate,CustomerType,IsFavourite,IsCredit,OpeningBalance,CreditDays,CreditBalance,CreditLimit,linktoBusinessMasterId,
		CreateDateTime,linktoUserMasterIdCreatedBy,UpdateDateTime,linktoUserMasterIdUpdatedBy,IsEnabled,IsDeleted,MembershipCardExpiryDate,MembershipCardNumber,linktoMembershipTypeMasterId,TotalPoints,TotalAmount
		,CASE WHEN Address IS NULL THEN (SELECT TOP 1 Address FROM posCustomerAddressTran WHERE linktoCustomerMasterId=@CustomerMasterId AND IsDeleted = 0) ELSE Address END As Address 
		,Gender,Password
	FROM
	(
		SELECT posCustomerMaster.*,
			(select  Address from  posCustomerAddressTran where linktoCustomerMasterId=@CustomerMasterId and IsPrimary = 1 AND IsDeleted = 0) AS Address	
		FROM
			 posCustomerMaster
		WHERE
			CustomerMasterId = ISNULL(@CustomerMasterId,CustomerMasterId)
			and Email1 = ISNULL(@Email1,Email1)
	)TempTable

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCustomerMaster_SelectAll	1,'A','','','',3,False
CREATE PROCEDURE [dbo].[posCustomerMaster_SelectAll]
	 @CustomerType smallint = NULL
	,@CustomerName varchar(100)
	,@Phone1 varchar(15)
	,@IsFavourite bit = NULL
	,@IsCredit bit = NULL
	,@IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint 
AS
BEGIN
	SET NOCOUNT ON

	SELECT DISTINCT
		CM.*		
	FROM
		 posCustomerMaster CM
		 LEFT JOIN posCustomerAddressTran CAT ON CAT.linktoCustomerMasterId=CM.CustomerMasterId
	WHERE
		(CustomerName LIKE @CustomerName + '%' OR ShortName LIKE @CustomerName + '%' OR ContactPersonName LIKE @CustomerName + '%')
		AND (Phone1 LIKE @Phone1 + '%' OR Phone2 LIKE @Phone1  + '%')
		AND ((@CustomerType IS NULL AND (CustomerType IS NULL OR CustomerType IS NOT NULL)) OR (@CustomerType IS NOT NULL AND CustomerType = @CustomerType))
		AND ((@IsFavourite IS NULL AND (IsFavourite IS NULL OR IsFavourite IS NOT NULL)) OR (@IsFavourite IS NOT NULL AND IsFavourite = @IsFavourite))
		AND ((@IsCredit IS NULL AND (IsCredit IS NULL OR IsCredit IS NOT NULL)) OR (@IsCredit IS NOT NULL AND IsCredit = @IsCredit))
		AND ((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND CM.IsDeleted = 0
		AND CM.linktoBusinessMasterId=@linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMaster_Update]
	 @CustomerMasterId int
	,@CustomerType smallint 
	,@ShortName varchar(10)
	,@CustomerName varchar(100)
	,@Description varchar(500) = NULL
	,@ContactPersonName varchar(100) = NULL
	,@Designation varchar(50) = NULL
	,@Birthdate datetime = null
	,@AnniversaryDate datetime = null
	,@Phone1 varchar(15)
	,@IsPhone1DND bit
	,@Phone2 varchar(15) = NULL
	,@IsPhone2DND bit = NULL
	,@Email1 varchar(80) = NULL
	,@Email2 varchar(80) = NULL
	,@Fax varchar(15) = NULL
	,@ImageName varchar(100) = NULL
	,@IsFavourite bit = NULL	
	,@IsCredit bit
	,@OpeningBalance money
	,@CreditDays smallint
	,@CreditBalance money
	,@CreditLimit money
	,@linktoBusinessMasterId smallint
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoMembershipTypeMasterId smallint = NULL
	,@TotalPoints smallint = NULL
	,@MembershipCardExpiryDate datetime = NULL
	,@MembershipCardNumber varchar(50) = NULL
	,@Status smallint OUTPUT
	,@TotalAmount Money= null
	
AS
BEGIN
	SET NOCOUNT OFF

	SET @Email1 = ISNULL(@Email1, '')

	IF EXISTS(SELECT CustomerMasterId FROM posCustomerMaster WHERE ((@Phone1 <> '' AND Phone1 = @Phone1) OR (@Email1 <> '' AND Email1 = @Email1)) 
		AND CustomerMasterId != @CustomerMasterId AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId AND CustomerType=@CustomerType)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posCustomerMaster
	SET
         CustomerType = @CustomerType
		 ,ShortName = @ShortName
		,CustomerName = @CustomerName
		,Description = @Description
		,ContactPersonName = @ContactPersonName
		,Designation = @Designation
		,Birthdate = @Birthdate
	    ,AnniversaryDate = @AnniversaryDate
		,Phone1 = @Phone1
		,IsPhone1DND = @IsPhone1DND
		,Phone2 = @Phone2
		,IsPhone2DND = @IsPhone2DND
		,Email1 = @Email1
		,Email2 = @Email2
		,Fax = @Fax
		,ImageName = @ImageName
		,IsFavourite = @IsFavourite
		,IsCredit = @IsCredit
		,OpeningBalance = @OpeningBalance
		,CreditDays = @CreditDays
		,CreditBalance = @CreditBalance
		,CreditLimit = @CreditLimit
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,MembershipCardNumber=@MembershipCardNumber
		,MembershipCardExpiryDate=@MembershipCardExpiryDate
		,linktoMembershipTypeMasterId=@linktoMembershipTypeMasterId
		,TotalPoints=@TotalPoints
		,TotalAmount=@TotalAmount
		
	WHERE
		CustomerMasterId = @CustomerMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterByBookingMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterByBookingMasterId_SelectAll]

@linktoBookingMasterId int
,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	 SELECT
		DISTINCT
			CM.CustomerMasterId
			,CM.CreditBalance
			,CM.CustomerName
		FROM
			posCustomerMaster CM INNER JOIN
			posBookingPaymentTran BT
		ON
			CM.CustomerMasterId = BT.linktoCustomerMasterId
		WHERE
			BT.linktoBookingMasterId = @linktoBookingMasterId
			and cm.linktoBusinessMasterId=@linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterById_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterById_Update]
	 @CustomerMasterId int
	,@CustomerName varchar(100)
	,@Birthdate datetime = null
	,@Phone1 varchar(15)
	,@Gender varchar(6)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posCustomerMaster
	SET
        CustomerName = @CustomerName
		,Birthdate = @Birthdate
		,Phone1 = @Phone1
		,Gender = @Gender
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		CustomerMasterId = @CustomerMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterByNameShortNameAndMobile_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCustomerMasterByNameShortNameAndMobile_SelectAll '',1,1
CREATE PROCEDURE [dbo].[posCustomerMasterByNameShortNameAndMobile_SelectAll]
	
	@CustomerName varchar(100)
	,@CustomerType smallint = null
	,@IsCredit bit = null
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON
	SELECT  
	DISTINCT
		CustomerMasterId,ShortName,CustomerName,Description,ContactPersonName,Designation,Phone1,IsPhone1DND,Phone2,IsPhone2DND,Email1,Email2,
		Fax,ImageName,Age,BirthDate,AnniversaryDate,CustomerType,IsFavourite,IsCredit,OpeningBalance,CreditDays,CreditBalance,CreditLimit,linktoBusinessMasterId,
		CreateDateTime,linktoUserMasterIdCreatedBy,UpdateDateTime,linktoUserMasterIdUpdatedBy,IsEnabled,IsDeleted,linktoMembershipTypeMasterId,linktoDiscountMasterId, DiscountTitle
		,CASE WHEN Address IS NULL THEN (SELECT Address FROM posCustomerAddressTran WHERE linktoCustomerMasterId = CustomerMasterId AND IsDeleted = 0) ELSE Address END AS Address 
	FROM
	(
		SELECT CSTM.*
			,(SELECT  Address FROM  posCustomerAddressTran WHERE linktoCustomerMasterId=CSTM.CustomerMasterId and IsPrimary = 1 AND IsDeleted = 0) AS Address	
			,(SELECT DiscountTitle FROM posDiscountMaster WHERE DiscountMasterId = linktoDiscountMasterId) AS DiscountTitle
		FROM
			 posCustomerMaster CstM
		WHERE
			(CstM.ShortName LIKE @CustomerName + '%' OR CstM.CustomerName LIKE @CustomerName + '%' OR CstM.Phone1 LIKE @CustomerName+ '%') AND
			CstM.IsEnabled = 1 AND CstM.IsDeleted = 0
			AND CustomerType=ISNULL(@CustomerType,CustomerType)
			AND CstM.IsCredit=ISNULL(@IsCredit,CstM.IsCredit)
			AND linktoBusinessMasterId=@linktoBusinessMasterId
	) TempTable	
	
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterBySalesMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterBySalesMasterId_SelectAll]
	@linktoSalesMasterId int
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	 SELECT
		DISTINCT
			CM.CustomerMasterId
			,CM.CreditBalance
			,CM.CustomerName
		FROM
			posCustomerMaster CM 
		INNER JOIN
			posSalesPaymentTran SPT
		ON
			CM.CustomerMasterId = SPT.linktoCustomerMasterId
		WHERE
			SPT.linktoSalesMasterId = @linktoSalesMasterId AND CM.linktoBusinessMasterId = @linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterChekMembershipCardNumber_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posCustomerMasterChekMembershipCardNumber_Select 4494551881,4
CREATE PROCEDURE [dbo].[posCustomerMasterChekMembershipCardNumber_Select] 
	@MembershipCardNumber  varchar(12)=NULL
	,@MembershipTypeMasterId  smallint
	,@linktoBusinessMasterId smallint
AS
BEGIN
	SET NOCOUNT ON;

	IF NOT EXISTS(SELECT CustomerMasterId FROM posCustomerMaster WHERE MembershipCardNumber=@MembershipCardNumber AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT CASE WHEN IsCardOfPoint= 1 THEN NewCardBonusPoints ELSE NewCardBonusAmount END AS TotalPointORAmount,
		IsCardOfPoint,
		CASE WHEN ValidMonths IS NULL THEN 'Life Time Validity' ELSE Convert(VARCHAR, DATEADD(m,ValidMonths, CreateDateTime)) END AS CardValidDate 
	
		,(SELECT CustomerMasterId FROM posCustomerMaster WHERE MembershipCardNumber=@MembershipCardNumber AND linktoBusinessMasterId=@linktoBusinessMasterId) AS CustomerMasterId
		FROM posMembershipType WHERE MembershipTypeMasterId=@MembershipTypeMasterId 
	END
	ELSE
	BEGIN
		SELECT CustomerMasterId FROM posCustomerMaster WHERE MembershipCardNumber= @MembershipCardNumber AND linktoBusinessMasterId=@linktoBusinessMasterId
	END
END
 


GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterCreditBalance_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterCreditBalance_Update]
	 @CustomerMasterId int
	,@CreditBalance money
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint

	,@Status smallint OUTPUT

AS
BEGIN
	SET NOCOUNT OFF 

		UPDATE posCustomerMaster
		SET
			CreditBalance =  @CreditBalance
			,UpdateDateTime=@UpdateDateTime
			,linktoUserMasterIdUpdatedBy= @linktoUserMasterIdUpdatedBy
		WHERE
			CustomerMasterId = @CustomerMasterId 

		IF @@ERROR <> 0
		BEGIN
			SET @Status = -1
		END
		ELSE
		BEGIN
			SET @Status = 0
		END

	
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterCustomerName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterCustomerName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 CustomerMasterId
		,CustomerName
	FROM
		 posCustomerMaster
	WHERE
		IsEnabled = 1
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY CustomerName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterCustomerNameByCustomerTypeWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posCustomerMasterCustomerNameByCustomerTypeWise_SelectAll 3
CREATE PROCEDURE [dbo].[posCustomerMasterCustomerNameByCustomerTypeWise_SelectAll]

	@CustomerType smallint
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 CustomerMasterId
		,CustomerName
	FROM
		 posCustomerMaster
	WHERE
		IsEnabled = 1
		AND IsDeleted = 0
		AND CustomerType = @CustomerType
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY CustomerName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterDiscountAssignment_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posDiscountCustomerTran_SelectAll 2
CREATE PROCEDURE [dbo].[posCustomerMasterDiscountAssignment_SelectAll] 
	
	@linktoBusinessMasterId smallint
AS
BEGIN 
	SET NOCOUNT ON 
	SELECT  
		 CM.CustomerName
		,CM.CustomerMasterId  
		,CM.Email1
		,CM.Phone1
		,CM.CustomerType
		,CM.linktoDiscountMasterId
		,DM.DiscountTitle
		 
	FROM
	   posCustomerMaster  CM
	LEFT JOIN
		posDiscountMaster DM ON DM.DiscountMasterId = CM.linktoDiscountMasterId 
	WHERE
		CM.IsDeleted = 0 AND CM.IsEnabled = 1  
		AND CM.linktoBusinessMasterId=@linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterlinktoDiscountMasterId_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterlinktoDiscountMasterId_Update]
	@linktoDiscountMasterId int
	,@CustomerMasterIDs varchar(500)
	,@Status smallint OUTPUT
AS
BEGIN
	
	UPDATE 
		posCustomerMaster 
	SET 
		linktoDiscountMasterId = NULL 
	WHERE 
		linktoDiscountMasterId = @linktoDiscountMasterId

	UPDATE 
		posCustomerMaster 
	SET 
		linktoDiscountMasterId = @linktoDiscountMasterId 
	WHERE 
		CustomerMasterId IN (SELECT parsevalue FROM dbo.Parse(@CustomerMasterIDs,','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN		
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterMembership_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterMembership_Update]
	 @CustomerMasterId int
	,@linktoMembershipTypeMasterId smallint = NULL
	,@TotalPoints smallint = NULL
	,@MembershipCardExpiryDate datetime = NULL
	,@MembershipCardNumber varchar(50) = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posCustomerMaster
	SET
		 MembershipCardNumber=@MembershipCardNumber
		,MembershipCardExpiryDate=@MembershipCardExpiryDate
		,linktoMembershipTypeMasterId=@linktoMembershipTypeMasterId
		,TotalPoints=@TotalPoints
		
	WHERE
		CustomerMasterId = @CustomerMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterMembershipTypeWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterMembershipTypeWise_SelectAll]
	 @linktoMembershipTypeMasterId smallint = NULL
	,@CustomerName varchar(100)
	,@Phone1 varchar(15)
	,@IsFavourite bit = NULL
	,@IsCredit bit = NULL
	,@IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint 
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	SELECT DISTINCT
		CM.*		
	FROM
		 posCustomerMaster CM
		 LEFT JOIN posCustomerAddressTran CAT ON CAT.linktoCustomerMasterId=CM.CustomerMasterId
	WHERE
		(CustomerName LIKE @CustomerName + '%' OR ShortName LIKE @CustomerName + '%' OR ContactPersonName LIKE @CustomerName + '%')
		AND (Phone1 LIKE @Phone1 + '%' OR Phone2 LIKE @Phone1  + '%')
		AND ((@linktoMembershipTypeMasterId IS NULL AND (linktoMembershipTypeMasterId IS NULL OR linktoMembershipTypeMasterId IS NOT NULL)) OR (@linktoMembershipTypeMasterId IS NOT NULL AND linktoMembershipTypeMasterId = @linktoMembershipTypeMasterId))
		AND ((@IsFavourite IS NULL AND (IsFavourite IS NULL OR IsFavourite IS NOT NULL)) OR (@IsFavourite IS NOT NULL AND IsFavourite = @IsFavourite))
		AND ((@IsCredit IS NULL AND (IsCredit IS NULL OR IsCredit IS NOT NULL)) OR (@IsCredit IS NOT NULL AND IsCredit = @IsCredit))
		AND ((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND CM.IsDeleted = 0
		AND CM.linktoBusinessMasterId=@linktoBusinessMasterId
	RETURN
	
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterPassword_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterPassword_Update]
	 @CustomerMasterId int
	,@Password varchar(25)
	,@OldPassword varchar(25)
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT Password FROM posCustomerMaster WHERE CustomerMasterId = @CustomerMasterId AND Password = @OldPassword) OR @OldPassword = 'New'
	BEGIN
		UPDATE posCustomerMaster
		SET
			Password = @Password
			,UpdateDateTime = @UpdateDateTime
			,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		WHERE
			CustomerMasterId = @CustomerMasterId

		IF @@ERROR <> 0
			SET @Status = -1
		ELSE
			SET @Status = 0

		RETURN
	END		
	ELSE
	BEGIN
		SET @Status = -3
		RETURN
	END	
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterRegisteredUser_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- [posRegisteredUserMaster_SelectAll] 0
CREATE PROCEDURE [dbo].[posCustomerMasterRegisteredUser_SelectAll]
 @linktoOfferMasterId smallint,
 @linktoBusinessMasterId smallint,
 @CustomerType smallint
AS
BEGIN

	SET NOCOUNT ON		

		SELECT 
			CM.CustomerName ,CM.Phone1,CM.CustomerMasterId,CM.Email1,OCT.OfferCodesTranId,OCT.OfferCode
		FROM
			posCustomerMaster CM
	LEFT JOIN 
			posOfferCodesTran OCT ON OCT.linktoCustomerMasterId = CM.CustomerMasterId AND OCT.linktoOfferMasterId = @linktoOfferMasterId
		WHERE	
			 IsEnabled=1 AND CM.linktoBusinessMasterId = @linktoBusinessMasterId AND CM.CustomerType=@CustomerType

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterRemoveDiscount_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerMasterRemoveDiscount_Update]
	@CustomerMasterIDs varchar(500)
	,@Status smallint OUTPUT
AS
BEGIN
	UPDATE 
		posCustomerMaster 
	SET 
		linktoDiscountMasterId = NULL 
	WHERE 
		CustomerMasterId IN (SELECT parsevalue FROM dbo.Parse(@CustomerMasterIDs,','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN		
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerMasterReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 -- posCustomerMasterReport_SelectAll 0,0,0        
CREATE PROCEDURE [dbo].[posCustomerMasterReport_SelectAll]
	@linktoCityMasterId int = NULL,
	@linktoAreaMasterId int = NULL ,
	@IsFavourite bit,
	@linktoBusinessMasterId smallint
AS
BEGIN   
SELECT * FROM
(
	SELECT 
		CustomerMasterId,CustomerName,Phone1,BirthDate,AnniversaryDate,CustomerAddressTranId,linktoBusinessMasterId,
		CASE WHEN CustomerAddressTranId IS NULL THEN 
		(
			SELECT TOP 1 CityName
			FROM posCustomerAddressTran CAT JOIN posCityMaster City ON City.CityMasterId = CAT.linktoCityMasterId				
			WHERE linktoCustomerMasterId = CustomerMasterId						
		)
		ELSE CityName END CityName,
		CASE WHEN CustomerAddressTranId IS NULL THEN 
		(
			SELECT TOP 1 AreaName
			FROM posCustomerAddressTran CAT JOIN posAreaMaster AM ON AM.AreaMasterId = CAT.linktoAreaMasterId 			
			WHERE linktoCustomerMasterId = CustomerMasterId				
		)
		ELSE AreaName END AreaName,
		CASE WHEN CustomerAddressTranId IS NULL THEN 
		(
			SELECT TOP 1 linktoCityMasterId
			FROM posCustomerAddressTran CAT JOIN posCityMaster City ON City.CityMasterId = CAT.linktoCityMasterId				
			WHERE linktoCustomerMasterId = CustomerMasterId						
		)
		ELSE linktoCityMasterId END linktoCityMasterId,
		CASE WHEN CustomerAddressTranId IS NULL THEN 
		(
			SELECT TOP 1 linktoAreaMasterId
			FROM posCustomerAddressTran CAT JOIN posAreaMaster AM ON AM.AreaMasterId = CAT.linktoAreaMasterId 			
			WHERE linktoCustomerMasterId = CustomerMasterId						
		)
		ELSE linktoAreaMasterId END linktoAreaMasterId
	FROM
	(
		SELECT 
			CustomerMasterId,CustomerName,Phone1,BirthDate,AnniversaryDate,AreaName,CityName,CustomerAddressTranId,CAT.linktoCityMasterId,linktoAreaMasterId,CM.linktoBusinessMasterId
		FROM
			posCustomerMaster CM
		LEFT JOIN
			posCustomerAddressTran CAT ON linktoCustomerMasterId = CM.CustomerMasterId AND CAT.IsPrimary = 1					
		LEFT JOIN 
			posCityMaster City ON City.CityMasterId = CAT.linktoCityMasterId
		LEFT JOIN 
			posAreaMaster AM ON AM.AreaMasterId = CAT.linktoAreaMasterId 
		WHERE
			CM.IsEnabled = 1 AND CM.IsDeleted = 0
			AND CM.IsFavourite = CASE WHEN @IsFavourite = 0 THEN CM.IsFavourite ELSE @IsFavourite END 
	)TEMP_TABLE 
)Tmp_Table
WHERE 
	((@linktoAreaMasterId IS NULL AND (linktoAreaMasterId IS NULL OR linktoAreaMasterId IS NOT NULL)) OR (@linktoAreaMasterId IS NOT NULL AND linktoAreaMasterId = @linktoAreaMasterId))
	AND ((@linktoCityMasterId IS NULL AND (linktoCityMasterId IS NULL OR linktoCityMasterId IS NOT NULL)) OR (@linktoCityMasterId IS NOT NULL AND linktoCityMasterId = @linktoCityMasterId))
	AND linktoBusinessMasterId=@linktoBusinessMasterId
 END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerPaymentTran_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCustomerPaymentTran_DeleteAll]
	 @CustomerPaymentTranIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posCustomerPaymentTran
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		CustomerPaymentTranId IN (SELECT * from dbo.Parse(@CustomerPaymentTranIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerPaymentTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerPaymentTran_Insert]
	 @CustomerPaymentTranId int OUTPUT
	,@linktoCustomerMasterId int
	,@linktoCustomerInvoiceMasterId int = NULL
	,@linktoPaymentTypeMasterId smallint
	,@PaymentDate datetime
	,@ReceiptNo varchar(20)
	,@TotalAmount money
	,@Remark varchar(250) = NULL
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posCustomerPaymentTran
	(
		 linktoCustomerMasterId
		,linktoCustomerInvoiceMasterId
		,linktoPaymentTypeMasterId
		,PaymentDate
		,ReceiptNo
		,TotalAmount
		,Remark
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoCustomerMasterId
		,@linktoCustomerInvoiceMasterId
		,@linktoPaymentTypeMasterId
		,@PaymentDate
		,@ReceiptNo
		,@TotalAmount
		,@Remark
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @CustomerPaymentTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerPaymentTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerPaymentTran_Select]
	 @CustomerPaymentTranId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posCustomerPaymentTran.*
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer
		,(SELECT InvoiceNo FROM posCustomerInvoiceMaster WHERE CustomerInvoiceMasterId = linktoCustomerInvoiceMasterId) AS CustomerInvoice
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType
	FROM
		 posCustomerPaymentTran
	WHERE
		CustomerPaymentTranId = @CustomerPaymentTranId

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posCustomerPaymentTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posCustomerPaymentTran_SelectAll]

@FromDate Datetime,
@ToDate Datetime,
@linktoCustomerMasterId int,
@linktoCustomerInvoiceMasterId int,
@ReceiptNo varchar(20)

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posCustomerPaymentTran.*
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer		
		,(SELECT InvoiceNo FROM posCustomerInvoiceMaster WHERE CustomerInvoiceMasterId = linktoCustomerInvoiceMasterId) AS CustomerInvoice		
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType
	FROM
		 posCustomerPaymentTran
	WHERE
		IsDeleted = 0
		AND ReceiptNo Like '%' + @ReceiptNo+ '%'
		AND CONVERT(varchar(8), PaymentDate ,112) BETWEEN CONVERT(varchar(8), @FromDate ,112) AND CONVERT(varchar(8), @ToDate ,112)
		AND linktoCustomerMasterId=@linktoCustomerMasterId
		AND ISNULL(linktoCustomerInvoiceMasterId,0) = CASE WHEN  @linktoCustomerInvoiceMasterId = 0 THEN ISNULL(linktoCustomerInvoiceMasterId,0) ELSE @linktoCustomerInvoiceMasterId END
	ORDER BY CustomerPaymentTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posCustomerPaymentTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posCustomerPaymentTran_Update]
	 @CustomerPaymentTranId int
	,@linktoCustomerMasterId int
	,@linktoCustomerInvoiceMasterId int = NULL
	,@linktoPaymentTypeMasterId smallint
	,@PaymentDate datetime
	,@ReceiptNo varchar(20)
	,@TotalAmount money
	,@Remark varchar(250) = NULL
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posCustomerPaymentTran
	SET
		 linktoCustomerMasterId = @linktoCustomerMasterId
		,linktoCustomerInvoiceMasterId = @linktoCustomerInvoiceMasterId
		,linktoPaymentTypeMasterId = @linktoPaymentTypeMasterId
		,PaymentDate = @PaymentDate
		,ReceiptNo = @ReceiptNo
		,TotalAmount = @TotalAmount
		,Remark = @Remark
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		CustomerPaymentTranId = @CustomerPaymentTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDenominationMaster_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDenominationMaster_Delete]
	 @DenominationMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posDenominationMaster
	WHERE
		DenominationMasterId = @DenominationMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDenominationMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDenominationMaster_Insert]
	 @DenominationMasterId smallint OUTPUT
	,@Denomination money
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT DenominationMasterId FROM posDenominationMaster WHERE Denomination = @Denomination AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @DenominationMasterId = DenominationMasterId FROM posDenominationMaster WHERE Denomination = @Denomination AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posDenominationMaster
	(
		 Denomination,
		 linktoBusinessMasterId
	)
	VALUES
	(
		 @Denomination,
		 @linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @DenominationMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDenominationMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDenominationMaster_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posDenominationMaster.*
	FROM
		 posDenominationMaster
	WHERE
		linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY 
		Denomination ASC

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDepartmentMaster_DeleteAll]
	 @DepartmentMasterIds varchar(1000)
	 ,@linktoUserMasterIdUpdatedBy smallint
	 ,@UpdateDateTime datetime
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecoeds int ,@UpdatedRawCount int
	set @TotalRecoeds = (SELECT count(*) FROM dbo.Parse(@DepartmentMasterIds, ','))

	UPDATE
		posDepartmentMaster
	SET 
		IsDeleted = 1,
		linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy,
		UpdateDateTime = @UpdateDateTime,
		TransactionDateTime = @UpdateDateTime,
		IsSync = 0
	WHERE
		DepartmentMasterId IN (SELECT * from dbo.Parse(@DepartmentMasterIds, ','))
		AND DepartmentMasterId NOT IN 
		(
			SELECT linktoDepartmentMasterId FROM posCounterMaster WHERE IsDeleted = 0 AND IsEnabled = 1 
			AND linktoDepartmentMasterId IN (SELECT * FROM dbo.Parse(@DepartmentMasterIds, ','))
		)

	SET @UpdatedRawCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		if @TotalRecoeds = @UpdatedRawCount
		begin
			SET @Status = 0
		end
		else
		begin
			SET @Status = -2
		end
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDepartmentMaster_Insert]
	 @DepartmentMasterId smallint OUTPUT
	,@ShortName varchar(10)
	,@DepartmentName varchar(50)
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoBusinessMasterId smallint
	,@IsSync bit
	,@SyncId smallint = NULL
	,@TransactionDateTime datetime = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT DepartmentMasterId FROM posDepartmentMaster WHERE DepartmentName = @DepartmentName AND IsDeleted = 0 AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @DepartmentMasterId = DepartmentMasterId FROM posDepartmentMaster WHERE DepartmentName = @DepartmentName AND IsDeleted = 0 AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posDepartmentMaster
	(
		 ShortName
		,DepartmentName
		,Description
		,IsEnabled
		,IsDeleted
		,linktoBusinessMasterId
		,IsSync
		,SyncId
		,TransactionDateTime
	)
	VALUES
	(
		 @ShortName
		,@DepartmentName
		,@Description
		,@IsEnabled
		,@IsDeleted
		,@linktoBusinessMasterId
		,@IsSync
		,@SyncId
		,@TransactionDateTime
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @DepartmentMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END








GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDepartmentMaster_Select]
	 @DepartmentMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posDepartmentMaster.*
	FROM
		 posDepartmentMaster
	WHERE
		DepartmentMasterId = @DepartmentMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDepartmentMaster_SelectAll]
	 @IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posDepartmentMaster.*
	FROM
		 posDepartmentMaster
	WHERE
		((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY DepartmentName
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDepartmentMaster_Update]
	 @DepartmentMasterId smallint
	,@ShortName varchar(10)
	,@DepartmentName varchar(50)
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@linktoBusinessMasterId smallint
	,@IsSync bit
	,@SyncId smallint = NULL
	,@TransactionDateTime datetime
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT DepartmentMasterId FROM posDepartmentMaster WHERE DepartmentName = @DepartmentName AND DepartmentMasterId != @DepartmentMasterId AND IsDeleted = 0  AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posDepartmentMaster
	SET
		 ShortName = @ShortName
		,DepartmentName = @DepartmentName
		,Description = @Description
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,IsSync = @IsSync
		,SyncId = @SyncId
		,TransactionDateTime = @TransactionDateTime
	WHERE
		DepartmentMasterId = @DepartmentMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMasterDepartmentName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDepartmentMasterDepartmentName_SelectAll]

	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 DepartmentMasterId
		,DepartmentName
	FROM
		 posDepartmentMaster
	WHERE
		IsEnabled = 1
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY DepartmentName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMasterForSync_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posDepartmentMasterForSync_SelectAll 1
CREATE PROCEDURE [dbo].[posDepartmentMasterForSync_SelectAll] 
	@linktoBusinessMasterId smallint
AS
BEGIN
	SELECT 
		DepartmentMasterId,ShortName,DepartmentName,Description,IsEnabled,IsDeleted,linktoBusinessMasterId,IsSync,SyncId,TransactionDateTime
	FROM
		abPOS.dbo.posDepartmentMaster
	WHERE
		IsSync = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId --AND IsDeleted = 0
END



GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMasterSync_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDepartmentMasterSync_Update] 
	@DepartmentMasterId smallint,
	@SyncId smallint
AS
BEGIN
	UPDATE
		abPOS.dbo.posDepartmentMaster
	SET
		IsSync = 1,SyncId = @SyncId	
	WHERE 
		DepartmentMasterId = @DepartmentMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posDepartmentMasterSyncId_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDepartmentMasterSyncId_Update]
	@DepartmentName varchar(50),
	@DepartmentMasterId smallint,
	@linktoBusinessMasterId smallint,
	@CurrentDateTime datetime,
	@ShortName varchar(10),
	@Description varchar(500),
	@IsEnabled bit,
	@IsDeleted bit,
	@TransactionDateTime datetime
AS
BEGIN
	DECLARE @IsSync bit,@pos_TransactionDateTime datetime,@pos_DepartmentMasterId smallint,@pos_linktoUserMasterIdUpdatedBy smallint
	
	CREATE TABLE #TEMP(ID int identity(1,1),posw_DepartmentMasterId smallint,pos_DepartmentMasterId smallint)

	IF EXISTS(SELECT DepartmentMasterId FROM abPOS.dbo.posDepartmentMaster WHERE SyncId = @DepartmentMasterId)
	BEGIN		
		SELECT 
			@pos_DepartmentMasterId = DepartmentMasterId,@IsSync = IsSync,@pos_TransactionDateTime = TransactionDateTime,
			@pos_linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy 
		FROM abPOS.dbo.posDepartmentMaster WHERE SyncId = @DepartmentMasterId

		IF @IsSync = 0 AND (CONVERT(DATETIME,@pos_TransactionDateTime) > CONVERT(DATETIME,@TransactionDateTime))
		BEGIN			
			--UPDATE 
			--	abPOS.dbo.posDepartmentMaster
			--SET
			--	IsSync = 1
			--WHERE
			--	SyncId = @DepartmentMasterId	
				
			INSERT INTO #TEMP
			VALUES(@DepartmentMasterId,@pos_DepartmentMasterId)			
		END
		ELSE
		BEGIN
			EXEC abpos.dbo.posDepartmentMaster_Update @pos_DepartmentMasterId,@ShortName,@DepartmentName,@Description,@IsEnabled,@IsDeleted,@CurrentDateTime
				,@pos_linktoUserMasterIdUpdatedBy,@linktoBusinessMasterId,1,@DepartmentMasterId,@pos_TransactionDateTime,1

			INSERT INTO #TEMP
			VALUES(@DepartmentMasterId,@pos_DepartmentMasterId)
		END			
	END
	ELSE
	BEGIN
		IF EXISTS(SELECT DepartmentMasterId FROM abPOS.dbo.posDepartmentMaster WHERE DepartmentName = @DepartmentName AND linktoBusinessMasterId = @linktoBusinessMasterId)
		BEGIN
			
			SELECT 
				@pos_DepartmentMasterId = DepartmentMasterId,@IsSync = IsSync,@pos_TransactionDateTime = TransactionDateTime,
				@pos_linktoUserMasterIdUpdatedBy = linktoUserMasterIdUpdatedBy  
			FROM abPOS.dbo.posDepartmentMaster 
			WHERE DepartmentName = @DepartmentName AND linktoBusinessMasterId = @linktoBusinessMasterId
			
			IF @IsSync = 0 AND (CONVERT(DATETIME,@pos_TransactionDateTime) > CONVERT(DATETIME,@TransactionDateTime))
			BEGIN			
				--UPDATE 
				--	abPOS.dbo.posDepartmentMaster
				--SET
				--	IsSync = 1,SyncId = @DepartmentMasterId
				--WHERE
				--	DepartmentName = @DepartmentName AND linktoBusinessMasterId = @linktoBusinessMasterId
				
				INSERT INTO #TEMP
				VALUES(@DepartmentMasterId,@pos_DepartmentMasterId)						
			END
			ELSE
			BEGIN
				EXEC abpos.dbo.posDepartmentMaster_Update @pos_DepartmentMasterId,@ShortName,@DepartmentName,@Description,@IsEnabled,@IsDeleted,@CurrentDateTime
				,@pos_linktoUserMasterIdUpdatedBy,@linktoBusinessMasterId,1,@DepartmentMasterId,@pos_TransactionDateTime,1
				
				INSERT INTO #TEMP
				VALUES(@DepartmentMasterId,@pos_DepartmentMasterId)
			END			
		END
		ELSE
		BEGIN
			EXEC abPOS.dbo.posDepartmentMaster_Insert @pos_DepartmentMasterId,@ShortName,@DepartmentName,@Description,@IsEnabled,@IsDeleted,@linktoBusinessMasterId
				,1,@DepartmentMasterId,@CurrentDateTime,1
			
			INSERT INTO #TEMP
			VALUES(@DepartmentMasterId,@@IDENTITY)			
		END
	END

	SELECT * FROM #TEMP

	DROP TABLE #TEMP 
END



GO
/****** Object:  StoredProcedure [dbo].[posDesignationMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDesignationMaster_DeleteAll]
	 @DesignationMasterIds varchar(1000)
	,@linktoUserMasterIdUpdatedBy smallint
	,@UpdateDateTime datetime	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int
	DECLARE @RowCount int

	set	@TotalRecords= (SELECT COUNT(*) FROM dbo.Parse(@DesignationMasterIds, ','))
	
	UPDATE
		posDesignationMaster
	SET IsDeleted = 1
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,UpdateDateTime = @UpdateDateTime
	WHERE
		DesignationMasterId IN (SELECT * from dbo.Parse(@DesignationMasterIds, ','))
		AND DesignationMasterId NOT IN 
		(
			SELECT linktoDesignationMasterId FROM posEmployeeMaster WHERE IsDeleted = 0 AND IsEnabled = 1 
			AND linktoDesignationMasterId IN (SELECT * FROM dbo.Parse(@DesignationMasterIds, ','))
		)
		SET @RowCount=@@ROWCOUNT
	
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		IF @TotalRecords = @RowCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDesignationMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDesignationMaster_Insert]
	 @DesignationMasterId smallint OUTPUT
	,@ShortName varchar(10)
	,@DesignationName varchar(50)
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT DesignationMasterId FROM posDesignationMaster WHERE DesignationName = @DesignationName AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @DesignationMasterId = DesignationMasterId FROM posDesignationMaster WHERE DesignationName = @DesignationName AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posDesignationMaster
	(
		 ShortName
		,DesignationName
		,Description
		,IsEnabled
		,IsDeleted
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @ShortName
		,@DesignationName
		,@Description
		,@IsEnabled
		,@IsDeleted
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @DesignationMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDesignationMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDesignationMaster_Select]
	 @DesignationMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posDesignationMaster.*
	FROM
		 posDesignationMaster
	WHERE
		DesignationMasterId = @DesignationMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDesignationMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDesignationMaster_SelectAll]
	 @IsEnabled bit = NULL,
	 @linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posDesignationMaster.*
	FROM
		 posDesignationMaster
	WHERE
		IsEnabled = ISNULL(@IsEnabled, IsEnabled)
		AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDesignationMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDesignationMaster_Update]
	 @DesignationMasterId smallint
	,@ShortName varchar(10)
	,@DesignationName varchar(50)
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT DesignationMasterId FROM posDesignationMaster WHERE DesignationName = @DesignationName AND DesignationMasterId != @DesignationMasterId
					AND linktoBusinessMasterId = @linktoBusinessMasterId AND IsDeleted = 0)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posDesignationMaster
	SET
		 ShortName = @ShortName
		,DesignationName = @DesignationName
		,Description = @Description
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		DesignationMasterId = @DesignationMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDesignationMasterDesignationName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDesignationMasterDesignationName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 DesignationMasterId
		,DesignationName
	FROM
		 posDesignationMaster
	WHERE
		IsEnabled = 1 AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY DesignationName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountMaster_DeleteAll]
	 @DiscountMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRawCount int

	set @TotalRecords = (SELECT count(*) FROM dbo.Parse(@DiscountMasterIds, ','))


	DELETE
	FROM
		 posDiscountMaster
	WHERE
		DiscountMasterId IN (SELECT * from dbo.Parse(@DiscountMasterIds, ','))
		AND DiscountMasterId NOT IN
		(
			SELECT linktoDiscountMasterId FROM posCustomerMaster WHERE IsEnabled =1 AND IsDeleted =0 
			AND linktoDiscountMasterId IN (SELECT * from dbo.Parse(@DiscountMasterIds, ','))
		)

	SET @UpdatedRawCount = @@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
			if @TotalRecords = @UpdatedRawCount
		begin
			SET @Status = 0
		end
		else
		begin
			SET @Status = -2
		end
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountMaster_Insert]
	 @DiscountMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@DiscountTitle varchar(50)
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy int
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT DiscountMasterId FROM posDiscountMaster WHERE DiscountTitle = @DiscountTitle AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @DiscountMasterId = DiscountMasterId FROM posDiscountMaster WHERE DiscountTitle = @DiscountTitle AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posDiscountMaster
	(
		 linktoBusinessMasterId
		,DiscountTitle
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@DiscountTitle
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @DiscountMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountMaster_Select]
	 @DiscountMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posDiscountMaster.*		
	FROM
		 posDiscountMaster
	WHERE
		DiscountMasterId = @DiscountMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountMaster_SelectAll]

	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posDiscountMaster.*		
	FROM
		 posDiscountMaster
	WHERE 	
		linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY DiscountTitle

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountMaster_Update]
	 @DiscountMasterId int
	,@linktoBusinessMasterId smallint
	,@DiscountTitle varchar(50)
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy int
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT DiscountMasterId FROM posDiscountMaster WHERE DiscountTitle = @DiscountTitle AND DiscountMasterId != @DiscountMasterId AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posDiscountMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,DiscountTitle = @DiscountTitle
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		DiscountMasterId = @DiscountMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountMasterDiscountTitle_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountMasterDiscountTitle_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 DiscountMasterId
		,DiscountTitle
	FROM
		 posDiscountMaster
	WHERE 	
		linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY DiscountTitle

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountSelectionMaster_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posDiscountSelectionMaster_Delete]
	 @DiscountSelectionMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posDiscountSelectionMaster
	WHERE
		DiscountSelectionMasterId = @DiscountSelectionMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountSelectionMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountSelectionMaster_Insert]
	 @DiscountSelectionMasterId smallint OUTPUT
	,@Discount numeric(6, 2)
	,@IsPercentage bit
	,@DiscountTitle varchar(50)
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT DiscountSelectionMasterId FROM posDiscountSelectionMaster WHERE DiscountTitle = @DiscountTitle AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @DiscountSelectionMasterId = DiscountSelectionMasterId FROM posDiscountSelectionMaster WHERE DiscountTitle = @DiscountTitle AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posDiscountSelectionMaster
	(
		 Discount
		,IsPercentage
		,DiscountTitle
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @Discount
		,@IsPercentage
		,@DiscountTitle
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @DiscountSelectionMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountSelectionMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountSelectionMaster_Select]
	 @DiscountSelectionMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posDiscountSelectionMaster.*
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posDiscountSelectionMaster
	WHERE
		DiscountSelectionMasterId = @DiscountSelectionMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountSelectionMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountSelectionMaster_SelectAll]
	@linktoBusinessMasterId smallint = NULL
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posDiscountSelectionMaster.*
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posDiscountSelectionMaster
	WHERE 
		linktoBusinessMasterId = ISNULL(@linktoBusinessMasterId,linktoBusinessMasterId)
	ORDER BY 
		DiscountTitle ASC

	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posDiscountSelectionMasterDiscountTitle_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountSelectionMasterDiscountTitle_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 DiscountSelectionMasterId
		,DiscountTitle
	FROM
		 posDiscountSelectionMaster
	ORDER BY DiscountTitle

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountTran_Delete]
	 @linktoDiscountMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posDiscountTran
	WHERE
		linktoDiscountMasterId = @linktoDiscountMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posDiscountTran_Insert]
	 @DiscountTranId int OUTPUT
	,@linktoDiscountMasterId int
	,@linktoItemMasterId int
	,@Discount money
	,@IsPercentage bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posDiscountTran
	(
		 linktoDiscountMasterId
		,linktoItemMasterId
		,Discount
		,IsPercentage
	)
	VALUES
	(
		 @linktoDiscountMasterId
		,@linktoItemMasterId
		,@Discount
		,@IsPercentage
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @DiscountTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posDiscountTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posDiscountTran_SelectAll 1
CREATE PROCEDURE [dbo].[posDiscountTran_SelectAll]
	@linktoDiscountMasterId int
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT DISTINCT ItemName,ItemMasterId,ItemCode,CategoryName,DiscountTranId,Discount,IsPercentage
	FROM
	(		
		SELECT DISTINCT
			IM.ItemName,IM.ItemMasterId,IM.ItemCode,CM.CategoryName,DT.DiscountTranId
			,DT.Discount,DT.IsPercentage
		FROM	
			posItemMaster IM 	
		JOIN 
			posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
		JOIN	
			posCategoryMaster CM ON  CM.CategoryMasterId = ICT.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
		LEFT JOIN 
			posDiscountTran DT ON DT.linktoItemMasterId = IM.ItemMasterId AND linktoDiscountMasterId = @linktoDiscountMasterId
		WHERE  
			IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
			IM.ItemType = 0  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1
			AND IM.linktoBusinessMasterId=@linktoBusinessMasterId
						   
	UNION ALL
		SELECT DISTINCT 
			IM.ItemName,IM.ItemMasterId,IM.ItemCode,CM.CategoryName,DT.DiscountTranId
			,DT.Discount,DT.IsPercentage
		FROM	
			posItemMaster IM
		JOIN	
			posCategoryMaster CM ON  CM.CategoryMasterId = IM.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
		LEFT JOIN 
			posDiscountTran DT ON DT.linktoItemMasterId = IM.ItemMasterId AND linktoDiscountMasterId = @linktoDiscountMasterId
		WHERE  
			IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
			IM.ItemType = 0  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1	
			AND IM.linktoBusinessMasterId=@linktoBusinessMasterId					 
	)TempTable
	ORDER BY CategoryName  

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posEmployeeMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posEmployeeMaster_DeleteAll]
	 @EmployeeMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posEmployeeMaster
	SET 
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		EmployeeMasterId IN (SELECT * from dbo.Parse(@EmployeeMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posEmployeeMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posEmployeeMaster_Insert]
	 @EmployeeMasterId smallint OUTPUT
	,@EmployeeCode varchar(9)
	,@EmployeeName varchar(100)
	,@ImageName varchar(250) = NULL
	,@BirthDate date
	,@Gender varchar(6)
	,@Address varchar(250)
	,@linktoCountryMasterId smallint
	,@linktoStateMasterId smallint
	,@linktoCityMasterId int
	,@ZipCode varchar(10)
	,@Phone1 varchar(15)
	,@Phone2 varchar(15) = NULL
	,@Email varchar(80) = NULL
	,@Salary money = NULL
	,@JoinDate date = NULL
	,@LeaveDate date = NULL
	,@BloodGroup varchar(3) = NULL
	,@linktoDesignationMasterId smallint
	,@linktoDepartmentMasterId smallint
	,@ReferenceBy varchar(50) = NULL
	,@Remark varchar(250) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoBusinessMasterId smallint
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	IF EXISTS(SELECT EmployeeMasterId FROM posEmployeeMaster WHERE EmployeeName = @EmployeeName AND IsDeleted = 0 and linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @EmployeeMasterId = EmployeeMasterId FROM posEmployeeMaster WHERE EmployeeName = @EmployeeName AND IsDeleted = 0 and linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END

	INSERT INTO posEmployeeMaster
	(
		 EmployeeCode
		,EmployeeName
		,ImageName
		,BirthDate
		,Gender
		,Address
		,linktoCountryMasterId
		,linktoStateMasterId
		,linktoCityMasterId
		,ZipCode
		,Phone1
		,Phone2
		,Email
		,Salary
		,JoinDate
		,LeaveDate
		,BloodGroup
		,linktoDesignationMasterId
		,linktoDepartmentMasterId
		,ReferenceBy
		,Remark
		,IsEnabled
		,IsDeleted
		,linktoBusinessMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @EmployeeCode
		,@EmployeeName
		,@ImageName
		,@BirthDate
		,@Gender
		,@Address
		,@linktoCountryMasterId
		,@linktoStateMasterId
		,@linktoCityMasterId
		,@ZipCode
		,@Phone1
		,@Phone2
		,@Email
		,@Salary
		,@JoinDate
		,@LeaveDate
		,@BloodGroup
		,@linktoDesignationMasterId
		,@linktoDepartmentMasterId
		,@ReferenceBy
		,@Remark
		,@IsEnabled
		,@IsDeleted
		,@linktoBusinessMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @EmployeeMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posEmployeeMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posEmployeeMaster_Select]
	 @EmployeeMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posEmployeeMaster.*
		,(SELECT DesignationName FROM posDesignationMaster WHERE DesignationMasterId = linktoDesignationMasterId) AS Designation
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department
	FROM
		 posEmployeeMaster
	WHERE
		EmployeeMasterId = @EmployeeMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posEmployeeMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posEmployeeMaster_SelectAll]
	 @EmployeeName varchar(100)
	,@linktoDesignationMasterId smallint = NULL
	,@linktoDepartmentMasterId smallint = NULL
	,@IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posEmployeeMaster.*
		,(SELECT DesignationName FROM posDesignationMaster WHERE DesignationMasterId = linktoDesignationMasterId) AS Designation
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department	
		,(SELECT CityName FROM posCityMaster where CityMasterId = linktoCityMasterId) As City 	
	FROM
		 posEmployeeMaster
	WHERE
		(EmployeeCode LIKE @EmployeeName + '%' OR EmployeeName LIKE @EmployeeName + '%')
		AND linktoDesignationMasterId = ISNULL(@linktoDesignationMasterId, linktoDesignationMasterId)
		AND linktoDepartmentMasterId = ISNULL(@linktoDepartmentMasterId, linktoDepartmentMasterId)
		AND ((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posEmployeeMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posEmployeeMaster_Update]
	 @EmployeeMasterId smallint
	,@EmployeeCode varchar(9)
	,@EmployeeName varchar(100)
	,@ImageName varchar(250) = NULL
	,@BirthDate date
	,@Gender varchar(6)
	,@Address varchar(250)
	,@linktoCountryMasterId smallint
	,@linktoStateMasterId smallint
	,@linktoCityMasterId int
	,@ZipCode varchar(10)
	,@Phone1 varchar(15)
	,@Phone2 varchar(15) = NULL
	,@Email varchar(80) = NULL
	,@Salary money = NULL
	,@JoinDate date = NULL
	,@LeaveDate date = NULL
	,@BloodGroup varchar(3) = NULL
	,@linktoDesignationMasterId smallint
	,@linktoDepartmentMasterId smallint
	,@ReferenceBy varchar(50) = NULL
	,@Remark varchar(250) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoBusinessMasterId smallint
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	IF EXISTS(SELECT EmployeeMasterId FROM posEmployeeMaster WHERE EmployeeName = @EmployeeName AND EmployeeMasterId != @EmployeeMasterId AND IsDeleted = 0 and linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @EmployeeMasterId = EmployeeMasterId FROM posEmployeeMaster WHERE EmployeeName = @EmployeeName AND IsDeleted = 0 AND EmployeeMasterId != @EmployeeMasterId and linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	UPDATE posEmployeeMaster
	SET
		 EmployeeCode = @EmployeeCode
		,EmployeeName = @EmployeeName
		,ImageName = @ImageName
		,BirthDate = @BirthDate
		,Gender = @Gender
		,Address = @Address
		,linktoCountryMasterId = @linktoCountryMasterId
		,linktoStateMasterId = @linktoStateMasterId
		,linktoCityMasterId = @linktoCityMasterId
		,ZipCode = @ZipCode
		,Phone1 = @Phone1
		,Phone2 = @Phone2
		,Email = @Email
		,Salary = @Salary
		,JoinDate = @JoinDate
		,LeaveDate = @LeaveDate
		,BloodGroup = @BloodGroup
		,linktoDesignationMasterId = @linktoDesignationMasterId
		,linktoDepartmentMasterId = @linktoDepartmentMasterId
		,ReferenceBy = @ReferenceBy
		,Remark = @Remark
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		EmployeeMasterId = @EmployeeMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posErrorLog_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create PROCEDURE [dbo].[posErrorLog_Delete]
	 @ErrorLogId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posErrorLog
	WHERE
		ErrorLogId = @ErrorLogId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posErrorLog_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posErrorLog_DeleteAll]
	 @ids varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posErrorLog
	WHERE
		ErrorLogId IN (SELECT * from dbo.Parse(@ids, ','))

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posErrorLog_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posErrorLog_Insert]
	 @ErrorLogId smallint OUTPUT
	,@ErrorDateTime datetime
	,@ErrorMessage varchar(500)
	,@ErrorStackTrace varchar(4000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	
	IF EXISTS(SELECT ErrorLogId FROM posErrorLog WHERE ErrorStackTrace = @ErrorStackTrace)
	BEGIN		
		UPDATE posErrorLog
		SET 
			 LastErrorDateTime = GETDATE()
			,ErrorCount = ErrorCount + 1
		WHERE
			ErrorLogId = (SELECT ErrorLogId FROM posErrorLog WHERE ErrorStackTrace = @ErrorStackTrace)
			
		RETURN
	END
	
	INSERT INTO posErrorLog
	(
		 ErrorDateTime
		,ErrorMessage
		,ErrorStackTrace
	)
	VALUES
	(
		 @ErrorDateTime
		,@ErrorMessage
		,@ErrorStackTrace
	)

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
	BEGIN
		SET @ErrorLogId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END






GO
/****** Object:  StoredProcedure [dbo].[posErrorLog_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posErrorLog_SelectAll]
AS
BEGIN
	SET NOCOUNT OFF
	
	SELECT  
		*
	FROM
		posErrorLog
	Order By
	  ErrorLogId Desc 

	RETURN
END






GO
/****** Object:  StoredProcedure [dbo].[posErrorLog_Truncate]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posErrorLog_Truncate]
	@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	TRUNCATE TABLE posErrorLog

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END






GO
/****** Object:  StoredProcedure [dbo].[posFeedbackAnswerMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackAnswerMaster_DeleteAll]
	 @FeedbackAnswerMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posFeedbackAnswerMaster
	SET
		 IsDeleted = 1
	WHERE
		FeedbackAnswerMasterId IN (SELECT * from dbo.Parse(@FeedbackAnswerMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackAnswerMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackAnswerMaster_Insert]
	 @FeedbackAnswerMasterId int OUTPUT
	,@linktoFeedbackQuestionMasterId int
	,@Answer varchar(50)
	,@IsDeleted bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posFeedbackAnswerMaster
	(
		 linktoFeedbackQuestionMasterId
		,Answer
		,IsDeleted
	)
	VALUES
	(
		 @linktoFeedbackQuestionMasterId
		,@Answer
		,@IsDeleted
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @FeedbackAnswerMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackAnswerMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackAnswerMaster_SelectAll]
  @linktoFeedbackQuestionMasterId smallint = NULL
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posFeedbackAnswerMaster.*
		,(SELECT FeedbackQuestion FROM posFeedbackQuestionMaster WHERE FeedbackQuestionMasterId = linktoFeedbackQuestionMasterId) AS FeedbackQuestion
	FROM
		 posFeedbackAnswerMaster
	WHERE
		IsDeleted = 0
		AND
		linktoFeedbackQuestionMasterId=ISNULL(@linktoFeedbackQuestionMasterId,linktoFeedbackQuestionMasterId)
	ORDER BY FeedbackAnswerMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackAnswerMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackAnswerMaster_Update]
	 @FeedbackAnswerMasterId int
	,@linktoFeedbackQuestionMasterId int
	,@Answer varchar(50)
	,@IsDeleted bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posFeedbackAnswerMaster
	SET
		 linktoFeedbackQuestionMasterId = @linktoFeedbackQuestionMasterId
		,Answer = @Answer
		,IsDeleted = @IsDeleted
	WHERE
		FeedbackAnswerMasterId = @FeedbackAnswerMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posFeedbackMaster_Insert]
	 @FeedbackMasterId int OUTPUT
	,@Name varchar(50) = NULL
	,@Email varchar(80)
	,@Phone varchar(50) = NULL
	,@Feedback varchar(2000)
	,@FeedbackDateTime datetime
	,@linktoFeedbackTypeMasterId smallint
	,@linktoCustomerMasterId int = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posFeedbackMaster
	(
		 Name
		,Email
		,Phone
		,Feedback
		,FeedbackDateTime
		,linktoFeedbackTypeMasterId
		,linktoCustomerMasterId
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @Name
		,@Email
		,@Phone
		,@Feedback
		,@FeedbackDateTime
		,@linktoFeedbackTypeMasterId
		,@linktoCustomerMasterId
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @FeedbackMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionGroupMaster_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackQuestionGroupMaster_Delete]
	 @FeedbackQuestionGroupMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS (SELECT linktoFeedbackQuestionGroupMasterId from posFeedbackQuestionMaster where linktoFeedbackQuestionGroupMasterId = @FeedbackQuestionGroupMasterId AND IsDeleted = 0)
	BEGIN
		  SET @Status = -2
		   RETURN
	END

	ELSE
	BEGIN

	UPDATE
		posFeedbackQuestionGroupMaster
	SET
		 IsDeleted = 1
	WHERE
		FeedbackQuestionGroupMasterId = @FeedbackQuestionGroupMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
	END
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionGroupMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackQuestionGroupMaster_Insert]
	 @FeedbackQuestionGroupMasterId smallint OUTPUT
	,@linktoBusinessMasterId smallint
	,@GroupName varchar(50)
	,@IsDeleted bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT FeedbackQuestionGroupMasterId FROM posFeedbackQuestionGroupMaster WHERE GroupName = @GroupName AND IsDeleted = 0 AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @FeedbackQuestionGroupMasterId = FeedbackQuestionGroupMasterId FROM posFeedbackQuestionGroupMaster WHERE GroupName = @GroupName AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posFeedbackQuestionGroupMaster
	(
		 linktoBusinessMasterId
		,GroupName
		,IsDeleted
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@GroupName
		,@IsDeleted
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @FeedbackQuestionGroupMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionGroupMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackQuestionGroupMaster_SelectAll]
	@linktoBusinessMasterId smallint = NULL
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posFeedbackQuestionGroupMaster.*,
		(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
		,(SELECT count(FeedbackQuestionMasterId) from posFeedbackQuestionMaster where linktoFeedbackQuestionGroupMasterId IS NULL )As TotalNullGroupFeedbackQuestion
	FROM 
	    posFeedbackQuestionGroupMaster  
	Where 
		linktoBusinessMasterId = ISNULL(@linktoBusinessMasterId, linktoBusinessMasterId)
		AND IsDeleted = 0

	ORDER BY GroupName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionGroupMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackQuestionGroupMaster_Update]
	 @FeedbackQuestionGroupMasterId smallint
	,@linktoBusinessMasterId smallint
	,@GroupName varchar(50)
	,@IsDeleted bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT FeedbackQuestionGroupMasterId FROM posFeedbackQuestionGroupMaster WHERE GroupName = @GroupName AND FeedbackQuestionGroupMasterId != @FeedbackQuestionGroupMasterId AND IsDeleted = 0 AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posFeedbackQuestionGroupMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,GroupName = @GroupName
		,IsDeleted = @IsDeleted
	WHERE
		FeedbackQuestionGroupMasterId = @FeedbackQuestionGroupMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionGroupMasterGroupName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackQuestionGroupMasterGroupName_SelectAll]

	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 FeedbackQuestionGroupMasterId
		,GroupName
	FROM
		 posFeedbackQuestionGroupMaster
	WHERE
		IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY GroupName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackQuestionMaster_DeleteAll]
	 @FeedbackQuestionMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int
	DECLARE @RowCount int
	SET @TotalRecords=(SELECT COUNT(*) FROM dbo.Parse(@FeedbackQuestionMasterIds,','))
	UPDATE
		posFeedbackQuestionMaster
	SET
		 IsDeleted = 1
	WHERE
		FeedbackQuestionMasterId IN (SELECT * from dbo.Parse(@FeedbackQuestionMasterIds, ','))
		AND FeedbackQuestionMasterId NOT IN 
		(
		SELECT linktoFeedbackQuestionMasterId FROM posFeedbackTran WHERE IsDeleted = 0 and IsEnabled = 1 
		AND linktoFeedbackQuestionMasterId IN (SELECT * FROM dbo.Parse(@FeedbackQuestionMasterIds,','))
		)
		SET @RowCount=@@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
			
			if @TotalRecords = @RowCount
				BEGIN
				set @Status=0
				END
			ELSE
				BEGIN
				SET @Status = -2
				END
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--  SELECT COUNT(*) FROM posFeedbackQuestionMaster WHERE linktoFeedbackQuestionGroupMasterId = 3 AND IsEnabled = 1 AND IsDeleted = 0
CREATE PROCEDURE [dbo].[posFeedbackQuestionMaster_Insert]
	 @FeedbackQuestionMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@FeedbackQuestion varchar(250)
	,@linktoFeedbackQuestionGroupMasterId smallint = null
	,@QuestionType smallint
	,@SortOrder int = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@Status smallint OUTPUT 
AS
BEGIN
	SET NOCOUNT OFF
	
	DECLARE @QuestionCount AS INT 
	if @linktoFeedbackQuestionGroupMasterId IS NOT NULL
		BEGIN
		 SELECT @QuestionCount = COUNT(*) FROM posFeedbackQuestionMaster WHERE linktoFeedbackQuestionGroupMasterId = @linktoFeedbackQuestionGroupMasterId AND IsEnabled = 1 AND IsDeleted = 0
		END
	ELSE
		BEGIN 
		SELECT @QuestionCount= COUNT(*) FROM posFeedbackQuestionMaster WHERE linktoFeedbackQuestionGroupMasterId IS NULL AND IsEnabled = 1 AND IsDeleted = 0
		END 

	if( @QuestionCount > 9)
	BEGIN
		SET @Status = -3
		RETURN
	END

	ELSE
	BEGIN 
		INSERT INTO posFeedbackQuestionMaster
	(
		 linktoBusinessMasterId
		,FeedbackQuestion
		,linktoFeedbackQuestionGroupMasterId
		,QuestionType
		,SortOrder
		,IsEnabled
		,IsDeleted
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@FeedbackQuestion
		,@linktoFeedbackQuestionGroupMasterId
		,@QuestionType
		,@SortOrder
		,@IsEnabled
		,@IsDeleted
	)

		IF @@ERROR <> 0
				BEGIN
					SET @Status = -1
				END
				ELSE
				BEGIN
					SET @FeedbackQuestionMasterId = @@IDENTITY
					SET @Status = 0
				END
		RETURN
	END
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackQuestionMaster_Select]
	 @FeedbackQuestionMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posFeedbackQuestionMaster.*
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
		,(SELECT GroupName from posFeedbackQuestionGroupMaster WHERE FeedbackQuestionGroupMasterId = linktoFeedbackQuestionGroupMasterId) AS QuestionGroupName
	FROM
		 posFeedbackQuestionMaster
	WHERE
		FeedbackQuestionMasterId = @FeedbackQuestionMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--posFeedbackQuestionMaster_SelectAll 1,'',''
CREATE PROCEDURE [dbo].[posFeedbackQuestionMaster_SelectAll]
	 @IsEnabled bit = NULL,
	 @QuestionType smallint = NULL,
	 @FeedbackQuestion varchar(50) = NULL,
	 @linktoBusinessMasterId smallint 

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posFeedbackQuestionMaster.* 
		,(SELECT GroupName from posFeedbackQuestionGroupMaster WHERE FeedbackQuestionGroupMasterId = linktoFeedbackQuestionGroupMasterId) AS QuestionGroupName
	FROM
		 posFeedbackQuestionMaster
	WHERE
		IsEnabled = ISNULL(@IsEnabled, IsEnabled)
		AND FeedbackQuestion LIKE ISNULL(@FeedbackQuestion,'') + '%'
		AND QuestionType = ISNULL(@QuestionType,QuestionType)
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY CASE WHEN SortOrder IS NULL THEN 1 ELSE 0 END, SortOrder

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackQuestionMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackQuestionMaster_Update]
	 @FeedbackQuestionMasterId int
	,@linktoBusinessMasterId smallint
	,@FeedbackQuestion varchar(250)
	,@linktoFeedbackQuestionGroupMasterId smallint = null
	,@QuestionType smallint
	,@SortOrder int = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DECLARE @QuestionCount AS INT 
	if @linktoFeedbackQuestionGroupMasterId IS NOT NULL
		BEGIN
		 SELECT @QuestionCount = COUNT(*) from posFeedbackQuestionMaster where linktoFeedbackQuestionGroupMasterId = @linktoFeedbackQuestionGroupMasterId AND IsEnabled = 1 AND IsDeleted = 0 AND FeedbackQuestionMasterId <> @FeedbackQuestionMasterId
		END
	ELSE
		BEGIN 
		SELECT @QuestionCount= COUNT(*) from posFeedbackQuestionMaster where linktoFeedbackQuestionGroupMasterId IS NULL AND IsEnabled = 1 AND IsDeleted = 0 AND FeedbackQuestionMasterId <> @FeedbackQuestionMasterId
		END   
		if( @QuestionCount >= 10)
	BEGIN
		SET @Status = -3
		RETURN
	END

	ELSE
	BEGIN
		UPDATE posFeedbackQuestionMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,FeedbackQuestion = @FeedbackQuestion
		,linktoFeedbackQuestionGroupMasterId = @linktoFeedbackQuestionGroupMasterId  
		,QuestionType = @QuestionType
		,SortOrder = @SortOrder
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
	WHERE
		FeedbackQuestionMasterId = @FeedbackQuestionMasterId

		IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
 END
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackTran_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackTran_DeleteAll]
	 @FeedbackTranIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
		Declare @TotalRecords int
		 DECLARE @RowCount int
		set @TotalRecords=(SELECT COUNT(*) FROM dbo.Parse(@FeedbackTranIds, ','))
	DELETE
	FROM
		 posFeedbackTran
	WHERE
		FeedbackTranId IN (SELECT * from dbo.Parse(@FeedbackTranIds, ','))
		AND FeedbackTranId NOT IN 
		(
			SELECT linktoFeedbackMasterId FROM posFeedbackTran WHERE    
			 linktoFeedbackMasterId IN (SELECT * FROM dbo.Parse(@FeedbackTranIds, ','))
		)
		SET @RowCount=@@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1	
	END
	ELSE
	BEGIN
		
			if @TotalRecords = @RowCount
				BEGIN
					SET @Status = 0
				END
				ELSE
				BEGIN
					SET @Status = -2
				END
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posFeedbackTran_Insert]
	 @FeedbackTranId int OUTPUT
	,@linktoFeedbackMasterId int
	,@linktoFeedbackQuestionMasterId int
	,@linktoFeedbackAnswerMasterId int = NULL
	,@Answer varchar(200) = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posFeedbackTran
	(
		 linktoFeedbackMasterId
		,linktoFeedbackQuestionMasterId
		,linktoFeedbackAnswerMasterId
		,Answer
	)
	VALUES
	(
		 @linktoFeedbackMasterId
		,@linktoFeedbackQuestionMasterId
		,@linktoFeedbackAnswerMasterId
		,@Answer
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @FeedbackTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFeedbackTran_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posFeedbackTran.*
		,(SELECT Name FROM posFeedbackMaster WHERE FeedbackMasterId = linktoFeedbackMasterId) AS Feedback		
		,(SELECT FeedbackQuestion FROM posFeedbackQuestionMaster WHERE FeedbackQuestionMasterId = linktoFeedbackQuestionMasterId) AS FeedbackQuestion		
		,(SELECT Answer FROM posFeedbackAnswerMaster WHERE FeedbackAnswerMasterId = linktoFeedbackAnswerMasterId) AS FeedbackAnswer
	FROM
		 posFeedbackTran
	
	ORDER BY FeedbackTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFeedbackTranQuestionWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--  posFeedbackTranQuestionWise_SelectAll 1,'2015/11/1 00:00:00','2015/12/26 00:00:0','','','',0
CREATE PROCEDURE [dbo].[posFeedbackTranQuestionWise_SelectAll]
	 @linktoFeedbackQuestionMasterId int = NULL
	,@FeebackDate date = NULL
	,@FeebackDateTo date = NULL
	,@Name varchar(50) 
	,@phone Varchar(20)
	,@Email Varchar(50)
	,@FeedbackMasterID int = NULL
AS
BEGIN 
	SET NOCOUNT ON; 
	SELECT DISTINCT 
		*,
		STUFF((
				SELECT DISTINCT ', ' + CONVERT(VARCHAR,Answer)  
				FROM posFeedbackAnswerMaster
				WHERE FeedbackAnswerMasterId in (SELECT parsevalue FROM dbo.parse(Answerids,',')) 
		FOR XML PATH(''), TYPE   
		).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As AnswerMultiOrSingle
	FROM
	(
		SELECT 	
			 FT.Answer,
			 STUFF((SELECT DISTINCT ',' + CONVERT(VARCHAR,FeedbackTranId)  
					FROM posFeedbackTran
					WHERE linktoFeedbackQuestionMasterId = ft.linktoFeedbackQuestionMasterId 
					AND linktoFeedbackMasterId = ft.linktoFeedbackMasterId
			 FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')   
			,1,1,'') AS FeedbackTranIds,
			FT.linktoFeedbackMasterId,FT.linktoFeedbackQuestionMasterId,FQM.FeedbackQuestion,FM.Name AS Name,FM.Email AS Email,
			FM.Phone AS Phone,FM.Feedback AS FeedbackAnswer,FQM.QuestionType AS QuestionType,
			STUFF((SELECT DISTINCT ',' + CONVERT(VARCHAR,linktoFeedbackAnswerMasterId)  
				FROM posFeedbackTran
				WHERE linktoFeedbackQuestionMasterId = ft.linktoFeedbackQuestionMasterId
			FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')   
			,1,1,'') AS Answerids
		FROM 
			posFeedbackTran FT
		JOIN 
			posFeedbackQuestionMaster FQM ON  FQM.FeedbackQuestionMasterId = FT.linktoFeedbackQuestionMasterId 
		JOIN 
			posFeedbackMaster FM ON  FM.FeedbackMasterId = FT.linktoFeedbackMasterId
		WHERE 
			FT.linktoFeedbackQuestionMasterId = ISNULL(@linktoFeedbackQuestionMasterId,FT.linktoFeedbackQuestionMasterId)  
			AND CONVERT (VARCHAR(8),FM.FeedbackDateTime,112) BETWEEN CONVERT(VARCHAR(8),@FeebackDate,112) AND CONVERT(VARCHAR(8),@FeebackDateTo,112)
			AND FM.Name LIKE @Name + '%'
			AND FM.Phone LIKE @phone + '%'
			AND FM.Email LIKE @Email + '%'
			AND FM.FeedbackMasterId = CASE WHEN @FeedbackMasterID = 0 THEN FeedbackMasterId ELSE @FeedbackMasterID END
	) TempTable
	ORDER BY linktoFeedbackMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posFinancialYearMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFinancialYearMaster_Insert]
	 @FinancialYearMasterId smallint OUTPUT
	,@linktoBusinessMasterId smallint
	,@FromDate date
	,@ToDate date
	,@IsDefault bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT FinancialYearMasterId FROM posFinancialYearMaster WHERE FromDate = @FromDate AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @FinancialYearMasterId = FinancialYearMasterId FROM posFinancialYearMaster WHERE FromDate = @FromDate AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END

	IF @IsDefault = 1
	BEGIN
		UPDATE posFinancialYearMaster SET IsDefault = 0 WHERE linktoBusinessMasterId = @linktoBusinessMasterId
	END 

	INSERT INTO posFinancialYearMaster
	(
		 linktoBusinessMasterId
		,FromDate
		,ToDate
		,IsDefault
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@FromDate
		,@ToDate
		,@IsDefault
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @FinancialYearMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFinancialYearMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFinancialYearMaster_Select]
	 @FinancialYearMasterId smallint = NULL,
	 @IsDefault bit = NULL,
	 @linktobusinessMasterId smallint = NULL
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posFinancialYearMaster.*
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posFinancialYearMaster
	WHERE
		FinancialYearMasterId = ISNULL(@FinancialYearMasterId,FinancialYearMasterId)
		AND IsDefault = ISNULL(@IsDefault,IsDefault)
		AND linktoBusinessMasterId = ISNULL(@linktobusinessMasterId,linktoBusinessMasterId)

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFinancialYearMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posFinancialYearMaster_SelectAll 1
CREATE PROCEDURE [dbo].[posFinancialYearMaster_SelectAll]
	@linktobusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posFinancialYearMaster.*,DATEPART(YEAR,FromDate) AS FinancialFromYear,DATEPART(YEAR,ToDate) AS FinancialToYear,DATENAME(MONTH,DATEADD(MONTH,DATEPART(m,FromDate),0 ) - 1 ) AS FinancialFromMonth,DATENAME(MONTH,DATEADD(MONTH,DATEPART(m,ToDate),0 ) - 1 ) AS FinancialToMonth		
	FROM
		 posFinancialYearMaster
	WHERE
		linktoBusinessMasterId = @linktobusinessMasterId
	ORDER BY FinancialYearMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posFinancialYearMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posFinancialYearMaster_Update]
	 @FinancialYearMasterId smallint
	,@linktoBusinessMasterId smallint
	,@FromDate date
	,@ToDate date	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT FinancialYearMasterId FROM posFinancialYearMaster WHERE FromDate = @FromDate AND linktoBusinessMasterId=@linktoBusinessMasterId AND FinancialYearMasterId != @FinancialYearMasterId)
	BEGIN
		SELECT @FinancialYearMasterId = FinancialYearMasterId FROM posFinancialYearMaster WHERE FromDate = @FromDate AND linktoBusinessMasterId=@linktoBusinessMasterId AND FinancialYearMasterId != @FinancialYearMasterId
		SET @Status = -2
		RETURN
	END

	UPDATE posFinancialYearMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,FromDate = @FromDate
		,ToDate = @ToDate		
	WHERE
		FinancialYearMasterId = @FinancialYearMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueItemMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueItemMaster_DeleteAll]
	 @IssueItemMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	DELETE
	FROM
		 posIssueItemTran
	WHERE
		linktoIssueItemMasterId IN (SELECT * FROM dbo.Parse(@IssueItemMasterIds, ','))

	DELETE
	FROM
		 posIssueItemMaster
	WHERE
		IssueItemMasterId IN (SELECT * from dbo.Parse(@IssueItemMasterIds, ','))		
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueItemMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueItemMaster_Insert]
	 @IssueItemMasterId int OUTPUT
	,@IssueNumber varchar(20)
	,@IssueDate date
	,@linktoDepartmentMasterIdFrom smallint
	,@linktoDepartmentMasterIdTo smallint
	,@Remark varchar(250) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posIssueItemMaster
	(
		 IssueNumber
		,IssueDate
		,linktoDepartmentMasterIdFrom
		,linktoDepartmentMasterIdTo
		,Remark
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @IssueNumber
		,@IssueDate
		,@linktoDepartmentMasterIdFrom
		,@linktoDepartmentMasterIdTo
		,@Remark
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @IssueItemMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueItemMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueItemMaster_Select]
	 @IssueItemMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posIssueItemMaster.*
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterIdFrom) AS DepartmentFrom
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterIdTo) AS DepartmentTo
	FROM
		 posIssueItemMaster
	WHERE
		IssueItemMasterId = @IssueItemMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueItemMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueItemMaster_SelectAll]
	 @IssueNumber varchar(20)
	,@IssueDate date = NULL
	,@linktoDepartmentMasterIdFrom smallint = NULL

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posIssueItemMaster.*
		,(DMFrom.DepartmentName) AS DepartmentFrom		
		,(DMTo.DepartmentName )  AS DepartmentTo
		
	FROM
		  posIssueItemMaster ,  posDepartmentMaster DMFrom , posDepartmentMaster DMTo
	WHERE
		IssueNumber LIKE @IssueNumber + '%'
		AND CONVERT(varchar(8), IssueDate, 112) = CONVERT(varchar(8), @IssueDate, 112)
		AND linktoDepartmentMasterIdFrom = ISNULL(@linktoDepartmentMasterIdFrom, linktoDepartmentMasterIdFrom)
		AND DMFrom.IsDeleted = 0 AND DMFrom.IsEnabled = 1
		AND DMTo.IsDeleted = 0 AND DMTo.IsEnabled = 1
		AND  DMFrom.DepartmentMasterId = linktoDepartmentMasterIdFrom
		AND  DMTo.DepartmentMasterId = linktoDepartmentMasterIdTo



	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueItemMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueItemMaster_Update]
	 @IssueItemMasterId int
	,@IssueNumber varchar(20)
	,@IssueDate date
	,@linktoDepartmentMasterIdFrom smallint
	,@linktoDepartmentMasterIdTo smallint
	,@Remark varchar(250) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posIssueItemMaster
	SET
		 IssueNumber = @IssueNumber
		,IssueDate = @IssueDate
		,linktoDepartmentMasterIdFrom = @linktoDepartmentMasterIdFrom
		,linktoDepartmentMasterIdTo = @linktoDepartmentMasterIdTo
		,Remark = @Remark
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		IssueItemMasterId = @IssueItemMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueItemMasterVerifiedByIssueNumber_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posIssueItemMasterVerifiedByIssueNumber_Select]
	@IssueItemMasterId int,
	@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT ON;
	IF EXISTS(SELECT IssueNumber FROM posIssueItemMaster WHERE IssueNumber = @IssueItemMasterId )
	BEGIN
			SET @Status=-2;
	END
	ELSE
	BEGIN
			SET @Status=0;
	END
	RETURN
END

GO
/****** Object:  StoredProcedure [dbo].[posIssueItemTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueItemTran_Delete]
	 @linktoIssueItemMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posIssueItemTran
	WHERE
		linktoIssueItemMasterId = @linktoIssueItemMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueItemTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueItemTran_Insert]
	 @IssueItemTranId int OUTPUT
	,@linktoIssueItemMasterId int
	,@linktoItemMasterId int
	,@linktoUnitMasterId smallint
	,@Quantity numeric(10, 2)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posIssueItemTran
	(
		 linktoIssueItemMasterId
		,linktoItemMasterId
		,linktoUnitMasterId
		,Quantity
	)
	VALUES
	(
		 @linktoIssueItemMasterId
		,@linktoItemMasterId
		,@linktoUnitMasterId
		,@Quantity
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @IssueItemTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueItemTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueItemTran_SelectAll]
@linktoIssueItemMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posIssueItemTran.*
		,(SELECT IssueItemMasterId FROM posIssueItemMaster WHERE IssueItemMasterId = linktoIssueItemMasterId) AS IssueItem		
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterId) AS Unit
	FROM
		 posIssueItemTran
	WHERE
	linktoIssueItemMasterId=@linktoIssueItemMasterId
	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posIssueMasterByIssueDate_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posIssueMasterByIssueDate_SelectAll]
	@linktoBusinessMasterId smallint,
	@FromDate datetime,
	@ToDate datetime,
	@ItemMasterIds varchar(1000)
AS
BEGIN
	SELECT 
		IIM.IssueDate,DMFrom.DepartmentName As DepartmentFrom,DMTo.DepartmentName As DepartmentTo,IM.ItemName,IIT.Quantity,UM.UnitName 
	FROM 
		posIssueItemMaster IIM
	JOIN
		posIssueItemTran IIT ON IIT.linktoIssueItemMasterId = IIM.IssueItemMasterId
	JOIN 
		posDepartmentMaster DMFrom ON DMFrom.DepartmentMasterId = IIM.linktoDepartmentMasterIdFrom
	JOIN 
		posDepartmentMaster DMTo ON DMTo.DepartmentMasterId = IIM.linktoDepartmentMasterIdTo
	JOIN
		posItemMaster IM ON IM.ItemMasterId = IIT.linktoItemMasterId
	JOIN
		posUnitMaster UM ON UM.UnitMasterId = IIT.linktoUnitMasterId
	WHERE 
		IM.linktoBusinessMasterId = @linktoBusinessMasterId 
		AND 
		linktoItemMasterId IN (SELECT parseValue FROM dbo.Parse(@ItemMasterIds,','))
		AND
		Convert(varchar(8),IssueDate,112) BETWEEN  Convert(varchar(8),@FromDate,112) AND Convert(varchar(8),@ToDate,112)
END



GO
/****** Object:  StoredProcedure [dbo].[posItemCategoryTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemCategoryTran_Insert]
	  @ItemCategoryTranIds varchar(1000) = NULL
	 ,@ItemMasterId int
	 ,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		 posItemCategoryTran
	WHERE
		linktoItemMasterId = @ItemMasterId

	IF(@ItemCategoryTranIds IS NOT NULL)

	BEGIN
		INSERT  INTO posItemCategoryTran
		(
			linktoItemMasterId,
			linktoCategoryMasterId
		)
		SELECT 
			@ItemMasterId,parsevalue 
		FROM 
			dbo.Parse(@ItemCategoryTranIds,',')
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemCategoryTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemCategoryTran_SelectAll 0
CREATE PROCEDURE [dbo].[posItemCategoryTran_SelectAll]
	@linktoItemMasterId int 
	,@IsRawMaterial bit = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		CM.*,ICT.*
	FROM
		posCategoryMaster CM LEFT JOIN posItemCategoryTran ICT
	ON
		CM.CategoryMasterId = ICT.linktoCategoryMasterId
		AND linktoItemMasterId = @linktoItemMasterId
		
		WHERE ((@IsRawMaterial IS NULL AND (IsRawMaterial IS NULL OR IsRawMaterial IS NOT NULL)) OR (@IsRawMaterial IS NOT NULL AND IsRawMaterial = @IsRawMaterial))
		AND  CM.IsDeleted=0
		AND CM.IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId
 

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemComboTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemComboTran_Insert]
	@linktoItemMasterId int
	,@linktoItemMasterIdsCombo varchar(1000) = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
			posItemComboTran
	WHERE
		linktoItemMasterId = @linktoItemMasterId

	IF(@linktoItemMasterIdsCombo IS NOT NULL)

	BEGIN
		INSERT INTO posItemComboTran
		(
			 linktoItemMasterId
			,linktoItemMasterIdCombo
		)
		SELECT 
			@linktoItemMasterId,parsevalue 
		FROM 
			dbo.Parse(@linktoItemMasterIdsCombo,',')
	END

	

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN		
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemComboTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemComboTran_Select]
	 @ItemComboTranId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posItemComboTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterIdCombo) AS ItemCombo
	FROM
		 posItemComboTran
	WHERE
		ItemComboTranId = @ItemComboTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemComboTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemComboTran_SelectAll]
@linktoItemMasterId int	
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posItemComboTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterIdCombo) AS ItemCombo
	FROM
		 posItemComboTran
	WHERE 
		linktoItemMasterId = @linktoItemMasterId
	ORDER BY ItemComboTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posItemMaster_DeleteAll]
     @ItemMasterIds varchar(1000)
    ,@UpdateDateTime datetime
    ,@linktoUserMasterIdUpdatedBy smallint
    ,@ItemType smallint
    ,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	DECLARE @TotalRecords int
	DECLARE @RowCount int
	SET @TotalRecords=(SELECT COUNT(*) FROM dbo.Parse(@ItemMasterIds, ','))

	IF @ItemType = 0
	BEGIN
	    UPDATE
		    posItemMaster
	    SET
			IsDeleted = 1,
			UpdateDateTime = @UpdateDateTime
		    ,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	    WHERE
		    ItemMasterId IN (SELECT * from dbo.Parse(@ItemMasterIds, ','))
		    AND ItemMasterId NOT IN 
		    (
			    SELECT linktoItemMasterId FROM posOrderItemTran WHERE IsDeleted = 0 AND IsEnabled = 1 
			    AND linktoItemMasterId IN (SELECT * FROM dbo.Parse(@ItemMasterIds, ','))
		    )

	   SET @RowCount=@@ROWCOUNT
	END
	ELSE IF @ItemType = 1
	BEGIN
		UPDATE
			posItemMaster
		SET
			 IsDeleted = 1,
			 UpdateDateTime = @UpdateDateTime
			,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		WHERE
			ItemMasterId IN (SELECT * from dbo.Parse(@ItemMasterIds, ','))
			AND ItemMasterId NOT IN 
			(
				SELECT linktoItemMasterIdUse FROM posItemUsageTran WHERE IsDeleted = 0 AND IsEnabled = 1 
				AND linktoItemMasterIdUse IN (SELECT * FROM dbo.Parse(@ItemMasterIds, ','))
			)

	   SET @RowCount=@@ROWCOUNT
	END
	ELSE IF @ItemType = 2
	BEGIN
		UPDATE
			posItemMaster
		SET
			 IsDeleted = 1,
			 UpdateDateTime = @UpdateDateTime
			,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		WHERE
			ItemMasterId IN (SELECT * from dbo.Parse(@ItemMasterIds, ','))
			AND ItemMasterId NOT IN 
			(
				SELECT linktoItemMasterId FROM posOrderItemModifierTran WHERE IsDeleted = 0 AND IsEnabled = 1 
				AND linktoItemMasterId IN (SELECT * FROM dbo.Parse(@ItemMasterIds, ','))
			)

	   SET @RowCount=@@ROWCOUNT
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
	   IF @TotalRecords = @RowCount
	   BEGIN
		  SET @Status = 0
	   END
	   ELSE
	   BEGIN
		  SET @Status = -2
	   END
	END
	RETURN
END









GO
/****** Object:  StoredProcedure [dbo].[posItemMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMaster_Insert]
	 @ItemMasterId int OUTPUT
	,@ShortName varchar(20) = NULL
	,@ItemName varchar(50)
	,@ItemCode varchar(20)
	,@BarCode varchar(50) = NULL
	,@ShortDescription varchar(500) = NULL
	,@linktoUnitMasterId smallint
	,@IsFavourite bit
	,@ImageName varchar(100) = NULL	
	,@ItemPoint smallint
	,@PriceByPoint smallint
	,@SearchWords varchar(200) = NULL
	,@linktoBusinessMasterId smallint
	,@SortOrder int = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@ItemType smallint
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoCategoryMasterId smallint
	,@IsDineInOnly bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF


	IF EXISTS(SELECT ItemMasterId FROM posItemMaster WHERE (ItemName = @ItemName OR (@ItemCode <> '' AND ItemCode = @ItemCode)) AND ItemType = @ItemType AND IsDeleted = 0  AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @ItemMasterId = ItemMasterId FROM posItemMaster WHERE (ItemName = @ItemName OR (@ItemCode <> '' AND ItemCode = @ItemCode)) AND ItemType = @ItemType AND IsDeleted = 0  AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posItemMaster
	(
		 ShortName
		,ItemName
		,ItemCode
		,BarCode
		,ShortDescription
		,linktoUnitMasterId
		,IsFavourite
		,ImageName		
		,linktoCategoryMasterId
		,ItemPoint
		,PriceByPoint
		,SearchWords
		,linktoBusinessMasterId
		,SortOrder
		,IsEnabled
		,IsDeleted
		,ItemType
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,IsDineInOnly
		
	)
	VALUES
	(
		 @ShortName
		,@ItemName
		,@ItemCode
		,@BarCode
		,@ShortDescription
		,@linktoUnitMasterId
		,@IsFavourite
		,@ImageName		
		,@linktoCategoryMasterId
		,@ItemPoint
		,@PriceByPoint
		,@SearchWords
		,@linktoBusinessMasterId
		,@SortOrder
		,@IsEnabled
		,@IsDeleted
		,@ItemType
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@IsDineInOnly
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @ItemMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMaster_Select]

	@ItemMasterId int

AS
BEGIN

	SET NOCOUNT ON;

	SELECT
		posItemMaster.*
	
		,(SELECT UnitName from posUnitMaster where UnitMasterId=linktoUnitMasterId) As Unit		
		,(SELECT MRP from posItemRateTran where linktoItemMasterId=@ItemMasterId) As MRP
	FROM
		posItemMaster
	WHERE
		ItemMasterId = @ItemMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemMaster_SelectAll
CREATE PROCEDURE [dbo].[posItemMaster_SelectAll]
	 @ItemName varchar(50)
	,@IsFavourite bit = NULL
	,@CategoryMasterId smallint = NULL
	,@ItemType smallint = NULL
	,@IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		im.*
		--,(SELECT ItemType FROM posItemTypeMaster WHERE ItemTypeMasterId = linktoItemTypeMasterId) AS ItemType		
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterId) AS Unit				
		,(SELECT CategoryName from posCategoryMaster WHERE CategoryMasterId = linktoCategoryMasterId) AS CategoryName 	
		,(SELECT MRP from posItemRateTran WHERE linktoItemMasterId = ItemMasterId) AS MRP 	
	FROM
		 posItemMaster im
	WHERE
		(ItemName LIKE @ItemName + '%' OR ISNULL(ShortName,'') LIKE @ItemName + '%' OR ItemCode LIKE @ItemName + '%')
		AND ((@IsFavourite IS NULL AND (IsFavourite IS NULL OR IsFavourite IS NOT NULL)) OR (@IsFavourite IS NOT NULL AND IsFavourite = @IsFavourite))
		AND linktoCategoryMasterId = ISNULL(@CategoryMasterId, linktoCategoryMasterId)
		AND ((@IsEnabled IS NULL AND (im.IsEnabled IS NULL OR im.IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND im.IsEnabled = @IsEnabled))
		AND ((@ItemType IS NULL AND (ItemType IS NULL OR ItemType IS NOT NULL)) OR (@ItemType IS NOT NULL AND ItemType = @ItemType))
		AND im.IsDeleted = 0 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
	--ORDER BY CASE WHEN SortOrder IS NULL THEN 1 ELSE 0 END, SortOrder
	ORDER BY ItemName

	RETURN
END



	


GO
/****** Object:  StoredProcedure [dbo].[posItemMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[posItemMaster_Update]
	 @ItemMasterId int
	,@ShortName varchar(20) = NULL
	,@ItemName varchar(50)
	,@ItemCode varchar(20)
	,@BarCode varchar(50) = NULL
	,@ShortDescription varchar(500) = NULL
	,@linktoUnitMasterId smallint
	,@IsFavourite bit
	,@ImageName varchar(100) = NULL
	,@ItemPoint smallint
	,@PriceByPoint smallint
	,@SearchWords varchar(200) = NULL
	,@linktoBusinessMasterId smallint
	,@SortOrder int = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoCategoryMasterId smallint
	,@ItemType smallint
	,@IsDineInOnly bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT ItemMasterId FROM posItemMaster WHERE (ItemName = @ItemName OR (@ItemCode <> '' AND ItemCode = @ItemCode)) AND ItemMasterId != @ItemMasterId 
				AND ItemType = @ItemType AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posItemMaster
	SET
		 ShortName = @ShortName
		,ItemName = @ItemName
		,ItemCode = @ItemCode
		,BarCode = @BarCode
		,ShortDescription = @ShortDescription
		,linktoUnitMasterId = @linktoUnitMasterId
		,IsFavourite = @IsFavourite
		,ImageName = @ImageName
		,linktoCategoryMasterId = @linktoCategoryMasterId
		,ItemPoint = @ItemPoint
		,PriceByPoint = @PriceByPoint
		,SearchWords = @SearchWords
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,SortOrder = @SortOrder
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,IsDineInOnly = @IsDineInOnly
		
	WHERE
		ItemMasterId = @ItemMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterBulkItems_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posItemMasterBulkItems_Update]
	 @ItemMasterId int
	,@ItemName varchar(50)
	,@ItemCode varchar(20)
	,@BarCode varchar(50)
	,@linktoUnitMasterId smallint
	,@ItemType smallint
	,@IsFavourite bit
	,@linktoCategoryMasterId smallint
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	IF EXISTS(SELECT ItemMasterId FROM posItemMaster WHERE (ItemName = @ItemName OR ItemCode = @ItemCode) AND ItemMasterId != @ItemMasterId AND ItemType = @ItemType AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END

	UPDATE posItemMaster
	SET
	    ItemName = @ItemName
		,ItemCode = @ItemCode
		,BarCode = @BarCode
		,linktoUnitMasterId = @linktoUnitMasterId
		,IsFavourite = @IsFavourite		
		,linktoCategoryMasterId = @linktoCategoryMasterId
		
	WHERE
		ItemMasterId = @ItemMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterByCategory_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMasterByCategory_SelectAll]
	 @linktoCategoryMasterId smallint = 0
	,@ItemType smallint
	,@CounterMasterId smallint
	,@linktoBusinessMasterId smallint
	,@CustomerMasterId int = NULL
AS
BEGIN
	SELECT DISTINCT 
		 ItemMasterId,TempTable.ShortName,ItemName,ItemCode,TempTable.IsFavourite,TempTable.ImageName
		,ItemType,IsDineInOnly,Discount,IsPercentage,linktoCategoryMasterId,linktoOptionValueTranId
		,MRP,Rate1,Rate2,Rate3,Rate4,Rate5,IsRateTaxInclusive,Tax1,Tax2,Tax3,Tax4,Tax5
	FROM
	(
		SELECT 
			IM.ItemMasterId,IM.ShortName,IM.ItemName,IM.ItemCode,IM.BarCode,IM.ShortDescription,IM.linktoUnitMasterId
			,ICT.linktoCategoryMasterId,IM.IsFavourite,IM.ImageName,IM.ItemPoint,IM.PriceByPoint,IM.SearchWords
			,IM.linktoBusinessMasterId,IM.SortOrder,IM.IsEnabled,IM.IsDeleted,IM.ItemType,IM.IsDineInOnly
			,CM.linktoCounterMasterId,MRP,Rate1,Rate2,Rate3,Rate4,Rate5,IsRateTaxInclusive,Tax1,Tax2,Tax3,Tax4,Tax5
		FROM 
			posItemRateTran IRT, posItemMaster IM 
		JOIN
			posItemCategoryTran ICT ON  ICT.linktoItemMasterId = IM.ItemMasterId 
		JOIN
			posCounterItemTran CM ON CM.linktoItemMasterId = IM.ItemMasterId
		WHERE
			ICT.linktoCategoryMasterId = CASE WHEN @linktoCategoryMasterId = 0 THEN ICT.linktoCategoryMasterId ELSE @linktoCategoryMasterId END
			AND CM.linktoCounterMasterId = @CounterMasterId		
			AND ItemType != @ItemType
			AND IM.IsEnabled = 1
			AND IM.IsDeleted = 0 
			AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
			AND IM.ItemMasterId = IRT.linktoItemMasterId

		UNION ALL

		SELECT  
			 IM.ItemMasterId,IM.ShortName,IM.ItemName,IM.ItemCode,IM.BarCode,IM.ShortDescription,IM.linktoUnitMasterId
			,IM.linktoCategoryMasterId,IM.IsFavourite,IM.ImageName,IM.ItemPoint,IM.PriceByPoint,IM.SearchWords
			,IM.linktoBusinessMasterId,IM.SortOrder,IM.IsEnabled,IM.IsDeleted,IM.ItemType,IM.IsDineInOnly
			,CM.linktoCounterMasterId,MRP,Rate1,Rate2,Rate3,Rate4,Rate5,IsRateTaxInclusive,Tax1,Tax2,Tax3,Tax4,Tax5
		FROM 
			posItemRateTran IRT, posItemMaster IM 	
		JOIN
			posCounterItemTran CM ON CM.linktoItemMasterId = IM.ItemMasterId
		WHERE
			IM.linktoCategoryMasterId = CASE WHEN @linktoCategoryMasterId = 0 THEN IM.linktoCategoryMasterId ELSE @linktoCategoryMasterId END
			AND CM.linktoCounterMasterId = @CounterMasterId		
			AND ItemType != @ItemType
			AND IM.IsEnabled = 1
			AND IM.IsDeleted = 0 
			AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
			AND IM.ItemMasterId = IRT.linktoItemMasterId

	) TempTable 
	LEFT JOIN
	(
		SELECT Discount, linktoItemMasterId, IsPercentage
		FROM posDiscountTran DT
		    JOIN posDiscountMaster DM ON DM.DiscountMasterId = DT.linktoDiscountMasterId
		    JOIN posCustomerMaster CM ON cm.linktoDiscountMasterId = DM.DiscountMasterId 
			    AND CM.linktoDiscountMasterId = DT.linktoDiscountMasterId 
			    AND CM.CustomerMasterId = @CustomerMasterId
	) TP ON TP.linktoItemMasterId = ItemMasterId
	LEFT JOIN
		posItemOptionTran IOT ON IOT.linktoItemMasterId = ItemMasterId
END




GO
/****** Object:  StoredProcedure [dbo].[posItemMasterByCategoryMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posItemMasterByCategoryMasterId_SelectAll]

	 @linktoCategoryMasterId smallint = NULL,
	 @ItemTypeMasterIds varchar(100) = NULL,
	 @linktoCounterMasterId smallint,
	 @linktoOrderTypeMasterId smallint,
	 @linktoBusinessMasterId smallint,
	 @IsFavourite bit = NULL,
	 @ItemMasterIds varchar(100) = NULL
	
	-- @StartRowIndex int
	--,@PageSize int
	--,@TotalRowCount int OUTPUT
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @RateIndex smallint

    SELECT 
			@RateIndex = (CASE WHEN @linktoOrderTypeMasterId = 1 THEN DineInRateIndex ELSE
					CASE WHEN @linktoOrderTypeMasterId = 2 THEN TakeAwayRateIndex ELSE
					HomeDeliveryRateIndex END END) from posCounterItemRateTran where linktoCounterMasterId = @linktoCounterMasterId

	SELECT Distinct
			 IM.*	
			--,(SELECT ItemType from posItemTypeMaster where ItemTypeMasterId=linktoItemTypeMasterId) As ItemType
			,STUFF((SELECT distinct ',' + CAST(IOT.linktoOptionValueTranId As varchar(50))
					FROM posItemOptionTran IOT Join posOptionValueTran OVT on OVT.OptionValueTranId = IOT.linktoOptionValueTranId
							and IOT.linktoItemMasterId = IM.ItemMasterId 
							WHERE IOT.linktoItemMasterId = IM.ItemMasterId and OVT.IsDeleted = 0 and OVT.linktoBusinessMasterId = @linktoBusinessMasterId
						FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'),1,1,'') OptionValueTranIds
		    ,STUFF((SELECT distinct ',' + CAST(IMT.linktoItemMasterModifierId As varchar(50))
					FROM posItemModifierTran IMT
							WHERE IMT.linktoItemMasterId = IM.ItemMasterId
						FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'),1,1,'') ItemModifierMasterIds
			,(SELECT UnitName from posUnitMaster where UnitMasterId=IM.linktoUnitMasterId) As Unit			
			,(SELECT MRP from posItemRateTran where linktoItemMasterId=IM.ItemMasterId) As MRP
			,(SELECT Top 1 CM.CategoryName from abPOS.dbo.posCategoryMaster CM JOIN abPOS.dbo.posItemCategoryTran ICT on CM.CategoryMasterId = ICT.linktoCategoryMasterId and IM.ItemMasterId = ICT.linktoItemMasterId) As CategoryName
			--,CASE WHEN @RateIndex = 1 THEN IRT.Rate1 ELSE
			--	 CASE WHEN @RateIndex = 2 THEN IRT.Rate2 ELSE
			--		CASE WHEN  @RateIndex = 3 THEN IRT.Rate3 ELSE
			--			CASE WHEN @RateIndex = 4 THEN IRT.Rate4 ELSE
			--			   CASE WHEN @RateIndex = 5 THEN IRT.Rate5 ELSE
			--				      IRT.Rate1 END END END END END as Price
			,@RateIndex as RateIndex
			,(SELECT Top 1 CM.CategoryName from posCategoryMaster CM JOIN posItemCategoryTran ICT on CM.CategoryMasterId = ICT.linktoCategoryMasterId and IM.ItemMasterId = ICT.linktoItemMasterId) As Category
			,Convert(varchar,IRT.Tax1) + ','+ Convert(varchar,IRT.Tax2)  + ','+Convert(varchar,IRT.Tax3)  + ','+ Convert(varchar,IRT.Tax4)  + ','+ Convert(varchar,IRT.Tax5) as Tax
			,CASE WHEN @RateIndex = 1
			       THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate1 - ((IRT.Rate1 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate1 END ELSE 
				CASE WHEN @RateIndex = 2 
				    THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate2 - ((IRT.Rate2 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)  ELSE IRT.Rate2 END ELSE 
				  CASE WHEN @RateIndex = 3 
				     THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate3 - ((IRT.Rate3 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate3 END ELSE 
				    CASE WHEN @RateIndex = 4 
					    THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate4 - ((IRT.Rate4 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate4 END ELSE 
					  CASE WHEN @RateIndex = 5 
					      THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN  IRT.Rate5 - ((IRT.Rate5 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate5 END ELSE
			           CASE WHEN @RateIndex = 0 
					        THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN  IRT.MRP - ((IRT.MRP * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.MRP END
					     END END END END END END as Price
			,CASE WHEN (@RateIndex = 1 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate1 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
				CASE WHEN (@RateIndex = 2 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate2 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)ELSE 
				  CASE WHEN (@RateIndex = 3 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate3 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
				    CASE WHEN (@RateIndex = 4 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate4 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
					  CASE WHEN (@RateIndex = 5 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate5 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
					    CASE WHEN (@RateIndex = 0 and IRT.IsRateTaxInclusive = 1)  THEN ((IRT.MRP * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)
						     END END END END END END as TaxRate

		FROM 
			posItemMaster IM INNER JOIN posCounterItemTran CIM on IM.ItemMasterId = CIM.linktoItemMasterId
			INNER JOIN posItemCategoryTran ICT on IM.ItemMasterId = ICT.linktoItemMasterId 
			LEFT JOIN posItemOptionTran IOT on IOT.linktoItemMasterId = IM.ItemMasterId 
			LEFT JOIN posItemRateTran IRT on IM.ItemMasterId = IRT.linktoItemMasterId
		WHERE
			ICT.linktoCategoryMasterId=ISNULL(@linktoCategoryMasterId,ICT.linktoCategoryMasterId)
			--and IM.linktoItemTypeMasterId in (SELECT * FROM dbo.Parse(ISNULL((@ItemTypeMasterIds),IM.linktoItemTypeMasterId), ','))
			and IM.IsEnabled = 1
			and IM.IsDeleted = 0
			and CIM.linktoCounterMasterId=@linktoCounterMasterId
			and linktoBusinessMasterId=@linktoBusinessMasterId
			and IM.IsFavourite = ISNULL(@IsFavourite,IM.IsFavourite)
			and ((@ItemMasterIds IS NULL) OR (@ItemMasterIds IS NOT NULL AND IM.ItemMasterId In (select * from dbo.Parse(@ItemMasterIds,','))))



	--SELECT 
	--	@TotalRowCount = COUNT(Distinct ItemMasterId)
	--FROM 
	--	posItemMaster IM INNER JOIN posCounterItemTran CIM on IM.ItemMasterId = CIM.linktoItemMasterId
	--		INNER JOIN posItemCategoryTran ICT on IM.ItemMasterId = ICT.linktoItemMasterId
	--WHERE
	--	ICT.linktoCategoryMasterId=ISNULL(@linktoCategoryMasterId,ICT.linktoCategoryMasterId)
	--	--and IM.linktoItemTypeMasterId in (SELECT * FROM dbo.Parse(ISNULL((@ItemTypeMasterIds),IM.linktoItemTypeMasterId), ','))
	--	and IM.IsEnabled = 1
	--	and IM.IsDeleted = 0
	--	and IM.IsRowMaterial = 0
	--	and CIM.linktoCounterMasterId=@linktoCounterMasterId

	--SELECT Distinct * FROM
	--(
	--	SELECT Distinct IM.ItemMasterId AS ID, DENSE_RANK() OVER(ORDER BY ItemMasterId) AS RowNumber
	--		, IM.*	
	--		--,(SELECT ItemType from posItemTypeMaster where ItemTypeMasterId=linktoItemTypeMasterId) As ItemType
	--		,(SELECT UnitName from posUnitMaster where UnitMasterId=linktoUnitMasterId) As Unit
	--		,(SELECT StatusName from posItemStatusMaster where ItemStatusMasterId=linktoItemStatusMasterId) As ItemStatus
	--	FROM 
	--		posItemMaster IM INNER JOIN posCounterItemTran CIM on IM.ItemMasterId = CIM.linktoItemMasterId
	--		INNER JOIN posItemCategoryTran ICT on IM.ItemMasterId = ICT.linktoItemMasterId
	--	WHERE
	--		ICT.linktoCategoryMasterId=ISNULL(@linktoCategoryMasterId,ICT.linktoCategoryMasterId)
	--		--and IM.linktoItemTypeMasterId in (SELECT * FROM dbo.Parse(ISNULL((@ItemTypeMasterIds),IM.linktoItemTypeMasterId), ','))
	--		and IM.IsEnabled = 1
	--		and IM.IsDeleted = 0
	--		and IM.IsRowMaterial = 0
	--		and CIM.linktoCounterMasterId=@linktoCounterMasterId
	--	)
	--AS TEMP_TABLE
	--WHERE RowNumber BETWEEN @StartRowIndex + 1 AND @StartRowIndex + @PageSize

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posItemMasterByCounterId_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMasterByCounterId_Select]
	@ItemMasterId int
	,@CounterMasterId  smallint 
	,@linktoOrderTypeMasterId smallint

AS
BEGIN

	SET NOCOUNT ON;

	DECLARE @RateIndex smallint

    SELECT @RateIndex = (CASE WHEN @linktoOrderTypeMasterId = 1 THEN DineInRateIndex ELSE
					CASE WHEN @linktoOrderTypeMasterId = 2 THEN TakeAwayRateIndex ELSE
					HomeDeliveryRateIndex END END) from posCounterItemRateTran where linktoCounterMasterId = @CounterMasterId
	SELECT
		IM.*
		,(SELECT UnitName from posUnitMaster where UnitMasterId=IM.linktoUnitMasterId) As Unit		
		,(SELECT MRP from posItemRateTran where linktoItemMasterId=IM.ItemMasterId) As MRP
		,@RateIndex as RateIndex
		,Convert(varchar,IRT.Tax1) + ','+ Convert(varchar,IRT.Tax2)  + ','+Convert(varchar,IRT.Tax3)  + ','+ Convert(varchar,IRT.Tax4)  + ','+ Convert(varchar,IRT.Tax5) as Tax
			,CASE WHEN @RateIndex = 1
			       THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate1 - ((IRT.Rate1 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate1 END ELSE 
				CASE WHEN @RateIndex = 2 
				    THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate2 - ((IRT.Rate2 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)  ELSE IRT.Rate2 END ELSE 
				  CASE WHEN @RateIndex = 3 
				     THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate3 - ((IRT.Rate3 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate3 END ELSE 
				    CASE WHEN @RateIndex = 4 
					    THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate4 - ((IRT.Rate4 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate4 END ELSE 
					  CASE WHEN @RateIndex = 5 
					      THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN  IRT.Rate5 - ((IRT.Rate5 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate5 END ELSE
			           CASE WHEN @RateIndex = 0 
					        THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN  IRT.MRP - ((IRT.MRP * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.MRP END
					     END END END END END END as Price
		,CASE WHEN (@RateIndex = 1 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate1 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
				CASE WHEN (@RateIndex = 2 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate2 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)ELSE 
				  CASE WHEN (@RateIndex = 3 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate3 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
				    CASE WHEN (@RateIndex = 4 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate4 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
					  CASE WHEN (@RateIndex = 5 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate5 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
					    CASE WHEN (@RateIndex = 0 and IRT.IsRateTaxInclusive = 1)  THEN ((IRT.MRP * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)
						     END END END END END END as TaxRate
	FROM
		posItemMaster IM JOIN posCounterItemTran CIT on IM.ItemMasterId = CIT.linktoItemMasterId 
		LEFT JOIN posItemRateTran IRT on IRT.linktoItemMasterId = IM.ItemMasterId
	WHERE
		ItemMasterId = @ItemMasterId
		and CIT.linktoCounterMasterId = @CounterMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterByItemMasterIds_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMasterByItemMasterIds_SelectAll]
	@ItemMasterIds varchar(max)
AS
BEGIN
	SELECT 
		IM.ItemMasterId,IM.ItemName,IM.ItemCode,IM.BarCode,IM.linktoCategoryMasterId,IM.linktoUnitMasterId,IM.IsFavourite,IRT.PurchaseRate,IRT.MRP,IRT.SaleRate,IRT.Rate1,
		IRT.Rate2,IRT.Rate3,IRT.Rate4,IRT.Rate5,IRT.IsRateTaxInclusive,IRT.Tax1,IRT.Tax2,IRT.Tax3,IRT.Tax4,IRT.Tax5
	FROM
		posItemMaster IM
	JOIN 
		posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
	WHERE ItemMasterId IN (SELECT parsevalue FROM dbo.Parse(@ItemMasterIds,','))
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterByMostSellingItems_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMasterByMostSellingItems_SelectAll]
	-- Add the parameters for the stored procedure here
	 @FromDate datetime
	,@ToDate datetime
	,@linktoBusinessMasterId smallint
AS
BEGIN
	
	SET NOCOUNT ON;

	SELECT 
	IM.ItemMasterId,IM.ItemName,COUNT(linktoItemMasterId) AS SellingCount 
	FROM 
	posItemMaster IM 
	LEFT JOIN
	(
		SELECT DISTINCT 
			SIT.linktoItemMasterId,SIT.SalesItemTranId 
		FROM 
			posSalesItemTran SIT 
		JOIN  
			posSalesMaster SM ON SM.SalesMasterId = SIT.linktoSalesMasterId 
				AND Convert(varchar(8),BillDateTime,112) BETWEEN Convert(varchar(8),@FromDate,112) AND Convert(varchar(8),@ToDate,112)
	) Temp ON linktoItemMasterId = IM.ItemMasterId
	WHERE  
	IM.linktoBusinessMasterId = @linktoBusinessMasterId
	AND IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND IM.ItemType = 0
	GROUP BY IM.ItemMasterId,IM.ItemName
	ORDER BY SellingCount DESC

	
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterBySupplierMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posItemMasterBySupplierMasterId_SelectAll 1,1
CREATE PROCEDURE [dbo].[posItemMasterBySupplierMasterId_SelectAll]
	@linktoSupplierMasterId smallint,
	@linktoBusinessMasterId smallint
AS
BEGIN
	SELECT 
		DISTINCT IM.ItemMasterId,IM.ItemName,IM.ItemCode,IM.linktoUnitMasterId,UM.UnitName
	FROM
		posSupplierMaster SM
	JOIN
		posSupplierItemTran SIT ON SIT.linktoSupplierMasterId = SM.SupplierMasterId
	JOIN 
		posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
	JOIN
		posUnitMaster UM ON UM.UnitMasterId = IM.linktoUnitMasterId
	WHERE
		SM.IsEnabled = 1 and SM.IsDeleted = 0 AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
		AND SIT.linktoSupplierMasterId = @linktoSupplierMasterId AND IM.IsEnabled = 1 and IM.IsDeleted = 0
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterByTableMasterIds_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posItemMasterByTableMasterIds_SelectAll]
	@TableMasterIds varchar(100),
	@FromDate datetime
AS
BEGIN
	SELECT ItemMasterId, 0 As ItemModifierMasterIds, Quantity, ItemCode, OIT.ItemPoint, ItemName, Rate As ActualPrice, 
		(Rate * Quantity) As Rate, ISNULL(ItemRemark,'') AS ItemRemark, UM.UnitName, OM.OrderMasterId, IM.linktoUnitMasterId,
		OIT.OrderItemTranId, (((Rate * Quantity) * IRT.Tax1) / 100) Tax1, (((Rate * Quantity) * IRT.Tax2) / 100) Tax2, 
		(((Rate * Quantity) * IRT.Tax3) / 100) Tax3, (((Rate * Quantity) * IRT.Tax4) / 100) Tax4, (((Rate * Quantity) * IRT.Tax5) / 100) Tax5
	FROM posOrderItemTran OIT, posItemMaster IM, posItemRateTran IRT, posUnitMaster UM, posOrderMaster OM
	WHERE 
		  ',' + linktoTableMasterIds + ','  like '%,' + CONVERT(varchar,ISNULL(@TableMasterIds, OM.linktoTableMasterIds))+ ',%'
	      AND CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8),ISNULL(@FromDate, OrderDateTime), 112)
		  AND OM.linktoSalesMasterId IS NULL
		  AND IM.ItemMasterId = OIT.linktoItemMasterId
		  AND UM.UnitMasterId = IM.linktoUnitMasterId
		  AND OM.OrderMasterId = OIT.linktoOrderMasterId
		  AND IRT.linktoItemMasterId = IM.ItemMasterId

	UNION ALL

	SELECT OIT.linktoItemMasterId, IM.ItemMasterId As ItemModifierMasterIds, 0 As Quantity, ItemCode, OIT.ItemPoint, ItemName, IMT.Rate As ActualPrice
		,(IMT.Rate * Quantity) As Rate, '' As ItemRemark, '' As UnitName, OM.OrderMasterId, 0 As linktoUnitMasterId, OIT.OrderItemTranId, 
		0 As Tax1, 0 As Tax2, 0 As Tax3, 0 As Tax4, 0 As Tax5
	FROM 
		posOrderItemTran OIT, posOrderItemModifierTran IMT, posItemMaster IM, posOrderMaster OM
	WHERE 
		  ',' + linktoTableMasterIds + ','  like '%,' + CONVERT(varchar,ISNULL(@TableMasterIds, OM.linktoTableMasterIds))+ ',%'
	      AND CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8),ISNULL(@FromDate, OrderDateTime), 112)
		  AND OM.linktoSalesMasterId IS NULL
		  AND IMT.linktoOrderItemTranId = OIT.OrderItemTranId
		  AND IM.ItemMasterId = IMT.linktoItemMasterId
		  AND OM.OrderMasterId = OIT.linktoOrderMasterId
	ORDER BY OM.OrderMasterId DESC, ItemMasterId, OrderItemTranId, ItemModifierMasterIds

	--SELECT ItemMasterId,0 As ItemModifierMasterIds,Quantity,ItemCode,OIT.ItemPoint,ItemName,Rate As ActualPrice,(Rate * Quantity)As Rate,ISNULL(ItemRemark,'') AS ItemRemark,UM.UnitName,OM.OrderMasterId,IM.linktoUnitMasterId,OIT.OrderItemTranId,OIT.Tax1,OIT.Tax2,OIT.Tax3,OIT.Tax4,OIT.Tax5
	--FROM 
	--	posOrderItemTran OIT
	--JOIN 
	--	posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId
	--JOIN 
	--	posUnitMaster UM ON UM.UnitMasterId = IM.linktoUnitMasterId
	--JOIN 
	--	posOrderMaster OM ON OM.OrderMasterId = OIT.linktoOrderMasterId
	--WHERE 
	--	  ',' + linktoTableMasterIds + ','  like '%,' + CONVERT(varchar,ISNULL(@TableMasterIds,OM.linktoTableMasterIds))+ ',%'
	--      and CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8),ISNULL(@FromDate,OrderDateTime), 112)
	--	  and OM.linktoSalesMasterId IS NULL
	--UNION ALL
	--SELECT OIT.linktoItemMasterId,IM.ItemMasterId As ItemModifierMasterIds,0 As Quantity,ItemCode,OIT.ItemPoint,ItemName,IMT.Rate As ActualPrice
	--	,(IMT.Rate * Quantity)As Rate,'' As ItemRemark,'' As UnitName,OM.OrderMasterId,0 As linktoUnitMasterId,OIT.OrderItemTranId,0 As Tax1,0 As Tax2,0 As Tax3,0 As Tax4,0 As Tax5
	--FROM 
	--	posOrderItemTran OIT
	--JOIN
	--	posOrderItemModifierTran IMT ON IMT.linktoOrderItemTranId = OIT.OrderItemTranId
	--JOIN 
	--	posItemMaster IM ON IM.ItemMasterId = IMT.linktoItemMasterId
	--JOIN 
	--	posOrderMaster OM ON OM.OrderMasterId = OIT.linktoOrderMasterId
	--WHERE 
	--	  ',' + linktoTableMasterIds + ','  like '%,' + CONVERT(varchar,ISNULL(@TableMasterIds,OM.linktoTableMasterIds))+ ',%'
	--      and CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8),ISNULL(@FromDate,OrderDateTime), 112)
	--	  and OM.linktoSalesMasterId IS NULL
	--ORDER BY OM.OrderMasterId DESC,ItemMasterId,OrderItemTranId,ItemModifierMasterIds
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterComboItem_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- [posItemMasterComboItem_SelectAll] 1,1
CREATE PROCEDURE [dbo].[posItemMasterComboItem_SelectAll]
	@linktoItemMasterId int,
	@ItemType int,
	@linktoBusinessMasterId smallint
AS
BEGIN
	SELECT 
		IM.ItemMasterId,IM.ItemName,ICT.ItemComboTranId,IM.ItemCode
		,(select CategoryName FROM posCategoryMaster WHERE im.linktoCategoryMasterId=CategoryMasterId) AS CategoryName
	FROM
		posItemMaster IM
	LEFT JOIN
		posItemComboTran ICT ON ICT.linktoItemMasterIdCombo = IM.ItemMasterId AND ICT.linktoItemMasterId = @linktoItemMasterId
	WHERE 
		ItemType = @ItemType AND IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY CategoryName 


	--SELECT DISTINCT
	--	 Category,ItemName,ItemCode,ItemMasterId,ItemComboTranId,linktoItemMasterId,ItemType
	--FROM
	--(
	--	SELECT DISTINCT
	
	--	CM.CategoryName  AS Category,
	--	IM.ItemName,IM.ItemCode,IM.ItemMasterId,IM.ItemType, ICT.*

	--	FROM			
	--		posItemMaster IM 	
	--	JOIN	posCategoryMaster CM ON  CM.CategoryMasterId = IM.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
	--	LEFT JOIN	
	--		posItemComboTran ICT  ON ItemMasterId = @linktoItemMasterId
	--	WHERE 
	--		IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
	--		IM.ItemType =@ItemType AND CM.IsDeleted = 0 AND CM.IsEnabled = 1
	--UNION ALL
	--	SELECT DISTINCT
	
	--			CM.CategoryName  AS Category,
	--			IM.ItemName,IM.ItemCode,IM.ItemMasterId,IM.ItemType, ICT2.*
	--		FROM			
	--			posItemMaster IM 
	--		JOIN 
	--				posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
	--		JOIN	posCategoryMaster CM ON  CM.CategoryMasterId = ICT.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
	--		LEFT JOIN	
	--				posItemComboTran ICT2  ON ItemMasterId = @linktoItemMasterId
	--		WHERE 
	--			IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
	--			IM.ItemType =@ItemType  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1
	--)TempTable
	--ORDER BY Category
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterImport_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMasterImport_Insert]
	@ItemMasterId int OUTPUT
	,@ItemName varchar(50)
	,@ItemCode varchar(20)
	,@Unit varchar(50)
	,@IsFavourite bit
	,@ItemPoint smallint
	,@PriceByPoint smallint
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@ItemType smallint
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@CategoryName varchar(80)
	,@MRP money
	,@Rate1 money
	,@IsRateTaxInclusive bit 
	,@Status smallint OUTPUT
	,@IsDineInOnly bit
AS
BEGIN
	
	DECLARE @linktoCategoryMasterId smallint,@linktoUnitMasterId smallint,@ItemRateTranId int,@StatusCM smallint ,@StatusUM smallint,@StatusIRT smallint

	SET @linktoCategoryMasterId = 0
	SET @linktoUnitMasterId = 0

	BEGIN TRAN
	
	BEGIN TRY
	SELECT @linktoCategoryMasterId = CategoryMasterId  FROM posCategoryMaster WHERE CategoryName = @CategoryName AND IsDeleted = 0 
		AND IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId

	IF @linktoCategoryMasterId = 0
	BEGIN
		EXEC posCategoryMaster_Insert @linktoCategoryMasterId OUTPUT,@CategoryName,NULL,NULL,NULL,NULL,@linktoBusinessMasterId,NULL,@IsEnabled,@IsDeleted,@CreateDateTime,@linktoUserMasterIdCreatedBy,0,@StatusCM OUTPUT
		IF @StatusCM = -1
		BEGIN
			set @Status=@StatusCM
			ROLLBACK TRAN
			RETURN
		END
	END

	SELECT @linktoUnitMasterId = UnitMasterId  FROM posUnitMaster WHERE UnitName = @Unit AND IsDeleted = 0 

	IF @linktoUnitMasterId = 0
	BEGIN
		EXEC posUnitMaster_Insert @linktoUnitMasterId OUTPUT,@Unit,'',NULL,@IsEnabled,@IsDeleted,@StatusUM OUTPUT
		IF @StatusUM = -1
		BEGIN
		set @Status=@StatusUM
			ROLLBACK TRAN
			RETURN
		END
	END


	IF EXISTS(SELECT ItemMasterId FROM posItemMaster WHERE (ItemName = @ItemName OR ItemCode = @ItemCode) AND ItemType = @ItemType AND IsDeleted = 0 AND IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @ItemMasterId = ItemMasterId FROM posItemMaster WHERE (ItemName = @ItemName OR ItemCode = @ItemCode) and ItemType = @ItemType AND IsDeleted = 0 AND IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		ROLLBACK TRAN
		RETURN
	END
	INSERT INTO posItemMaster
	(
		ItemName
		,ItemCode
		,linktoUnitMasterId
		,IsFavourite
		,linktoCategoryMasterId
		,ItemPoint
		,PriceByPoint
		,linktoBusinessMasterId
		,IsEnabled
		,IsDeleted
		,ItemType
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,IsDineInOnly
		
	)
	VALUES
	(
		@ItemName
		,@ItemCode
		,@linktoUnitMasterId
		,@IsFavourite
		,@linktoCategoryMasterId
		,@ItemPoint
		,@PriceByPoint
		,@linktoBusinessMasterId
		,@IsEnabled
		,@IsDeleted
		,@ItemType
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy	
		,@IsDineInOnly	
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
		ROLLBACK TRAN
		RETURN
	END
	ELSE
	BEGIN
		SET @ItemMasterId = @@IDENTITY
		SET @Status = 0
	END

	EXEC posItemRateTran_Insert @ItemRateTranId OUTPUT,@ItemMasterId,0,@MRP,0,@Rate1,0,0,0,0,0,0,0,0,0,@IsRateTaxInclusive,@CreateDateTime,@linktoUserMasterIdCreatedBy,@StatusIRT OUTPUT	
	
	IF @StatusIRT = -1
	BEGIN 	
	set @Status=@StatusIRT
		ROLLBACK TRAN
		RETURN 
	END
	ELSE
	BEGIN			
		set @Status=@StatusIRT
	END

	COMMIT TRAN

	END TRY
	BEGIN CATCH
		set @Status=@StatusIRT
		ROLLBACK TRAN
	END CATCH

	RETURN

END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterItemName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemMasterItemName_SelectAll 1
CREATE PROCEDURE [dbo].[posItemMasterItemName_SelectAll]
@ItemType smallint = NULL,
@linktoBusinessMasterId smallint,
@linktoCategoryMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 ItemMasterId
		,ItemName
		,ItemCode
		,linktoUnitMasterId
		,linktoCategoryMasterId
		,(SELECT UnitName from posUnitMaster Where UnitMasterId = linktoUnitMasterId) AS Unit		
	FROM
		 posItemMaster
	WHERE
	 IsEnabled = 1
	 AND IsDeleted = 0
	 AND ((@ItemType IS NULL AND (Itemtype IS NULL OR ItemType IS NOT NULL)) OR (@ItemType IS NOT NULL AND ItemType = @ItemType))
	 AND linktoBusinessMasterId = @linktoBusinessMasterId
	  AND linktoCategoryMasterId= CASE WHEN @linktoCategoryMasterId =0 THEN linktoCategoryMasterId ELSE @linktoCategoryMasterId END 
	ORDER BY ItemName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterItemNameByCategory_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemMasterItemNameByCategory_SelectAll  2
CREATE PROCEDURE [dbo].[posItemMasterItemNameByCategory_SelectAll] 
		@linktoBusinessMasterId smallint
AS
BEGIN 
	SET NOCOUNT ON 
	SELECT DISTINCT * FROM (
		SELECT IM.ItemName,IM.ItemMasterId,ICT.linktoCategoryMasterId as CategoryMasterId
			FROM
				posItemMaster IM
			JOIN 
				posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId 
			WHERE 
				IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND IM.ItemType = 0 
				AND IM.linktoBusinessMasterId=@linktoBusinessMasterId
	UNION ALL
		SELECT IM.ItemName,IM.ItemMasterId,IM.linktoCategoryMasterId
			FROM
				posItemMaster IM
			WHERE 
				IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND IM.ItemType = 0
				AND IM.linktoBusinessMasterId=@linktoBusinessMasterId
		)TempTable	 
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterItemNameSupplierWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemMasterItemName_SelectAll 1
CREATE PROCEDURE [dbo].[posItemMasterItemNameSupplierWise_SelectAll]
@ItemType smallint,
@linktoBusinessMasterId smallint,
@CategoryMasterIds varchar(1000),
@linktoSupplierMasterId smallint

AS
BEGIN
	SET NOCOUNT ON

	SELECT DISTINCT
		 ItemMasterId
		,ItemName
		,ItemCode				
	FROM
		 posItemMaster IM
		 JOIN posSupplierItemTran SIT ON  SIT.linktoItemMasterId=IM.ItemMasterId
		
	WHERE
	 IsEnabled = 1
	 AND IsDeleted = 0
	 AND ((@ItemType IS NULL AND (Itemtype IS NULL OR ItemType IS NOT NULL)) OR (@ItemType IS NOT NULL AND ItemType = @ItemType))
	 AND linktoBusinessMasterId = @linktoBusinessMasterId
	  AND linktoCategoryMasterId IN (SELECT parseValue FROM dbo.Parse(@CategoryMasterIds,','))
	  AND SIT.linktoSupplierMasterId = CASE WHEN @linktoSupplierMasterId=0 THEN SIT.linktoSupplierMasterId ELSE @linktoSupplierMasterId END
	ORDER BY ItemName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterKOTItemByOrderMasterID_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMasterKOTItemByOrderMasterID_SelectAll]
	@OrderMasterIDs varchar(1000)
AS
BEGIN
    SELECT * FROM
    (
	   SELECT OIT.OrderItemTranId AS OrderId, OrderItemTranId, OIT.Rate, ItemRemark, ItemMasterId, ItemName, Quantity, 0 AS ItemMasterIdModifier, RateIndex
		, MRP, Rate1, Rate2, Rate3, Rate4, Rate5, Tax1, Tax2, Tax3, Tax4, Tax5
	   FROM posOrderItemTran OIT, posItemMaster, posOrderMaster, posItemRateTran IRT
	   WHERE linktoOrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIDs,','))
		  AND OIT.linktoItemMasterId = ItemMasterId
		  AND linktoOrderMasterId = OrderMasterId
		  AND IRT.linktoItemMasterId = ItemMasterId

	   UNION ALL

	   SELECT OIT.OrderItemTranId AS OrderId, OrderItemTranId, OIMT.Rate, '', OIT.linktoItemMasterId, ItemName, Quantity, OIMT.linktoItemMasterId AS ItemMasterIdModifier, RateIndex
		, MRP, Rate1, Rate2, Rate3, Rate4, Rate5, Tax1, Tax2, Tax3, Tax4, Tax5
	   FROM posOrderItemTran OIT, posItemMaster, posOrderItemModifierTran OIMT, posOrderMaster, posItemRateTran IRT
	   WHERE linktoOrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIDs,','))
		  AND OrderItemTranId = linktoOrderItemTranId
		  AND ItemMasterId = OIMT.linktoItemMasterId
		  AND linktoOrderMasterId = OrderMasterId
		  AND IRT.linktoItemMasterId = ItemMasterId
    ) AS TEMP
    ORDER BY OrderItemTranId,ItemMasterId,ItemMasterIdModifier

	--SELECT 
	--	(-1 * DENSE_RANK() OVER (ORDER BY OrderItemTranId)) As OrderId,OrderItemTranId,ItemMasterId,ItemName,ItemMasterIdModifier,SUM(Quantity) Quantity,SUM(DiscountAmount) DiscountAmount,SUM(MRP) MRP,SUM(Rate1) Rate1,SUM(Rate2) Rate2,SUM(Rate3) Rate3,
	--	SUM(Rate4) Rate4,SUM(Rate5) Rate5,SUM(Tax1) Tax1,SUM(Tax2) Tax2,SUM(Tax3) Tax3,SUM(Tax4) Tax4,SUM(Tax5) Tax5,SUM(MRPWithTax) MRPWithTax,
	--	SUM(Rate1WithTax) Rate1WithTax,SUM(Rate2WithTax) Rate2WithTax,SUM(Rate3WithTax) Rate3WithTax,SUM(Rate4WithTax) Rate4WithTax,
	--	SUM(Rate5WithTax) Rate5WithTax,RateIndex,ItemRemark
	--FROM
	--( 
	--	SELECT
	--		OrderItemTranId,ItemMasterId,ItemName,0 AS ItemMasterIdModifier,Quantity,DiscountAmount,
	--		CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (MRP - ((MRP * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE MRP END MRP,
	--			CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate1 - ((Rate1 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate1 END Rate1,
	--			CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate2 - ((Rate2 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate2 END Rate2,
	--			CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate3 - ((Rate3 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate3 END Rate3,
	--			CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate4 - ((Rate4 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate4 END Rate4,
	--			CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate5 - ((Rate5 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate5 END Rate5,
	--		IRT.Tax1,IRT.Tax2,IRT.Tax3,IRT.Tax4,IRT.Tax5,
	--		MRP As MRPWithTax,Rate1 As Rate1WithTax,Rate2 As Rate2WithTax,Rate3 As Rate3WithTax,Rate4 As Rate4WithTax,Rate5 As Rate5WithTax,OM.RateIndex,
	--		OIT.ItemRemark
	--	FROM
	--		posOrderMaster OM
	--	JOIN
	--		posOrderItemTran OIT ON OIT.linktoOrderMasterId = OM.OrderMasterId
	--	JOIN
	--		posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId
	--	JOIN
	--		posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
	--	WHERE 
	--		OM.OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIDs,',')) 
	--	UNION ALL
	--	SELECT
	--		OrderItemTranId,OIT.linktoItemMasterId,ItemName,ItemMasterId,Quantity,0,
	--		MRP,MRP As Rate1,MRP As Rate2,MRP As Rate3,MRP As Rate4,MRP As Rate5,IRT.Tax1,IRT.Tax2,IRT.Tax3,IRT.Tax4,IRT.Tax5,
	--		0 As MRPWithTax,0 As Rate1WithTax,0 As Rate2WithTax,0 As Rate3WithTax,0 As Rate4WithTax,0 As Rate5WithTax,OM.RateIndex,
	--		OIT.ItemRemark
	--	FROM
	--		posOrderMaster OM
	--	JOIN
	--		posOrderItemTran OIT ON OIT.linktoOrderMasterId = OM.OrderMasterId
	--	JOIN
	--		posOrderItemModifierTran OIMT ON OIMT.linktoOrderItemTranId = OIT.OrderItemTranId
	--	JOIN
	--		posItemMaster IM ON IM.ItemMasterId = OIMT.linktoItemMasterId
	--	JOIN
	--		posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
	--	WHERE 
	--		OM.OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIDs,',')) 
	--)Temp
	--GROUP BY OrderItemTranId,ItemMasterId,ItemName,ItemMasterIdModifier,RateIndex,ItemRemark
	--ORDER BY OrderItemTranId,ItemMasterId,ItemRemark,ItemMasterIdModifier
END




GO
/****** Object:  StoredProcedure [dbo].[posItemMasterModifier_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMasterModifier_Select]

	@ItemMasterId int

AS
BEGIN

	SET NOCOUNT ON;

	SELECT
		posItemMaster.*
		,(SELECT MRP from posItemRateTran where linktoItemMasterId=@ItemMasterId) As MRP

	FROM
		posItemMaster
	WHERE
		ItemMasterId = @ItemMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterModifier_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posItemMasterModifier_SelectAll]
	@ItemType smallint,
	@IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posItemMaster.*
		,(SELECT MRP from posItemRateTran WHERE linktoItemMasterId = ItemMasterId) AS MRP 	
	FROM
		 posItemMaster
	WHERE
		IsEnabled=@IsEnabled
	    AND ItemType = @ItemType 
		AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY ItemName 
	

	RETURN
END



	


GO
/****** Object:  StoredProcedure [dbo].[posItemMasterRawMaterial_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[posItemMasterRawMaterial_Update]
	 @ItemMasterId int
	,@ItemName varchar(50)
	,@ShortName varchar(20) = NULL
	,@ItemCode varchar(20) 
	,@ShortDescription varchar(500) = NULL
	,@linktoUnitMasterId smallint 
	,@linktoBusinessMasterId smallint 
	,@IsEnabled bit
	,@IsDeleted bit 
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoCategoryMasterId smallint
	,@ItemType smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT ItemMasterId FROM posItemMaster WHERE (ItemName = @ItemName OR ItemCode = @ItemCode) AND ItemMasterId != @ItemMasterId AND Itemtype = @Itemtype AND IsDeleted = 0)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posItemMaster
	SET
		 ShortName = @ShortName
		,ItemName = @ItemName
		,ItemCode = @ItemCode 
		,ShortDescription = @ShortDescription
		,linktoUnitMasterId = @linktoUnitMasterId 
		,linktoBusinessMasterId = @linktoBusinessMasterId 
		,linktoCategoryMasterId = @linktoCategoryMasterId
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted 
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		ItemMasterId = @ItemMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterSaleItemBySalesMasterID_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMasterSaleItemBySalesMasterID_SelectAll]
	@SalesMasterId bigint
AS
BEGIN
	SELECT 
		ItemMasterId,ItemModifierMasterIds,SUM(Quantity) Quantity,ItemCode,ItemName,ActualPrice,SUM(Rate) Rate,ItemRemark,UnitName,linktoCounterMasterId 
	FROM
	(
		SELECT SIT.linktoItemMasterId ItemMasterId,0 As ItemModifierMasterIds,SIT.SalesItemTranId,Quantity,ItemCode,ItemName,(Rate/Quantity) As ActualPrice,Rate,ISNULL(ItemRemark,'') AS ItemRemark,UM.UnitName,SM.linktoCounterMasterId
		FROM 
			posSalesItemTran SIT		
		JOIN 
			posUnitMaster UM ON UM.UnitMasterId = SIT.linktoUnitMasterId
		JOIN 
			posSalesMaster SM ON SM.SalesMasterId = SIT.linktoSalesMasterId
		WHERE SIT.linktoSalesMasterId = @SalesMasterId
		UNION ALL
		SELECT SIT.linktoItemMasterId,IMT.linktoItemMasterId As ItemModifierMasterIds,SIT.SalesItemTranId,0 As Quantity,IM.ItemCode,IM.ItemName,(IMT.Rate) As ActualPrice
			,IMT.Rate,ISNULL(ItemRemark,' ') As ItemRemark,' ' As UnitName,SM.linktoCounterMasterId
		FROM 
			posSalesItemTran SIT
		JOIN
			posSalesItemModifierTran IMT ON IMT.linktoSalesItemTranId = SIT.SalesItemTranId	
		JOIN 
			posItemMaster IM ON IM.ItemMasterId = IMT.linktoItemMasterId	
		JOIN 
			posSalesMaster SM ON SM.SalesMasterId = SIT.linktoSalesMasterId
		WHERE SIT.linktoSalesMasterId = @SalesMasterId
	)Temp
	GROUP BY ItemMasterId,ItemModifierMasterIds,ItemCode,ItemName,ActualPrice,ItemRemark,UnitName,linktoCounterMasterId 
	ORDER BY ItemMasterId,ItemRemark,ItemModifierMasterIds
END



GO
/****** Object:  StoredProcedure [dbo].[posItemMasterWithAddlessTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemMasterWithAddlessTran_SelectAll  5,1
CREATE PROCEDURE [dbo].[posItemMasterWithAddlessTran_SelectAll]
	@linktoAddLessMasterId smallint = null
	,@ItemType smallint 
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		IM.ItemMasterId,IM.ItemName,IM.ShortName,IM.ItemCode, AIT.AddLessItemTranId
		,(select CategoryName FROM posCategoryMaster WHERE IM.linktoCategoryMasterId=CategoryMasterId) AS Category
	FROM
		 posItemMaster IM
		 LEFT JOIN posAddLessItemTran AIT ON AIT. linktoItemMasterId = IM.ItemMasterId AND AIT.linktoAddLessMasterId=@linktoAddLessMasterId
	WHERE
			ItemType= @ItemType
		AND  IM.IsDeleted = 0 
		AND IM.IsEnabled=1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
	
	ORDER BY Category

	RETURN
END



	


GO
/****** Object:  StoredProcedure [dbo].[posItemMaterRawMaterial_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemMaterRawMaterial_SelectAll]
   @ItemMasterIds varchar(Max)
   
   AS
BEGIN
  SET NOCOUNT ON;
  SELECT IM.ItemMasterId,IM.ItemName,IM.ItemCode,UM.UnitName, CM.CategoryName,IRT.PurchaseRate

  FROM posItemMaster IM 

   INNER JOIN posUnitMaster UM ON UM.UnitMasterId= IM.linktoUnitMasterId
   INNER JOIN posCategoryMaster CM ON CM.CategoryMasterId= IM.linktoCategoryMasterId
   INNER JOIN posItemRateTran IRT ON IRT.ItemRateTranId= IM.ItemMasterId
    
  WHERE IM.IsDeleted=0 AND IM.IsEnabled=1 AND ItemMasterId IN (SELECT Parsevalue FROM DBO.Parse(@ItemMasterIds,','))
 
END

GO
/****** Object:  StoredProcedure [dbo].[posItemModifierTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemModifierTran_Insert]
	  @ItemModifierMasterIds varchar(1000) = NULL
	 ,@ItemMasterId int
	 ,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		 posItemModifierTran
	WHERE
		linktoItemMasterId = @ItemMasterId

	IF(@ItemModifierMasterIds IS NOT NULL)

	BEGIN
		INSERT  INTO posItemModifierTran
		(
			linktoItemMasterId,
			linktoItemMasterModifierId
		)
		SELECT 
			@ItemMasterId,parsevalue 
		FROM 
			dbo.Parse(@ItemModifierMasterIds,',')
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemModifierTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemModifierTran_SelectAll 38,2
CREATE PROCEDURE [dbo].[posItemModifierTran_SelectAll]
	@linktoItemMasterId int ,
	@ItemType smallint,
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
	    IM.ItemMasterId,IM.ItemName, IM.ShortName,IM.ShortDescription,IRT.MRP , IMT.*
	FROM
		posItemMaster IM 
		LEFT JOIN posItemModifierTran IMT ON  IM.ItemMasterId = IMT.linktoItemMasterModifierId AND linktoItemMasterId = @linktoItemMasterId
		JOIN posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
	WHERE
		 IM.IsEnabled = 1 AND IM.ItemType = @ItemType AND IM.IsDeleted = 0 AND Im.linktoBusinessMasterId = @linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemOptionTran_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemOptionTran_DeleteAll]
	@linktoItemMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	DELETE
	FROM
		 posItemOptionTran
	WHERE
		linktoItemMasterId IN (SELECT * FROM dbo.Parse(@linktoItemMasterId, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemOptionTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posItemOptionTran_Insert]
	 @linktoItemMasterId int
	,@linktoOptionValueTranId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF NOT EXISTS(SELECT ItemOptionTranId FROM posItemOptionTran WHERE linktoOptionValueTranId = @linktoOptionValueTranId AND linktoItemMasterId = @linktoItemMasterId)
	BEGIN
		INSERT INTO posItemOptionTran
		(
			 linktoItemMasterId
			,linktoOptionValueTranId
		)
		VALUES
		(
			 @linktoItemMasterId
			,@linktoOptionValueTranId
		)
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemOptionTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemOptionTran_SelectAll null
CREATE PROCEDURE [dbo].[posItemOptionTran_SelectAll]
@linktoItemMasterId smallint = null,
@linktoBusinessTypeMasterId smallint
AS
BEGIN

	SET NOCOUNT ON
	SELECT
		OVT.*,IOT.ItemOptionTranId,OM.OptionName
	FROM
		 posOptionValueTran OVT
		 JOIN posOptionMaster OM ON OM.OptionMasterId=OVT.linktoOptionMasterId AND OM.IsDeleted=0
		 LEFT JOIN posItemOptionTran  IOT ON IOT.linktoOptionValueTranId=OVT.OptionValueTranId AND IOT.linktoItemMasterId=@linktoItemMasterId
	WHERE
		OVT.IsDeleted = 0 AND OM.linktoBusinessTypeMasterId = @linktoBusinessTypeMasterId
	ORDER BY linktoOptionMasterId
		

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemOptionTranByItemMastreId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemOptionTranByItemMastreId_SelectAll]
	@linktoItemMasterId smallint
AS
BEGIN
	
	SELECT 
		OVT.*,IOT.ItemOptionTranId,OM.OptionName
	FROM
		posItemOptionTran IOT 
	JOIN
		posOptionValueTran OVT ON OVT.OptionValueTranId = IOT.linktoOptionValueTranId
	JOIN
		posOptionMaster OM ON OM.OptionMasterId = OVT.linktoOptionMasterId
	WHERE 
		linktoItemMasterId = @linktoItemMasterId AND OVT.IsDeleted = 0 AND OM.IsDeleted = 0

END



GO
/****** Object:  StoredProcedure [dbo].[posItemRateTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemRateTran_Insert]
	 @ItemRateTranId int OUTPUT
	,@linktoItemMasterId int
	,@PurchaseRate money
	,@MRP money
	,@SaleRate money
	,@Rate1 money
	,@Rate2 money
	,@Rate3 money
	,@Rate4 money
	,@Rate5 money
	,@Tax1 numeric(5,2)
	,@Tax2 numeric(5,2)
	,@Tax3 numeric(5,2)
	,@Tax4 numeric(5,2)
	,@Tax5 numeric(5,2)
	,@IsRateTaxInclusive bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT ItemRateTranId FROM posItemRateTran WHERE linktoItemMasterId = @linktoItemMasterId)
	BEGIN
		SELECT @ItemRateTranId = ItemRateTranId FROM posItemRateTran WHERE linktoItemMasterId = @linktoItemMasterId

		EXEC posItemRateTran_Update 
			 @linktoItemMasterId 
			,@PurchaseRate
			,@MRP
			,@SaleRate
			,@Rate1
			,@Rate2
			,@Rate3
			,@Rate4
			,@Rate5
			,@Tax1
			,@Tax2
			,@Tax3
			,@Tax4
			,@Tax5
			,@IsRateTaxInclusive
			,@CreateDateTime
			,@linktoUserMasterIdCreatedBy
			,@Status
	END
	ELSE
	BEGIN
		INSERT INTO posItemRateTran
		(
			 linktoItemMasterId
			,PurchaseRate
			,MRP
			,SaleRate
			,Rate1
			,Rate2
			,Rate3
			,Rate4
			,Rate5
			,Tax1  
			,Tax2 
			,Tax3  
			,Tax4 
			,Tax5  
			,IsRateTaxInclusive
			,CreateDateTime
			,linktoUserMasterIdCreatedBy
		
		)
		VALUES
		(
			 @linktoItemMasterId
			,@PurchaseRate
			,@MRP
			,@SaleRate
			,@Rate1
			,@Rate2
			,@Rate3
			,@Rate4
			,@Rate5
			,@Tax1  
			,@Tax2  
			,@Tax3  
			,@Tax4  
			,@Tax5 
			,@IsRateTaxInclusive
			,@CreateDateTime
			,@linktoUserMasterIdCreatedBy
		
		)

		SET @ItemRateTranId = @@IDENTITY
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemRateTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemRateTran_Select]
	 @linktoItemMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posItemRateTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item
	FROM
		 posItemRateTran
	WHERE
		linktoItemMasterId = @linktoItemMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemRateTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemRateTran_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posItemRateTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
	FROM
		 posItemRateTran
	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemRateTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemRateTran_Update]
	 @linktoItemMasterId int
	,@PurchaseRate money
	,@MRP money
	,@SaleRate money
	,@Rate1 money
	,@Rate2 money
	,@Rate3 money
	,@Rate4 money
	,@Rate5 money
	,@Tax1 numeric(5,2)
	,@Tax2 numeric(5,2)
	,@Tax3 numeric(5,2)
	,@Tax4 numeric(5,2)
	,@Tax5 numeric(5,2)
	,@IsRateTaxInclusive bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posItemRateTran
	SET
		 PurchaseRate = @PurchaseRate
		,MRP = @MRP
		,SaleRate = @SaleRate
		,Rate1 = @Rate1
		,Rate2 = @Rate2
		,Rate3 = @Rate3
		,Rate4 = @Rate4
		,Rate5 = @Rate5
		,Tax1 = @Tax1 
    	,Tax2 = @Tax2  
	    ,Tax3 =  @Tax3 
	    ,Tax4 = @Tax4 
     	,Tax5 = @Tax5 
		,IsRateTaxInclusive = @IsRateTaxInclusive
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
	     linktoItemMasterId = @linktoItemMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemRateTranUpdatebyTaxSetting_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posItemRateTranUpdatebyTaxSetting_Update] 
	 @Tax1 decimal
	,@Tax2 decimal
	,@Tax3 decimal
	,@Tax4 decimal
	,@Tax5 decimal
	,@Options smallint
	,@linktoBusinessMasterId int
	,@Status smallint OUTPUT
AS
BEGIN

	DECLARE @OldTax1 decimal,
		@OldTax2 decimal,
		@OldTax3 decimal,
		@OldTax4 decimal,
		@OldTax5 decimal

	IF (@Options = 1)
	BEGIN 
		SELECT @OldTax1=TaxRate FROM posTaxMaster where TaxIndex=1 AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsEnabled=1
		SELECT @OldTax2=TaxRate FROM posTaxMaster where TaxIndex=2 AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsEnabled=1
		SELECT @OldTax3=TaxRate FROM posTaxMaster where TaxIndex=3 AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsEnabled=1
		SELECT @OldTax4=TaxRate FROM posTaxMaster where TaxIndex=4 AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsEnabled=1
		SELECT @OldTax5=TaxRate FROM posTaxMaster where TaxIndex=5 AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsEnabled=1

		UPDATE posItemRateTran SET Tax1 = @Tax1 WHERE Tax1 = @OldTax1  AND linktoItemMasterId IN (SELECT ItemMasterId FROM posItemMaster WHERE linktoBusinessMasterId = @linktoBusinessMasterId)
		UPDATE posItemRateTran SET Tax2 = @Tax2 WHERE Tax2 = @OldTax2  AND linktoItemMasterId IN (SELECT ItemMasterId FROM posItemMaster WHERE linktoBusinessMasterId = @linktoBusinessMasterId)
		UPDATE posItemRateTran SET Tax3 = @Tax3 WHERE Tax3 = @OldTax3  AND linktoItemMasterId IN (SELECT ItemMasterId FROM posItemMaster WHERE linktoBusinessMasterId = @linktoBusinessMasterId)
		UPDATE posItemRateTran SET Tax4 = @Tax4 WHERE Tax4 = @OldTax4  AND linktoItemMasterId IN (SELECT ItemMasterId FROM posItemMaster WHERE linktoBusinessMasterId = @linktoBusinessMasterId)
		UPDATE posItemRateTran SET Tax5 = @Tax5 WHERE Tax5 = @OldTax5  AND linktoItemMasterId IN (SELECT ItemMasterId FROM posItemMaster WHERE linktoBusinessMasterId = @linktoBusinessMasterId)
	END
	ELSE
	BEGIN
		UPDATE 
			posItemRateTran 
		SET
			 Tax1 = @Tax1
			,Tax2 = @Tax2
			,Tax3 = @Tax3
			,Tax4 = @Tax4
			,Tax5 = @Tax5
	END
	IF @@ERROR <> 0
		BEGIN
			SET @Status = -1
		END
		ELSE
		BEGIN
			SET @Status = 0
		END
	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posItemRateTranWithTaxCalculation_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posItemRateTranWithTaxCalculation_Select 2
CREATE PROCEDURE [dbo].[posItemRateTranWithTaxCalculation_Select]
	@linktoItemMasterId int
AS
BEGIN
	SELECT
		ItemRateTranId,linktoItemMasterId,PurchaseRate,SaleRate,IsRateTaxInclusive,
		CASE WHEN IsRateTaxInclusive = 1 THEN (MRP - ((MRP * (Tax1 + Tax2 + Tax3 + Tax4 + Tax5))/100)) ELSE MRP END MRP,
		CASE WHEN IsRateTaxInclusive = 1 THEN (Rate1 - ((Rate1 * (Tax1 + Tax2 + Tax3 + Tax4 + Tax5))/100)) ELSE Rate1 END Rate1,
		CASE WHEN IsRateTaxInclusive = 1 THEN (Rate2 - ((Rate2 * (Tax1 + Tax2 + Tax3 + Tax4 + Tax5))/100)) ELSE Rate2 END Rate2,
		CASE WHEN IsRateTaxInclusive = 1 THEN (Rate3 - ((Rate3 * (Tax1 + Tax2 + Tax3 + Tax4 + Tax5))/100)) ELSE Rate3 END Rate3,
		CASE WHEN IsRateTaxInclusive = 1 THEN (Rate4 - ((Rate4 * (Tax1 + Tax2 + Tax3 + Tax4 + Tax5))/100)) ELSE Rate4 END Rate4,
		CASE WHEN IsRateTaxInclusive = 1 THEN (Rate5 - ((Rate5 * (Tax1 + Tax2 + Tax3 + Tax4 + Tax5))/100)) ELSE Rate5 END Rate5,
		Tax1,Tax2,Tax3,Tax4,Tax5,CreateDateTime,linktoUserMasterIdCreatedBy,UpdateDateTime,linktoUserMasterIdUpdatedBy
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item,
		MRP As MRPWithTax,Rate1 As Rate1WithTax,Rate2 As Rate2WithTax,Rate3 As Rate3WithTax,Rate4 As Rate4WithTax,Rate5 As Rate5WithTax
	FROM
		 posItemRateTran
	WHERE
		linktoItemMasterId = @linktoItemMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posItemRemarkMaster_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemRemarkMaster_Delete]
	 @ItemRemarkMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posItemRemarkMaster
	WHERE
		ItemRemarkMasterId = @ItemRemarkMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemRemarkMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemRemarkMaster_Insert]
	 @ItemRemarkMasterId smallint OUTPUT
	,@ItemRemark varchar(100)
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT ItemRemarkMasterId FROM posItemRemarkMaster WHERE ItemRemark = @ItemRemark AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @ItemRemarkMasterId = ItemRemarkMasterId FROM posItemRemarkMaster WHERE ItemRemark = @ItemRemark AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posItemRemarkMaster
	(
		 ItemRemark
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @ItemRemark
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @ItemRemarkMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemRemarkMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemRemarkMaster_SelectAll]
			@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posItemRemarkMaster.*
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posItemRemarkMaster
	WHERE
		linktoBusinessMasterId= @linktoBusinessMasterId
	ORDER BY ItemRemark ASC

	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posItemStockTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posItemStockTran_Delete]
	 @linktoItemMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posItemStockTran
	WHERE
		linktoItemMasterId = @linktoItemMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemStockTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemStockTran_Insert]
	 @ItemStockTranId int OUTPUT
	,@linktoItemMasterId int
	,@linktoUnitMasterId smallint
	,@linktoDepartmentMasterId smallint
	,@IsMaintainStock bit
	,@OpeningStock numeric(9, 2)
	,@InHand numeric(9, 2)
	,@MinimumStock numeric(9, 2)
	,@MaximumStock numeric(9, 2)
	,@CreateDateTime datetime = NULL
	,@linktoUserMasterIdCreatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT ItemStockTranId FROM posItemStockTran WHERE linktoItemMasterId = @linktoItemMasterId AND linktoDepartmentMasterId = @linktoDepartmentMasterId)
	BEGIN
		SELECT @ItemStockTranId = ItemStockTranId FROM posItemStockTran WHERE linktoItemMasterId = @linktoItemMasterId AND linktoDepartmentMasterId = @linktoDepartmentMasterId

		UPDATE posItemStockTran
		SET
			 linktoItemMasterId = @linktoItemMasterId
			,linktoUnitMasterId = @linktoUnitMasterId
			,linktoDepartmentMasterId = @linktoDepartmentMasterId
			,IsMaintainStock = @IsMaintainStock
			,OpeningStock = @OpeningStock
			,InHand = @InHand
			,MinimumStock = @MinimumStock
			,MaximumStock = @MaximumStock
			,UpdatedDateTime = @CreateDateTime
			,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdCreatedBy
		
		WHERE
			linktoItemMasterId = @linktoItemMasterId 
			AND linktoDepartmentMasterId = @linktoDepartmentMasterId

	END
	ELSE
	BEGIN
		INSERT INTO posItemStockTran
		(
			linktoItemMasterId
			,linktoUnitMasterId
			,linktoDepartmentMasterId
			,IsMaintainStock
			,OpeningStock
			,InHand
			,MinimumStock
			,MaximumStock
			,CreateDateTime
			,linktoUserMasterIdCreatedBy  
		)
		VALUES
		(
			@linktoItemMasterId
			,@linktoUnitMasterId
			,@linktoDepartmentMasterId
			,@IsMaintainStock
			,@OpeningStock
			,@InHand
			,@MinimumStock
			,@MaximumStock
			,@CreateDateTime
			,@linktoUserMasterIdCreatedBy  
		) 

		SET @ItemStockTranId = @@IDENTITY
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END
			
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemStockTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemStockTran_Select]
	 @ItemStockTranId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posItemStockTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterId) AS Unit
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department
	FROM
		 posItemStockTran
	WHERE
		ItemStockTranId = @ItemStockTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemStockTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemStockTran_SelectAll 43
CREATE PROCEDURE [dbo].[posItemStockTran_SelectAll]
@linktoItemMasterId int
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		IST.*
		,IM.ItemName  AS Item		
		,UM.UnitName AS Unit		
		,DM.DepartmentName AS Department
	FROM
		 posItemStockTran IST,posItemMaster IM,posUnitMaster UM,posDepartmentMaster DM 
	
	WHERE IST.linktoItemMasterId = @linktoItemMasterId AND IST.linktoItemMasterId = IM.ItemMasterId
	AND IST.linktoUnitMasterId = UM.UnitMasterId AND IST.linktoDepartmentMasterId = DM.DepartmentMasterId
	AND IM.IsDeleted = 0
	AND UM.IsDeleted = 0
	AND DM.IsDeleted = 0
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemStockTranCategoryWiseReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posItemStockTranCategoryWiseReport_SelectAll 0
CREATE PROCEDURE [dbo].[posItemStockTranCategoryWiseReport_SelectAll] 
	@CategoryMasterIDs varchar(100)
	,@linktoBusinessMasterId int
AS
BEGIN
IF @CategoryMasterIDs = '0'
BEGIN
	SELECT 
	CM.CategoryName,IM.ItemName,IST.InHand,IST.MinimumStock,IST.MaximumStock,
	(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId=IM.linktoUnitMasterId) as Unit
	,CASE WHEN 
		IST.InHand < IST.MinimumStock 
	THEN 'Below Min' 
	ELSE 
	CASE WHEN 
		IST.InHand > IST.MaximumStock 
	THEN 'Above Max' 
	ELSE 
	CASE WHEN	
		(IST.InHand >= IST.MinimumStock AND IST.InHand <= IST.MaximumStock)
	THEN 'Normal'
		
		END END END As Reorder
	FROM 
		posItemStockTran IST,posItemMaster IM, posCategoryMaster CM
	WHERE 
		IST.linktoItemMasterId = IM.ItemMasterId
		AND IM.linktoCategoryMasterId = CM.CategoryMasterId 
		AND IM.IsDeleted=0
		AND IM.linktoBusinessMasterId=@linktoBusinessMasterId
END
ELSE
BEGIN
	SELECT 
	CM.CategoryName,IM.ItemName,IST.InHand,IST.MinimumStock,IST.MaximumStock,
	(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId=IM.linktoUnitMasterId) as Unit
	,CASE WHEN 
		IST.InHand < IST.MinimumStock 
	THEN 'Below Min' 
	ELSE 
	CASE WHEN 
		IST.InHand > IST.MaximumStock 
	THEN 'Above Max' 
	ELSE 
	CASE WHEN	
		(IST.InHand >= IST.MinimumStock AND IST.InHand <= IST.MaximumStock)
	THEN 'Normal'
		
		END END END As Reorder
	FROM 
		posItemStockTran IST,posItemMaster IM, posCategoryMaster CM
	WHERE 
		IST.linktoItemMasterId = IM.ItemMasterId
		AND IM.linktoCategoryMasterId = CM.CategoryMasterId 
		AND IM.linktoCategoryMasterId IN (SELECT ParseValue FROM dbo.Parse(@CategoryMasterIDs,','))
		AND IM.IsDeleted=0
		AND IM.linktoBusinessMasterId=@linktoBusinessMasterId
END
	--CM.CategoryMasterId IN (CASE WHEN 
	--	@CategoryMasterIDs='0'
	--THEN CM.CategoryMasterId 
	--ELSE
	-- (SELECT ParseValue FROM dbo.Parse(@CategoryMasterIDs,','))
	--END)
	
END



GO
/****** Object:  StoredProcedure [dbo].[posItemSuggested_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- [dbo].[posItemSuggested_SelectAll] 8
CREATE PROCEDURE [dbo].[posItemSuggested_SelectAll]
	 @linktoBusinessMasterId smallint
	,@linktoItemmasterId int
	,@RateIndex smallint
	,@IsDineInOnly bit = NULL
	
AS
BEGIN

	SELECT

	*
	,STUFF((SELECT distinct ',' + CAST(IOT.linktoOptionValueTranId As varchar(50))
					FROM posItemOptionTran IOT Join posOptionValueTran OVT on OVT.OptionValueTranId = IOT.linktoOptionValueTranId
							and IOT.linktoItemMasterId = IM.ItemMasterId 
							WHERE IOT.linktoItemMasterId = IM.ItemMasterId and OVT.IsDeleted = 0 and OVT.linktoBusinessMasterId = @linktoBusinessMasterId
						FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'),1,1,'') OptionValueTranIds
	,STUFF((SELECT distinct ',' + CAST(IMT.linktoItemMasterModifierId As varchar(50))
		FROM posItemModifierTran IMT
		WHERE IMT.linktoItemMasterId = IM.ItemMasterId
		FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'),1,1,'') ItemModifierMasterIds
	,@RateIndex as RateIndex
	,(SELECT MRP from posItemRateTran where linktoItemMasterId=IM.ItemMasterId) As MRP
	,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = IM.linktoUnitMasterId) AS Unit			
	,(SELECT CategoryName from posCategoryMaster where CategoryMasterId = IM.linktoCategoryMasterId) AS CategoryName			
	,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business	
	,Convert(varchar,IRT.Tax1) + ','+ Convert(varchar,IRT.Tax2)  + ','+Convert(varchar,IRT.Tax3)  + ','+ Convert(varchar,IRT.Tax4)  + ','+ Convert(varchar,IRT.Tax5) as Tax
			,CASE WHEN @RateIndex = 1
			       THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate1 - ((IRT.Rate1 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate1 END ELSE 
				CASE WHEN @RateIndex = 2 
				    THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate2 - ((IRT.Rate2 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)  ELSE IRT.Rate2 END ELSE 
				  CASE WHEN @RateIndex = 3 
				     THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate3 - ((IRT.Rate3 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate3 END ELSE 
				    CASE WHEN @RateIndex = 4 
					    THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN IRT.Rate4 - ((IRT.Rate4 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate4 END ELSE 
					  CASE WHEN @RateIndex = 5 
					      THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN  IRT.Rate5 - ((IRT.Rate5 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.Rate5 END ELSE
			           CASE WHEN @RateIndex = 0 
					        THEN CASE WHEN IRT.IsRateTaxInclusive = 1 THEN  IRT.MRP - ((IRT.MRP * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE IRT.MRP END
					     END END END END END END as Price
			,CASE WHEN (@RateIndex = 1 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate1 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
				CASE WHEN (@RateIndex = 2 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate2 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)ELSE 
				  CASE WHEN (@RateIndex = 3 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate3 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
				    CASE WHEN (@RateIndex = 4 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate4 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
					  CASE WHEN (@RateIndex = 5 and IRT.IsRateTaxInclusive = 1) THEN ((IRT.Rate5 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100) ELSE 
					    CASE WHEN (@RateIndex = 0 and IRT.IsRateTaxInclusive = 1)  THEN ((IRT.MRP * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)
						     END END END END END END as TaxRate		
FROM
	posItemSuggestedTran IST
	JOIN posItemMaster IM ON IST.linktoItemMasterIdSuggested = IM.ItemMasterId
	JOIN posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
WHERE
	IST.linktoItemmasterId = @linktoItemmasterId
	AND IM.IsDineInOnly = ISNULL(@IsDineInOnly,IM.IsDineInOnly)
	AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posItemSuggestedTran_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemSuggestedTran_DeleteAll "72"
CREATE PROCEDURE [dbo].[posItemSuggestedTran_DeleteAll]
	 @ItemMasterIDs varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		 posItemSuggestedTran
	WHERE
		linktoItemMasterId IN (SELECT * from dbo.Parse(@ItemMasterIDs, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemSuggestedTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemSuggestedTran_Insert] 
	@linktoItemMasterId int
	,@SuggestedItemMasterIDs varchar(100)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
		FROM	
		posItemSuggestedTran 
		WHERE 
		linktoItemMasterId = @linktoItemMasterId 

	INSERT INTO posItemSuggestedTran
	(
		 linktoItemMasterId
		,linktoItemMasterIdSuggested
	)
	 SELECT  @linktoItemMasterId, parseValue FROM  dbo.Parse(@SuggestedItemMasterIDs,',')  

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN	
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemSuggestedTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemSuggestedTran_Select]
	 @ItemSuggestedTranId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posItemSuggestedTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterIdSuggested) AS ItemSuggested
	FROM
		 posItemSuggestedTran
	WHERE
		ItemSuggestedTranId = @ItemSuggestedTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemSuggestionTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posItemSuggestionTran_SelectAll 2
CREATE PROCEDURE [dbo].[posItemSuggestionTran_SelectAll]
		@ItemMasterId smallint = NULL,
		@linktoBusinessMasterId smallint 
AS
BEGIN 
	SET NOCOUNT ON; 
	  SELECT DISTINCT ItemName,ItemMasterId,ItemCode,CategoryName,ItemSuggestedTranId,linktoItemMasterIdSuggested,linktoItemMasterId
			FROM
			(		
				SELECT DISTINCT
						IM.ItemName,IM.ItemMasterId
						,IM.ItemCode 
						,CM.CategoryName
						,IST.ItemSuggestedTranId
						,IST.linktoItemMasterIdSuggested
						,IST.linktoItemMasterId
						FROM	
						posItemMaster IM 	
						JOIN 
								posItemCategoryTran ICT ON IM.ItemMasterId = ICT.linktoItemMasterId
						JOIN	
								posCategoryMaster CM ON  CM.CategoryMasterId = ICT.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
						LEFT JOIN 
								posItemSuggestedTran  IST ON IST.linktoItemMasterIdSuggested=IM.ItemMasterId  AND IST.linktoItemMasterId = @ItemMasterId
						WHERE  
							IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
							IM.ItemType = 0  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
						   
			UNION ALL
					SELECT DISTINCT 
					IM.ItemName,IM.ItemMasterId,IM.ItemCode,CM.CategoryName,IST.ItemSuggestedTranId
						,IST.linktoItemMasterIdSuggested,IST.linktoItemMasterId
					FROM	
						posItemMaster IM
					JOIN	
						posCategoryMaster CM ON  CM.CategoryMasterId = IM.linktoCategoryMasterId AND CM.IsRawMaterial = 0 
					LEFT JOIN 
						posItemSuggestedTran  IST ON IST.linktoItemMasterIdSuggested=IM.ItemMasterId AND IST.linktoItemMasterId =  @ItemMasterId
					WHERE  
						IM.IsEnabled = 1 AND IM.IsDeleted = 0 AND
						IM.ItemType = 0  AND CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND IM.linktoBusinessMasterId = @linktoBusinessMasterId
						 
			)TempTable
		ORDER BY CategoryName  
		END



GO
/****** Object:  StoredProcedure [dbo].[posItemSuggestionTranByItemMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posItemSuggestionTranByItemMasterId_SelectAll 42
CREATE PROCEDURE [dbo].[posItemSuggestionTranByItemMasterId_SelectAll]
	 @linktoItemMastreId int
	,@CounterMasterId smallint
AS
BEGIN
	SELECT
		ItemName,ShortName,ItemCode,ItemMasterId,ImageName
	FROM
		posItemMaster, posItemSuggestedTran IST, posCounterItemTran CIT
	WHERE 
		ItemMasterId = linktoItemMasterIdSuggested
		AND ItemMasterId = CIT.linktoItemMasterId
		AND IST.linktoItemMasterId = @linktoItemMastreId
		AND CIT.linktoCounterMasterId = @CounterMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posItemSuggestionTranItemNames_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--   
CREATE PROCEDURE [dbo].[posItemSuggestionTranItemNames_SelectAll] 
	@linktoBusinessMasterId smallint
AS
BEGIN 
	SET NOCOUNT ON; 
	 SELECT DISTINCT  IM1.linktoCategoryMasterId,IT.linktoItemMasterId,IM1.ItemName,
			 STUFF((SELECT DISTINCT ', ' + CONVERT(VARCHAR,IM.ItemName)  
					FROM posItemSuggestedTran ITs
					JOIN posItemMaster IM
					ON IM.ItemMasterId = ITs.linktoItemMasterIdSuggested
					WHERE ITs.linktoItemMasterId = IT.linktoItemMasterId
					
			 FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')
			,1,1,'') AS SuggestedItems
	FROM
		posItemSuggestedTran IT 
	JOIN 
		posItemMaster IM1 ON IT.linktoItemMasterId = IM1.ItemMasterId   
	WHERE 
		linktoBusinessMasterId = @linktoBusinessMasterId
		
END



GO
/****** Object:  StoredProcedure [dbo].[posItemUsageTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



CREATE PROCEDURE [dbo].[posItemUsageTran_Delete]
	 @ItemUsageTranId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posItemUsageTran
	WHERE
		ItemUsageTranId = @ItemUsageTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemUsageTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemUsageTran_Insert]
	 @ItemUsageTranId int OUTPUT
	,@linktoItemMasterId int
	,@linktoItemMasterIdUse int
	,@linktoUnitMasterIdUse smallint
	,@Quantity numeric(9, 2)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	IF EXISTS(SELECT ItemUsageTranId FROM posItemUsageTran WHERE linktoItemMasterId = @linktoItemMasterId AND linktoItemMasterIdUse = @linktoItemMasterIdUse)
	BEGIN
		SELECT @ItemUsageTranId= ItemUsageTranId FROM posItemUsageTran WHERE linktoItemMasterId = @linktoItemMasterId AND linktoItemMasterIdUse = @linktoItemMasterIdUse
		SET @Status = -2
		RETURN
	END
	INSERT INTO posItemUsageTran
	(
		 linktoItemMasterId
		,linktoItemMasterIdUse
		,linktoUnitMasterIdUse
		,Quantity
	)
	VALUES
	(
		 @linktoItemMasterId
		,@linktoItemMasterIdUse
		,@linktoUnitMasterIdUse
		,@Quantity
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @ItemUsageTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemUsageTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemUsageTran_Select]
	 @ItemUsageTranId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posItemUsageTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterIdUse) AS ItemUse
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterIdUse) AS UnitUse
	FROM
		 posItemUsageTran
	WHERE
		ItemUsageTranId = @ItemUsageTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemUsageTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemUsageTran_SelectAll]
@linktoItemMasterId int 
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posItemUsageTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterIdUse) AS ItemUse		
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterIdUse) AS UnitUse
	FROM
		 posItemUsageTran
	WHERE 
		linktoItemMasterId = @linktoItemMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posItemUsageTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posItemUsageTran_Update]
	 @ItemUsageTranId int
	,@linktoItemMasterId int
	,@linktoItemMasterIdUse int
	,@linktoUnitMasterIdUse smallint
	,@Quantity numeric(9, 2)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posItemUsageTran
	SET
		 linktoItemMasterId = @linktoItemMasterId
		,linktoItemMasterIdUse = @linktoItemMasterIdUse
		,linktoUnitMasterIdUse = @linktoUnitMasterIdUse
		,Quantity = @Quantity
	WHERE
		ItemUsageTranId = @ItemUsageTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posJournalVoucherMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posJournalVoucherMaster_DeleteAll]
	 @JournalVoucherMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

DELETE
	FROM
		 posJournalVoucherTran
	WHERE
		linktoJournalVoucherMasterId IN (SELECT * from dbo.Parse(@JournalVoucherMasterIds, ','))


	DELETE
	FROM
		 posJournalVoucherMaster
	WHERE
		JournalVoucherMasterId IN (SELECT * from dbo.Parse(@JournalVoucherMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posJournalVoucherMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posJournalVoucherMaster_Insert]
	 @JournalVoucherMasterId int OUTPUT
	,@linktoBusinessMasterId smallint
	,@VoucherDate date
	,@VoucherNumber varchar(20)
	,@Remark varchar(500) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posJournalVoucherMaster
	(
		 linktoBusinessMasterId
		,VoucherDate
		,VoucherNumber
		,Remark
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@VoucherDate
		,@VoucherNumber
		,@Remark
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @JournalVoucherMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posJournalVoucherMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posJournalVoucherMaster_Select]
	 @JournalVoucherMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posJournalVoucherMaster.*
	FROM
		 posJournalVoucherMaster
	WHERE
		JournalVoucherMasterId = @JournalVoucherMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posJournalVoucherMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posJournalVoucherMaster_SelectAll]
	 @VoucherDate date = NULL
	,@VoucherNumber varchar(20)
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		JVM.*,JVT.*,AM.AccountName
	FROM
		 posJournalVoucherMaster JVM
		 JOIN posJournalVoucherTran JVT ON JVT.linktoJournalVoucherMasterId=JVM.JournalVoucherMasterId
		 JOIN posAccountMaster AM ON AM.AccountMasterId=JVT.linktoAccountMasterId 
	WHERE
		CONVERT(varchar(8), VoucherDate, 112) = CONVERT(varchar(8), @VoucherDate, 112)
		AND VoucherNumber LIKE @VoucherNumber + '%' AND JVM.linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY JournalVoucherMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posJournalVoucherMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posJournalVoucherMaster_Update]
	 @JournalVoucherMasterId int
	,@linktoBusinessMasterId smallint
	,@VoucherDate date
	,@VoucherNumber varchar(20)
	,@Remark varchar(500) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posJournalVoucherMaster
	SET
		 linktoBusinessMasterId = @linktoBusinessMasterId
		,VoucherDate = @VoucherDate
		,VoucherNumber = @VoucherNumber
		,Remark = @Remark
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		JournalVoucherMasterId = @JournalVoucherMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posJournalVoucherTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posJournalVoucherTran_Insert]
	 @JournalVoucherTranId int OUTPUT
	,@linktoJournalVoucherMasterId int
	,@linktoAccountMasterId int
	,@Debit money = NULL
	,@Credit money = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posJournalVoucherTran
	(
		 linktoJournalVoucherMasterId
		,linktoAccountMasterId
		,Debit
		,Credit
	)
	VALUES
	(
		 @linktoJournalVoucherMasterId
		,@linktoAccountMasterId
		,@Debit
		,@Credit
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @JournalVoucherTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posJournalVoucherTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posJournalVoucherTran_SelectAll]
    @linktoJournalVoucherMasterId int
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posJournalVoucherTran.*
		,(SELECT AccountName FROM posAccountMaster WHERE AccountMasterId = linktoAccountMasterId) AS Account
	FROM
		 posJournalVoucherTran
	WHERE
		linktoJournalVoucherMasterId = @linktoJournalVoucherMasterId
	
	ORDER BY JournalVoucherTranId

	RETURN
END

GO
/****** Object:  StoredProcedure [dbo].[posJournalVoucherTranByJournalVoucherMasterId_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posJournalVoucherTranByJournalVoucherMasterId_Delete]
	 @linktoJournalVoucherMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posJournalVoucherTran
	WHERE
		linktoJournalVoucherMasterId = @linktoJournalVoucherMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMembershipType_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMembershipType_DeleteAll]
	 @MembershipTypeIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRawCount int
	set @TotalRecords = (SELECT count(*) FROM dbo.Parse(@MembershipTypeIds, ','))

	DELETE
	FROM
		 posMembershipType
	WHERE
		MembershipTypeMasterId IN (SELECT * from dbo.Parse(@MembershipTypeIds, ','))
		AND MembershipTypeMasterId NOT IN
		(
			SELECT linktoMembershipTypeMasterId FROM posCustomerMaster WHERE IsEnabled = 1 AND IsDeleted = 0
			AND linktoMembershipTypeMasterId IN (SELECT * from dbo.Parse(@MembershipTypeIds, ','))
		)

	SET @UpdatedRawCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
			IF @TotalRecords = @UpdatedRawCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		end
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMembershipType_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMembershipType_Insert]
	 @MembershipTypeMasterId smallint OUTPUT
	,@MembershipType varchar(50)
	,@Prefix varchar(10)
	,@DisplayFormat varchar(50)
	,@TotalDigits smallint
	,@PricePerPoint money
	,@IsCardOfPoint bit
	,@NewCardRate money = NULL
	,@NewCardBonusPoints smallint = NULL
	,@NewCardBonusAmount money = NULL
	,@CardRenewalRate money = NULL
	,@CardRenewalBonusPoints smallint = NULL
	,@CardRenewalBonusAmount money = NULL
	,@ValidMonths smallint = NULL
	,@MinimumPointsForDeduction smallint
	,@TermsAndConditions varchar(500) = NULL
	,@IsEnabled bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy int
	,@linktoBusinessMasterId smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT MembershipTypeMasterId FROM posMembershipType WHERE MembershipType = @MembershipType AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @MembershipTypeMasterId = MembershipTypeMasterId FROM posMembershipType WHERE MembershipType = @MembershipType AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posMembershipType
	(
		 MembershipType
		,Prefix
		,DisplayFormat
		,TotalDigits
		,PricePerPoint
		,IsCardOfPoint
		,NewCardRate
		,NewCardBonusPoints
		,NewCardBonusAmount
		,CardRenewalRate
		,CardRenewalBonusPoints
		,CardRenewalBonusAmount
		,ValidMonths
		,MinimumPointsForDeduction
		,TermsAndConditions
		,IsEnabled
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoBusinessMasterId
		
	)
	VALUES
	(
		 @MembershipType
		,@Prefix
		,@DisplayFormat
		,@TotalDigits
		,@PricePerPoint
		,@IsCardOfPoint
		,@NewCardRate
		,@NewCardBonusPoints
		,@NewCardBonusAmount
		,@CardRenewalRate
		,@CardRenewalBonusPoints
		,@CardRenewalBonusAmount
		,@ValidMonths
		,@MinimumPointsForDeduction
		,@TermsAndConditions
		,@IsEnabled
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoBusinessMasterId
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @MembershipTypeMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMembershipType_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMembershipType_Select]
	 @MembershipTypeMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posMembershipType.*
	FROM
		 posMembershipType
	WHERE
		MembershipTypeMasterId = @MembershipTypeMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMembershipType_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMembershipType_SelectAll]	 
	@IsEnabled bit = NULL,
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posMembershipType.*
	FROM
		 posMembershipType
	WHERE
	 
	 IsEnabled = ISNULL(@IsEnabled, IsEnabled) AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY MembershipType

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMembershipType_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMembershipType_Update]
	 @MembershipTypeMasterId smallint
	,@MembershipType varchar(50)
	,@Prefix varchar(10)
	,@DisplayFormat varchar(50)
	,@TotalDigits smallint
	,@PricePerPoint money
	,@IsCardOfPoint bit
	,@NewCardRate money = NULL
	,@NewCardBonusPoints smallint = NULL
	,@NewCardBonusAmount money = NULL
	,@CardRenewalRate money = NULL
	,@CardRenewalBonusPoints smallint = NULL
	,@CardRenewalBonusAmount money = NULL
	,@ValidMonths smallint = NULL
	,@MinimumPointsForDeduction smallint
	,@TermsAndConditions varchar(500) = NULL
	,@IsEnabled bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy int = NULL
	,@linktoBusinessMasterId smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT MembershipTypeMasterId FROM posMembershipType WHERE MembershipType = @MembershipType AND MembershipTypeMasterId != @MembershipTypeMasterId
				AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posMembershipType
	SET
		 MembershipType = @MembershipType
		,Prefix = @Prefix
		,DisplayFormat = @DisplayFormat
		,TotalDigits = @TotalDigits
		,PricePerPoint = @PricePerPoint
		,IsCardOfPoint=@IsCardOfPoint
		,NewCardRate = @NewCardRate
		,NewCardBonusPoints = @NewCardBonusPoints
		,NewCardBonusAmount = @NewCardBonusAmount
		,CardRenewalRate = @CardRenewalRate
		,CardRenewalBonusPoints = @CardRenewalBonusPoints
		,CardRenewalBonusAmount = @CardRenewalBonusAmount
		,ValidMonths = @ValidMonths
		,MinimumPointsForDeduction = @MinimumPointsForDeduction
		,TermsAndConditions = @TermsAndConditions
		,IsEnabled = @IsEnabled
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoBusinessMasterId = @linktoBusinessMasterId
		
	WHERE
		MembershipTypeMasterId = @MembershipTypeMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMembershipTypeMembershipType_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMembershipTypeMembershipType_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 MembershipTypeMasterId
		,MembershipType
	FROM
		 posMembershipType
	WHERE
		IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY MembershipType

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMenuMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create PROCEDURE [dbo].[posMenuMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		posMenuMaster.*
	FROM 
		posMenuMaster
	ORDER BY 
		SortOrder

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posMenuMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[posMenuMaster_Update]
	 @MenuMasterId smallint
	,@IsShowInQuickLaunch char(1) = null
	,@IsEnabled bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT MenuMasterId FROM posMenuMaster WHERE IsShowInQuickLaunch = @IsShowInQuickLaunch)
	BEGIN
		SET @Status = -2
		RETURN
	END

	UPDATE posMenuMaster
	SET
		IsShowInQuickLaunch = @IsShowInQuickLaunch
		,IsEnabled = @IsEnabled
	WHERE
		MenuMasterId = @MenuMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posMenuMaster_UpdateAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[posMenuMaster_UpdateAll]
	@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posMenuMaster
	SET
		ShortcutKey = [DefaultShortcutKey]

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posMenuMasterShortcutKey_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[posMenuMasterShortcutKey_Update]
	 @MenuMasterId smallint
	,@ShortcutKey varchar(15) = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT MenuMasterId FROM posMenuMaster WHERE ShortcutKey = @ShortcutKey)
	BEGIN
		SELECT @MenuMasterId = MenuMasterId FROM posMenuMaster WHERE ShortcutKey = @ShortcutKey
		SET @Status = -2
		RETURN
	END

	UPDATE posMenuMaster
	SET
		ShortcutKey = @ShortcutKey
	WHERE
		MenuMasterId = @MenuMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseCategoryMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseCategoryMaster_DeleteAll]
	 @MiscExpenseCategoryMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
		Declare @TotalRecords int
		DECLARE @RowCount int
		set @TotalRecords=(SELECT COUNT(*) FROM dbo.Parse(@MiscExpenseCategoryMasterIds, ','))
	DELETE
	FROM
		 posMiscExpenseCategoryMaster
	WHERE
		MiscExpenseCategoryMasterId IN (SELECT * from dbo.Parse(@MiscExpenseCategoryMasterIds, ','))
		AND MiscExpenseCategoryMasterId NOT IN 
		(
			SELECT linktoMiscExpenseCategoryMasterId FROM posMiscExpenseMaster WHERE   IsEnabled = 1 
			AND linktoMiscExpenseCategoryMasterId IN (SELECT * FROM dbo.Parse(@MiscExpenseCategoryMasterIds, ','))
		)

		SET @RowCount=@@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
	
			if @TotalRecords = @RowCount
				BEGIN
					SET @Status = 0
				END
				ELSE
				BEGIN
					SET @Status = -2
				END
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseCategoryMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseCategoryMaster_Insert]
	 @MiscExpenseCategoryMasterId smallint OUTPUT
	,@CategoryName varchar(50) = NULL
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT MiscExpenseCategoryMasterId FROM posMiscExpenseCategoryMaster WHERE CategoryName = @CategoryName AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @MiscExpenseCategoryMasterId = MiscExpenseCategoryMasterId FROM posMiscExpenseCategoryMaster WHERE CategoryName = @CategoryName 
				AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posMiscExpenseCategoryMaster
	(
		 CategoryName
		,Description
		,IsEnabled
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @CategoryName
		,@Description
		,@IsEnabled
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @MiscExpenseCategoryMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseCategoryMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseCategoryMaster_Select]
	 @MiscExpenseCategoryMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posMiscExpenseCategoryMaster.*
	FROM
		 posMiscExpenseCategoryMaster
	WHERE
		MiscExpenseCategoryMasterId = @MiscExpenseCategoryMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseCategoryMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseCategoryMaster_SelectAll]
	 @IsEnabled bit = NULL,
	 @linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posMiscExpenseCategoryMaster.*
	FROM
		 posMiscExpenseCategoryMaster
	WHERE
		((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseCategoryMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseCategoryMaster_Update]
	 @MiscExpenseCategoryMasterId smallint
	,@CategoryName varchar(50) = NULL
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT MiscExpenseCategoryMasterId FROM posMiscExpenseCategoryMaster WHERE CategoryName = @CategoryName AND MiscExpenseCategoryMasterId != @MiscExpenseCategoryMasterId
					AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posMiscExpenseCategoryMaster
	SET
		 CategoryName = @CategoryName
		,Description = @Description
		,IsEnabled = @IsEnabled
	WHERE
		MiscExpenseCategoryMasterId = @MiscExpenseCategoryMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseCategoryMasterCategoryName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseCategoryMasterCategoryName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 MiscExpenseCategoryMasterId
		,CategoryName
	FROM
		 posMiscExpenseCategoryMaster
	WHERE
		IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY CategoryName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseMaster_DeleteAll]
	 @MiscExpenseMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE	
		 posMiscExpenseMaster
	SET		
		IsDeleted=1
	WHERE
		MiscExpenseMasterId IN (SELECT * from dbo.Parse(@MiscExpenseMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseMaster_Insert]
	 @MiscExpenseMasterId int OUTPUT
	,@linktoMiscExpenseCategoryMasterId smallint
	,@PaidTo varchar(100)
	,@PaidDate date
	,@InvoiceNo varchar(20) = NULL
	,@linktoPaymentTypeMasterId smallint
	,@linktoCounterMasterId smallint
	,@Amount money
	,@Remark varchar(200) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoBusinessMasterId smallint
	,@IsDeleted bit=0
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posMiscExpenseMaster
	(
		 linktoMiscExpenseCategoryMasterId
		,PaidTo
		,PaidDate
		,InvoiceNo
		,linktoPaymentTypeMasterId
		,linktoCounterMasterId
		,Amount
		,Remark
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoBusinessMasterId
		,IsDeleted
	)
	VALUES
	(
		 @linktoMiscExpenseCategoryMasterId
		,@PaidTo
		,@PaidDate
		,@InvoiceNo
		,@linktoPaymentTypeMasterId
		,@linktoCounterMasterId
		,@Amount
		,@Remark
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoBusinessMasterId
		,@IsDeleted
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @MiscExpenseMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseMaster_Select]
	 @MiscExpenseMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posMiscExpenseMaster.*
		,(SELECT CategoryName FROM posMiscExpenseCategoryMaster WHERE MiscExpenseCategoryMasterId = linktoMiscExpenseCategoryMasterId) AS MiscExpenseCategory
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType
	FROM
		 posMiscExpenseMaster
	WHERE
		MiscExpenseMasterId = @MiscExpenseMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseMaster_SelectAll]
	 @linktoMiscExpenseCategoryMasterId smallint = NULL
	,@PaidTo varchar(100)
	,@PaidDate date = NULL
	,@PaidDateTo date = NULL
	,@InvoiceNo varchar(20)
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posMiscExpenseMaster.*
		,(SELECT CategoryName FROM posMiscExpenseCategoryMaster WHERE MiscExpenseCategoryMasterId = linktoMiscExpenseCategoryMasterId) AS MiscExpenseCategory		
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType		
	FROM
		 posMiscExpenseMaster
	WHERE
		linktoMiscExpenseCategoryMasterId = ISNULL(@linktoMiscExpenseCategoryMasterId, linktoMiscExpenseCategoryMasterId)
		AND PaidTo LIKE @PaidTo + '%'
		AND CONVERT (varchar(8),PaidDate,112) BETWEEN CONVERT(varchar(8),@PaidDate,112) AND CONVERT(varchar(8),@PaidDateTo,112)
	--	AND CONVERT(varchar(8), PaidDate, 112) = CONVERT(varchar(8), @PaidDate, 112)
		AND ISNULL(InvoiceNo,'') LIKE @InvoiceNo + '%'
		AND linktoBusinessMasterId = @linktoBusinessMasterId
		AND IsDeleted=0
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscExpenseMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscExpenseMaster_Update]
	 @MiscExpenseMasterId int
	,@linktoMiscExpenseCategoryMasterId smallint
	,@PaidTo varchar(100)
	,@PaidDate date
	,@InvoiceNo varchar(20) = NULL
	,@linktoPaymentTypeMasterId smallint
	,@linktoCounterMasterId smallint
	,@Amount money
	,@Remark varchar(200) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posMiscExpenseMaster
	SET
		 linktoMiscExpenseCategoryMasterId = @linktoMiscExpenseCategoryMasterId
		,PaidTo = @PaidTo
		,PaidDate = @PaidDate
		,InvoiceNo = @InvoiceNo
		,linktoPaymentTypeMasterId = @linktoPaymentTypeMasterId
		,linktoCounterMasterId = @linktoCounterMasterId
		,Amount = @Amount
		,Remark = @Remark
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoBusinessMasterId = @linktoBusinessMasterId
	WHERE
		MiscExpenseMasterId = @MiscExpenseMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeCategoryMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeCategoryMaster_DeleteAll]
	 @MiscIncomeCategoryMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
		Declare @TotalRecords int
		DECLARE @RowCount int
		set @TotalRecords=(SELECT COUNT(*) FROM dbo.Parse(@MiscIncomeCategoryMasterIds, ','))
	DELETE
	FROM
		 posMiscIncomeCategoryMaster
	WHERE
		MiscIncomeCategoryMasterId IN (SELECT * from dbo.Parse(@MiscIncomeCategoryMasterIds, ','))
		AND MiscIncomeCategoryMasterId NOT IN 
		(
			SELECT linktoMiscIncomeCategoryMasterId FROM posMiscIncomeMaster WHERE   IsEnabled = 1 
			AND linktoMiscIncomeCategoryMasterId IN (SELECT * FROM dbo.Parse(@MiscIncomeCategoryMasterIds, ','))
		)
		SET @RowCount=@@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		
			if @TotalRecords = @RowCount
				BEGIN
					SET @Status = 0
				END
				ELSE
				BEGIN
					SET @Status = -2
				END
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeCategoryMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeCategoryMaster_Insert]
	 @MiscIncomeCategoryMasterId smallint OUTPUT
	,@CategoryName varchar(50) = NULL
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT MiscIncomeCategoryMasterId FROM posMiscIncomeCategoryMaster WHERE CategoryName = @CategoryName AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @MiscIncomeCategoryMasterId = MiscIncomeCategoryMasterId FROM posMiscIncomeCategoryMaster WHERE CategoryName = @CategoryName
				AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posMiscIncomeCategoryMaster
	(
		 CategoryName
		,Description
		,IsEnabled
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @CategoryName
		,@Description
		,@IsEnabled
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @MiscIncomeCategoryMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeCategoryMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeCategoryMaster_Select]
	 @MiscIncomeCategoryMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posMiscIncomeCategoryMaster.*
	FROM
		 posMiscIncomeCategoryMaster
	WHERE
		MiscIncomeCategoryMasterId = @MiscIncomeCategoryMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeCategoryMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeCategoryMaster_SelectAll]
	 @IsEnabled bit = NULL,
	 @linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posMiscIncomeCategoryMaster.*
	FROM
		 posMiscIncomeCategoryMaster
	WHERE
		((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeCategoryMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeCategoryMaster_Update]
	 @MiscIncomeCategoryMasterId smallint
	,@CategoryName varchar(50) = NULL
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT MiscIncomeCategoryMasterId FROM posMiscIncomeCategoryMaster WHERE CategoryName = @CategoryName AND MiscIncomeCategoryMasterId != @MiscIncomeCategoryMasterId
					AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posMiscIncomeCategoryMaster
	SET
		 CategoryName = @CategoryName
		,Description = @Description
		,IsEnabled = @IsEnabled
	WHERE
		MiscIncomeCategoryMasterId = @MiscIncomeCategoryMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeCategoryMasterCategoryName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeCategoryMasterCategoryName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 MiscIncomeCategoryMasterId
		,CategoryName
	FROM
		 posMiscIncomeCategoryMaster
	WHERE
		IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY CategoryName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeMaster_DeleteAll]
	 @MiscIncomeMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE	
		 posMiscIncomeMaster
	SET 
		IsDeleted=1
	WHERE
		MiscIncomeMasterId IN (SELECT * from dbo.Parse(@MiscIncomeMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posMiscIncomeMaster_Insert]
	 @MiscIncomeMasterId int OUTPUT
	,@linktoMiscIncomeCategoryMasterId smallint
	,@PaidBy varchar(100)
	,@ReceiveDate date
	,@ReceiptNo varchar(20) = NULL
	,@linktoPaymentTypeMasterId smallint
	,@linktoCounterMasterId smallint
	,@Amount money
	,@Remark varchar(200) = NULL
	,@CreateDateTime datetime
	,@IsDeleted bit=0
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posMiscIncomeMaster
	(
		 linktoMiscIncomeCategoryMasterId
		,PaidBy
		,ReceiveDate
		,ReceiptNo
		,linktoPaymentTypeMasterId
		,linktoCounterMasterId
		,Amount
		,Remark
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoBusinessMasterId
		,IsDeleted
	)
	VALUES
	(
		 @linktoMiscIncomeCategoryMasterId
		,@PaidBy
		,@ReceiveDate
		,@ReceiptNo
		,@linktoPaymentTypeMasterId
		,@linktoCounterMasterId
		,@Amount
		,@Remark
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoBusinessMasterId
		,@IsDeleted
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @MiscIncomeMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeMaster_Select]
	 @MiscIncomeMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posMiscIncomeMaster.*
		,(SELECT CategoryName FROM posMiscIncomeCategoryMaster WHERE MiscIncomeCategoryMasterId = linktoMiscIncomeCategoryMasterId) AS MiscIncomeCategory
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType
	FROM
		 posMiscIncomeMaster
	WHERE
		MiscIncomeMasterId = @MiscIncomeMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeMaster_SelectAll]
	 @linktoMiscIncomeCategoryMasterId smallint = NULL
	,@PaidBy varchar(100)
	,@ReceiveDate date = NULL
	,@ReceivedDateTo date = NULL
	,@ReceiptNo varchar(20)
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posMiscIncomeMaster.*
		,(SELECT CategoryName FROM posMiscIncomeCategoryMaster WHERE MiscIncomeCategoryMasterId = linktoMiscIncomeCategoryMasterId) AS MiscIncomeCategory		
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType		
	FROM
		 posMiscIncomeMaster
	WHERE
		linktoMiscIncomeCategoryMasterId = ISNULL(@linktoMiscIncomeCategoryMasterId, linktoMiscIncomeCategoryMasterId)
		AND PaidBy LIKE @PaidBy + '%'
		AND CONVERT (varchar(8),ReceiveDate,112) BETWEEN CONVERT(varchar(8),@ReceiveDate,112) AND CONVERT(varchar(8),@ReceivedDateTo,112)
		--AND CONVERT(varchar(8), ReceiveDate, 112) = CONVERT(varchar(8), @ReceiveDate, 112)
		AND ISNULL(ReceiptNo,'') LIKE @ReceiptNo + '%' AND linktoBusinessMasterId = @linktoBusinessMasterId
		AND IsDeleted=0

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posMiscIncomeMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posMiscIncomeMaster_Update]
	 @MiscIncomeMasterId int
	,@linktoMiscIncomeCategoryMasterId smallint
	,@PaidBy varchar(100)
	,@ReceiveDate date
	,@ReceiptNo varchar(20) = NULL
	,@linktoPaymentTypeMasterId smallint
	,@linktoCounterMasterId smallint
	,@Amount money
	,@Remark varchar(200) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posMiscIncomeMaster
	SET
		 linktoMiscIncomeCategoryMasterId = @linktoMiscIncomeCategoryMasterId
		,PaidBy = @PaidBy
		,ReceiveDate = @ReceiveDate
		,ReceiptNo = @ReceiptNo
		,linktoPaymentTypeMasterId = @linktoPaymentTypeMasterId
		,linktoCounterMasterId = @linktoCounterMasterId
		,Amount = @Amount
		,Remark = @Remark
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoBusinessMasterId = @linktoBusinessMasterId
	WHERE
		MiscIncomeMasterId = @MiscIncomeMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferCodesTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferCodesTran_Delete]
	 @linktoOfferMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posOfferCodesTran
	WHERE
		linktoOfferMasterId = @linktoOfferMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferCodesTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferCodesTran_Insert]
	 @OfferCodesTranId bigint OUTPUT
	,@linktoOfferMasterId int
	,@OfferCode varchar(10)
	,@linktoCustomerMasterId int = NULL
	,@linktoItemMasterId int = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@RedeemDateTime datetime = NULL
	,@linktoUserMasterIdRedeemedBy int = NULL
	,@linktoSourceMasterId smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
--IF EXISTS(SELECT @OfferCodesTranId FROM posOfferCodesTran WHERE linktoOfferMasterId=@linktoOfferMasterId AND linktoRegistredUserMasterId=@linktoRegistredUserMasterId)
--	BEGIN
--		SELECT @OfferCodesTranId = OfferCodesTranId FROM posOfferCodesTran WHERE linktoOfferMasterId=@linktoOfferMasterId AND linktoRegistredUserMasterId=@linktoRegistredUserMasterId
--		SET @Status = -2
--		RETURN
--	END
	SET NOCOUNT OFF
	INSERT INTO posOfferCodesTran
	(
		 linktoOfferMasterId
		,OfferCode
		,linktoCustomerMasterId
		,linktoItemMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,RedeemDateTime
		,linktoUserMasterIdRedeemedBy
		,linktoSourceMasterId
	)
	VALUES
	(
		 @linktoOfferMasterId
		,@OfferCode
		,@linktoCustomerMasterId
		,@linktoItemMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@RedeemDateTime
		,@linktoUserMasterIdRedeemedBy
		,@linktoSourceMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OfferCodesTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferCodesTranByOfferCode_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferCodesTranByOfferCode_Select]
	 @OfferCode varchar(10)
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 OCT.OfferCode
	FROM
		 posOfferCodesTran OCT
	WHERE
		OCT.OfferCode=@OfferCode

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferDaysTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferDaysTran_Delete]
	 @linktoOfferMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posOfferDaysTran
	WHERE
		linktoOfferMasterId = @linktoOfferMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferDaysTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferDaysTran_Insert]
	 @OfferDaysTranId int OUTPUT
	,@linktoOfferMasterId int
	,@Day smallint
	,@FromTime time
	,@ToTime time
	,@IsEnabled bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posOfferDaysTran
	(
		 linktoOfferMasterId
		,Day
		,FromTime
		,ToTime
		,IsEnabled
	)
	VALUES
	(
		 @linktoOfferMasterId
		,@Day
		,@FromTime
		,@ToTime
		,@IsEnabled
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OfferDaysTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferDaysTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- [posOfferDaysTran_SelectAll] 1
CREATE PROCEDURE [dbo].[posOfferDaysTran_SelectAll]

	@linktoOfferMasterId smallint
	--,@IsEnabled bit = NULL
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posOfferDaysTran.*
		,(SELECT OfferTitle FROM posOfferMaster WHERE OfferMasterId = linktoOfferMasterId) AS Offer
	FROM
		 posOfferDaysTran
	WHERE
	linktoOfferMasterId=@linktoOfferMasterId
--	AND
	--	IsEnabled = ISNULL(@IsEnabled, IsEnabled)
	ORDER BY OfferDaysTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferDaysTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferDaysTran_Update]
	 @OfferDaysTranId int
	,@linktoOfferMasterId int
	,@Day smallint
	,@FromTime time
	,@ToTime time
	,@IsEnabled bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posOfferDaysTran
	SET
		 linktoOfferMasterId = @linktoOfferMasterId
		,Day = @Day
		,FromTime = @FromTime
		,ToTime = @ToTime
		,IsEnabled = @IsEnabled
	WHERE
		OfferDaysTranId = @OfferDaysTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferItemsTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferItemsTran_Delete]
	 @linktoOfferMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posOfferItemsTran
	WHERE
		linktoOfferMasterId = @linktoOfferMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferItemsTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferItemsTran_Insert]
	 @OfferItemsTranId int OUTPUT
	,@linktoOfferMasterId smallint
	,@linktoItemMasterId smallint = NULL
	,@OfferItemType smallint = null
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posOfferItemsTran
	(
		 linktoOfferMasterId
		,linktoItemMasterId
		,OfferItemType
	)
	VALUES
	(
		 @linktoOfferMasterId
		,@linktoItemMasterId
		,@OfferItemType
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OfferItemsTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferItemsTranByOffer_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posOfferItemsTranByOffer_SelectAll 1
CREATE PROCEDURE [dbo].[posOfferItemsTranByOffer_SelectAll]
@linktoOfferMasterId int
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 ItemName 
		,ItemCode
		,OfferItemType
		,ShortDescription
		,linktoOfferMasterId
		,linktoItemMasterId
		
	FROM
		 posOfferItemsTran , posItemMaster
    WHERE 
		linktoOfferMasterId = @linktoOfferMasterId
		AND linktoItemMasterId = ItemMasterId
	
	ORDER BY OfferItemsTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferMaster_DeleteAll]
	 @OfferMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posOfferMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		OfferMasterId IN (SELECT * from dbo.Parse(@OfferMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferMaster_Insert]
	 @OfferMasterId int OUTPUT
	,@linktoOfferTypeMasterId smallint
	,@linktoOrderTypeMasterIds varchar(50) = NULL
	,@OfferTitle varchar(50)
	,@OfferContent varchar(2000) = NULL
	,@FromDate date = NULL
	,@ToDate date = NULL
	,@FromTime time = NULL
	,@ToTime time = NULL
	,@MinimumBillAmount money = NULL
	,@Discount money
	,@IsDiscountPercentage bit
	,@RedeemCount int = NULL
	,@OfferCode varchar(50) = NULL
	,@ImagePhysicalName varchar(100) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoBusinessMasterId smallint
	,@TermsAndConditions varchar(4000) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@IsForCustomers bit
	,@BuyItemCount int
	,@GetItemCount int
	,@IsOnline bit
	,@IsForApp bit
	,@IsForAllDays bit
	,@linktoCounterMasterId smallint= null
	,@IsNotApplicableWithOtherOffers bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posOfferMaster
	(
		 linktoOfferTypeMasterId
		,linktoOrderTypeMasterIds
		,OfferTitle
		,OfferContent
		,FromDate
		,ToDate
		,FromTime
		,ToTime
		,MinimumBillAmount
		,Discount
		,IsDiscountPercentage		
		,RedeemCount
		,OfferCode
		,ImagePhysicalName
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoBusinessMasterId
		,TermsAndConditions
		,IsEnabled
		,IsDeleted
		,IsForCustomers		
		,BuyItemCount
		,GetItemCount
		,IsOnline
		,IsForApp
		,IsForAllDays
		,linktoCounterMasterId
		,IsNotApplicableWithOtherOffers
	)
	VALUES
	(
		 @linktoOfferTypeMasterId
		 ,@linktoOrderTypeMasterIds
		,@OfferTitle
		,@OfferContent
		,@FromDate
		,@ToDate
		,@FromTime
		,@ToTime
		,@MinimumBillAmount
		,@Discount
		,@IsDiscountPercentage		
		,@RedeemCount
		,@OfferCode
		,@ImagePhysicalName
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoBusinessMasterId
		,@TermsAndConditions
		,@IsEnabled
		,@IsDeleted
		,@IsForCustomers		
		,@BuyItemCount
		,@GetItemCount
		,@IsOnline
		,@IsForApp	
		,@IsForAllDays
		,@linktoCounterMasterId
		,@IsNotApplicableWithOtherOffers
	)
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OfferMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferMaster_Select]
	 @OfferMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 OM.*
		,STUFF((SELECT distinct ',' + DATENAME(DW,CAST(ODT.Day AS INT))  
				from posOfferDaysTran ODT				
				WHERE  ODT.linktoOfferMasterId = OM.OfferMasterId AND ODT.IsEnabled = 1
			FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')   
        ,1,1,'') As ValidDays
		,STUFF((SELECT distinct ',' + ItemName
					from posOfferItemsTran OIT		
					JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
					WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 1
				FOR XML PATH(''), TYPE   
				).value('.', 'VARCHAR(MAX)')   
			,1,1,'') As ValidItems	
		,STUFF((SELECT distinct ',' + ItemName
					from posOfferItemsTran OIT		
					JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
					WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 2
				FOR XML PATH(''), TYPE   
				).value('.', 'VARCHAR(MAX)')   
			,1,1,'') As ValidBuyItems
		,STUFF((SELECT distinct ',' + ItemName
					from posOfferItemsTran OIT		
					JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
					WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 3
				FOR XML PATH(''), TYPE   
				).value('.', 'VARCHAR(MAX)')   
			,1,1,'') As ValidGetItems
		,(SELECT OfferType FROM posOfferTypeMaster WHERE OfferTypeMasterId = linktoOfferTypeMasterId) AS OfferType
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posOfferMaster OM
	WHERE
		OfferMasterId = @OfferMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posOfferMaster_SelectAll 1,'20160430','20160401','',1
CREATE PROCEDURE [dbo].[posOfferMaster_SelectAll]
	 @IsEnabled bit = NULL
	 ,@ToDate date = NULL
    ,@FromDate date = NULL
	,@OfferTitle varchar(50) = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posOfferMaster.*
		,(SELECT OfferType FROM posOfferTypeMaster WHERE OfferTypeMasterId = linktoOfferTypeMasterId) AS OfferType		
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posOfferMaster
	WHERE
		IsEnabled = ISNULL(@IsEnabled, IsEnabled)
		AND IsDeleted = 0		
		AND (
					(CONVERT(varchar(8), @FromDate,112) BETWEEN CONVERT(varchar(8), FromDate,112) AND CONVERT(varchar(8), ToDate,112))
				OR	(CONVERT(varchar(8), @ToDate,112) BETWEEN CONVERT(varchar(8), FromDate,112) AND CONVERT(varchar(8),ToDate,112))
				OR	(FromDate IS NULL AND ToDate IS NULL)
			)
		AND OfferTitle LIKE @OfferTitle + '%' AND linktoBusinessMasterId = @linktoBusinessMasterId
	
	ORDER BY OfferMasterId



	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferMaster_Update]
	 @OfferMasterId int
	,@linktoOfferTypeMasterId smallint
	,@linktoOrderTypeMasterIds varchar(50) = null
	,@OfferTitle varchar(50)
	,@OfferContent varchar(2000) = NULL
	,@FromDate date = NULL
	,@ToDate date = NULL
	,@FromTime time = NULL
	,@ToTime time = NULL
	,@MinimumBillAmount money = NULL
	,@Discount money
	,@IsDiscountPercentage bit
	,@OfferCode varchar(50) = NULL
	,@ImagePhysicalName varchar(100) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoBusinessMasterId smallint
	,@TermsAndConditions varchar(4000) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@IsForCustomers bit
	,@BuyItemCount int
	,@GetItemCount int
	,@IsOnline bit
	,@IsForApp bit
	,@IsForAllDays bit
	,@linktoCounterMasterId smallint= null
	,@IsNotApplicableWithOtherOffers bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posOfferMaster
	SET
		 linktoOfferTypeMasterId = @linktoOfferTypeMasterId
		 ,linktoOrderTypeMasterIds=@linktoOrderTypeMasterIds
		,OfferTitle = @OfferTitle
		,OfferContent = @OfferContent
		,FromDate = @FromDate
		,ToDate = @ToDate
		,FromTime = @FromTime
		,ToTime = @ToTime
		,MinimumBillAmount = @MinimumBillAmount
		,Discount = @Discount
		,IsDiscountPercentage = @IsDiscountPercentage		
		,OfferCode = @OfferCode
		,ImagePhysicalName = @ImagePhysicalName
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,TermsAndConditions = @TermsAndConditions
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,IsForCustomers = @IsForCustomers
		,BuyItemCount=@BuyItemCount
		,GetItemCount=@GetItemCount
		,IsOnline = @IsOnline
		,IsForApp=@IsForApp
		,IsForAllDays=@IsForAllDays
		,linktoCounterMasterId=@linktoCounterMasterId
		,IsNotApplicableWithOtherOffers = @IsNotApplicableWithOtherOffers
	WHERE
		OfferMasterId = @OfferMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferMasterByFromDate_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferMasterByFromDate_SelectAll]
    @FromDate datetime,
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posOfferMaster.*
		,(SELECT OfferType From posOfferTypeMaster Where OfferTypeMasterId = linktoOfferTypeMasterId)As OfferType
		,(SELECT BusinessName From posBusinessMaster Where BusinessMasterId = linktoBusinessMasterId)As Business
	FROM
		 posOfferMaster
	WHERE
		(((FromDate IS NULL AND ToDate IS NULL AND FromTime IS NULL AND ToTime IS NULL) OR (FromDate IS NULL AND ToDate IS NULL AND FromTime IS NOT NULL AND ToTime IS NOT NULL))
			OR
			(@FromDate BETWEEN CAST(FromDate AS datetime) AND CAST(ToDate AS datetime) AND (FromTime IS NULL and ToTime IS NULL) AND (FromDate IS NOT NULL and ToDate IS NOT NULL))
			OR 
			(@FromDate BETWEEN CAST(FromDate AS datetime) + CAST(FromTime AS datetime) AND CAST(ToDate AS datetime) + CAST(ToTime AS datetime) AND (FromTime IS NOT NULL and ToTime IS NOT NULL) AND (FromDate IS NOT NULL and ToDate IS NOT NULL)))
		AND  IsEnabled = 1
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferMasterForKOT_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferMasterForKOT_Select]
	@OfferMasterId int
AS
BEGIN
	SELECT
		 OM.*
		,STUFF((SELECT distinct ',' + DATENAME(DW,CAST(ODT.Day AS INT))  
				from posOfferDaysTran ODT				
				WHERE  ODT.linktoOfferMasterId = OM.OfferMasterId AND ODT.IsEnabled = 1
			FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')   
        ,1,1,'') As ValidDays
		,STUFF((SELECT distinct ',' + CONVERT(varchar, ItemMasterId)
					from posOfferItemsTran OIT		
					JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
					WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 1
				FOR XML PATH(''), TYPE   
				).value('.', 'VARCHAR(MAX)')   
			,1,1,'') As ValidItems	
		,STUFF((SELECT distinct ',' + CONVERT(varchar, ItemMasterId)
					from posOfferItemsTran OIT		
					JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
					WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 2
				FOR XML PATH(''), TYPE   
				).value('.', 'VARCHAR(MAX)')   
			,1,1,'') As ValidBuyItems
		,STUFF((SELECT distinct ',' + CONVERT(varchar, ItemMasterId)
					from posOfferItemsTran OIT		
					JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
					WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 3
				FOR XML PATH(''), TYPE   
				).value('.', 'VARCHAR(MAX)')   
			,1,1,'') As ValidGetItems
		,(SELECT OfferType FROM posOfferTypeMaster WHERE OfferTypeMasterId = linktoOfferTypeMasterId) AS OfferType
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
	FROM
		 posOfferMaster OM
	WHERE
		OfferMasterId = @OfferMasterId
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferMasterOfferCodeUniqueness_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOfferMasterOfferCodeUniqueness_Select] 
		@OfferCode varchar(5)
		,@Status smallint OUTPUT 
AS
BEGIN 
	SET NOCOUNT on;
	--IF EXISTS(SELECT OfferMasterId FROM posOfferMaster WHERE OfferCode = @OfferCode AND IsDeleted = 0 AND IsEnabled = 1 AND CONVERT (VARCHAR(8),ToDate,112)  <  CONVERT (VARCHAR(8),GETDATE(),112) )
	IF EXISTS(SELECT OfferMasterId FROM posOfferMaster WHERE OfferCode = @OfferCode AND IsDeleted = 0 AND IsEnabled = 1)
	BEGIN 
		SET @Status = -2
		RETURN
	END 
	ELSE
		BEGIN 
		IF EXISTS(SELECT OfferCodesTranId FROM posOfferCodesTran WHERE OfferCode = @OfferCode)
		BEGIN
			SET @Status = -2
			RETURN
		END
		ELSE
		BEGIN
			SET @Status = 0
		RETURN
	END 


	END
	
	--ELSE IF EXISTS(SELECT OfferCodesTranId FROM posOfferCodesTran WHERE OfferCode = @OfferCode)
	--BEGIN
	--	SET @Status = -2
	--	RETURN
	--END
	--ELSE
	--BEGIN
	--	SET @Status = 0
	--	RETURN
	--END 
	END 
 
 



GO
/****** Object:  StoredProcedure [dbo].[posOfferMasterOfferCodeVerification_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferMasterOfferCodeVerification_Select] 
	-- Add the parameters for the stored procedure here
	@OfferCode varchar(50),
	@MinimumBillAmount money,
	@linktoBusinessMasterId smallint,
	@linktoCustomerMasterId int,
	@linktoOrderTypeMasterId smallint,
	@CurrentDate datetime
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	SELECT 
		OfferMasterId,linktoOfferTypeMasterId,Discount,IsDiscountPercentage,BuyItemCount,GetItemCount 
	FROM
	(
		SELECT DISTINCT
			OM.*,ODT.Day OfferDay,OCT.linktoCustomerMasterId
		FROM 
			posOfferMaster OM
		LEFT JOIN 
			posOfferCodesTran OCT ON OCT.linktoOfferMasterId = OM.OfferMasterId
		LEFT JOIN
			posOfferDaysTran ODT ON ODT.linktoOfferMasterId = OM.OfferMasterId AND ODT.IsEnabled = 1
		WHERE 
			(OCT.OfferCode = @OfferCode OR OM.OfferCode = @OfferCode)
			AND (
					(Convert(varchar(8),@CurrentDate,112) BETWEEN Convert(varchar(8),FromDate,112) AND Convert(varchar(8),ToDate,112))
				OR
					(FromDate IS NULL AND ToDate IS NULL)
				)
			AND (
					(Convert(varchar(8),@CurrentDate,108) BETWEEN Convert(varchar(8),OM.FromTime,108) AND Convert(varchar(8),OM.ToTime,108))
				OR
					(OM.FromTime IS NULL AND OM.ToTime IS NULL)
				)
			AND (MinimumBillAmount = @MinimumBillAmount OR MinimumBillAmount IS NULL)
			AND linktoBusinessMasterId = @linktoBusinessMasterId		
			AND (linktoOrderTypeMasterIds IS NULL OR ',' + linktoOrderTypeMasterIds + ',' LIKE '%,' + Convert(varchar(50),@linktoOrderTypeMasterId) + ',%')		
	)TempTable
	WHERE 
		(OfferDay IS NULL OR OfferDay = (DATEPART(WEEKDAY,@CurrentDate) - 1))
	AND (linktoCustomerMasterId IS NULL OR linktoCustomerMasterId = @linktoCustomerMasterId)
	AND ( RedeemCount > (SELECT COUNT(*) FROM posOfferCodesTran WHERE OfferCode=@OfferCode AND linktoCustomerMasterId = @linktoCustomerMasterId))
END


GO
/****** Object:  StoredProcedure [dbo].[posOfferMasterVerifiedOfferByDate_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 
-- posOfferMasterVerifiedOfferByDate_SelectAll ''
CREATE PROCEDURE [dbo].[posOfferMasterVerifiedOfferByDate_SelectAll]
    @CurrentDate datetime,
	@linktoBusinessMasterId smallint,
	@linktoOrderTypeMasterId smallint
AS
BEGIN

SELECT DISTINCT
	OfferMasterId,OfferTitle,OfferContent,Discount,IsDiscountPercentage,MinimumBillAmount,IsNotApplicableWithOtherOffers,linktoOfferTypeMasterId,
	BuyItemCount,GetItemCount
	,STUFF((SELECT distinct ',' +CONVERT(varchar, ItemMasterId)
				from posOfferItemsTran OIT		
				JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
				WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 1
			FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As ValidItems	
	,STUFF((SELECT distinct ',' + CONVERT(varchar, ItemMasterId)
				from posOfferItemsTran OIT		
				JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
				WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 2
			FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As ValidBuyItems
	,STUFF((SELECT distinct ',' + CONVERT(varchar, ItemMasterId)
				from posOfferItemsTran OIT		
				JOIN posItemMaster IM ON IM.ItemMasterId = OIT.linktoItemMasterId		
				WHERE  OIT.linktoOfferMasterId = OM.OfferMasterId AND OIT.OfferItemType = 3
			FOR XML PATH(''), TYPE   
			).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As ValidGetItems
FROM
	posOfferMaster OM
LEFT JOIN
	posOfferDaysTran ODT ON ODT.linktoOfferMasterId = OM.OfferMasterId 
WHERE
	OM.IsEnabled = 1 AND OM.IsDeleted = 0
	AND CONVERT(VARCHAR(8),@CurrentDate,112) BETWEEN CONVERT(VARCHAR(8),ISNULL(FromDate,@CurrentDate),112) AND CONVERT(VARCHAR(8),ISNULL(ToDate,@CurrentDate),112)
	AND CONVERT(VARCHAR(8),@CurrentDate,108) BETWEEN CONVERT(VARCHAR(8),ISNULL(OM.FromTime,@CurrentDate),108) AND CONVERT(VARCHAR(8),ISNULL(OM.ToTime,@CurrentDate),108)
	AND linktoBusinessMasterId = @linktoBusinessMasterId AND ',' + linktoOrderTypeMasterIds + ',' like '%,' + CONVERT(VARCHAR,@linktoOrderTypeMasterId) + ',%'
	AND ((IsForAllDays = 1) OR (IsForAllDays = 0 AND ODT.IsEnabled = 1 AND ODT.Day = (DATEPART(WEEKDAY,@CurrentDate) - 1)))
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferTypeMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferTypeMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posOfferTypeMaster.*
	FROM
		 posOfferTypeMaster
	
	ORDER BY OfferType

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOfferTypeMasterOfferType_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOfferTypeMasterOfferType_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 OfferTypeMasterId
		,OfferType
	FROM
		 posOfferTypeMaster
	ORDER BY OfferTypeMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOptionMaster_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOptionMaster_Delete]
	 @OptionMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS (SELECT linktoOptionMasterId from posItemOptionTran IOT JOIN posOptionValueTran OVT ON OVT.OptionValueTranId = IOT.linktoOptionValueTranId 
		where IsDeleted = 0 AND linktoOptionMasterId = @OptionMasterId)
	BEGIN 
		SET @Status = -2
		RETURN
	END

	ELSE

	BEGIN

	UPDATE
		posOptionMaster
	SET
		 IsDeleted = 1
	WHERE
		OptionMasterId = @OptionMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
	END
END



GO
/****** Object:  StoredProcedure [dbo].[posOptionMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOptionMaster_Insert]
	 @OptionMasterId smallint OUTPUT
	,@OptionName varchar(20)
	,@linktoBusinessTypeMasterId smallint
	,@SortOrder smallint = NULL
	,@IsDeleted bit
	,@IsDefault bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posOptionMaster
	(
		 OptionName
		,linktoBusinessTypeMasterId
		,SortOrder
		,IsDeleted
		,IsDefault
	)
	VALUES
	(
		 @OptionName
		,@linktoBusinessTypeMasterId
		,@SortOrder
		,@IsDeleted
		,@IsDefault
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OptionMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOptionMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOptionMaster_SelectAll]
	@linktoBusinessTypeMasterId smallint,
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 OM.*,
		 STUFF((SELECT DISTINCT ', ' + CONVERT(VARCHAR,OptionValue)  
						FROM posOptionValueTran
						WHERE linktoOptionMasterId = OM.OptionMasterId AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
				 FOR XML PATH(''), TYPE   
				).value('.', 'VARCHAR(MAX)')   
				,1,1,'') AS OptionValue
	FROM
		 posOptionMaster OM
	WHERE
		IsDeleted = 0 AND linktoBusinessTypeMasterId = @linktoBusinessTypeMasterId
	ORDER BY CASE WHEN SortOrder IS NULL THEN 1 ELSE 0 END, SortOrder

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOptionMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOptionMaster_Update]
	 @OptionMasterId smallint
	,@OptionName varchar(20)
	,@linktoBusinessTypeMasterId smallint
	,@SortOrder smallint = NULL
	,@IsDeleted bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posOptionMaster
	SET
		 OptionName = @OptionName
		,linktoBusinessTypeMasterId = @linktoBusinessTypeMasterId
		,SortOrder = @SortOrder
		,IsDeleted = @IsDeleted
	WHERE
		OptionMasterId = @OptionMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOptionValueTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOptionValueTran_Delete]
	 @OptionValueTranId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posOptionValueTran
	SET
		 IsDeleted = 1
	WHERE
		OptionValueTranId = @OptionValueTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOptionValueTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOptionValueTran_Insert]
	 @OptionValueTranId int OUTPUT
	,@linktoOptionMasterId smallint
	,@linktoBusinessMasterId smallint
	,@OptionValue varchar(50)
	,@IsDeleted bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT OptionValueTranId FROM posOptionValueTran WHERE OptionValue = @OptionValue AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @OptionValueTranId = OptionValueTranId FROM posOptionValueTran WHERE  OptionValue = @OptionValue AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posOptionValueTran
	(
		 linktoOptionMasterId
		,linktoBusinessMasterId
		,OptionValue
		,IsDeleted
	)
	VALUES
	(
		 @linktoOptionMasterId
		,@linktoBusinessMasterId
		,@OptionValue
		,@IsDeleted
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OptionValueTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOptionValueTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOptionValueTran_SelectAll]
	@linktoOptionMasterId smallint,
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posOptionValueTran.*
		,(SELECT OptionMasterId FROM posOptionMaster WHERE OptionMasterId = linktoOptionMasterId) AS OptionName
	FROM
		 posOptionValueTran
	WHERE
		IsDeleted = 0 AND linktoOptionMasterId = @linktoOptionMasterId AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY OptionValueTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOptionValueTranByItemMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- [posOptionValueTranByItemMasterId_SelectAll] 16
CREATE PROCEDURE [dbo].[posOptionValueTranByItemMasterId_SelectAll]
	 @linktoItemMasterId int
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 OVT.*
		,(SELECT OptionName FROM posOptionMaster WHERE OptionMasterId = linktoOptionMasterId)As OptionName
	FROM
		 posItemOptionTran IOT 
		 Left Join posOptionValueTran OVT on OVT.OptionValueTranId = IOT.linktoOptionValueTranId
	WHERE
		linktoItemMasterId = @linktoItemMasterId
		and OVT.IsDeleted = 0
	ORDER BY OptionName,OptionValue
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemModifierTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderItemModifierTran_Delete] 
	@linktoOrderItemTranId bigint
   ,@Status smallint OUTPUT
AS
BEGIN

SET NOCOUNT OFF

	DELETE
	FROM
		posOrderItemModifierTran
	WHERE
		linktoOrderItemTranId = @linktoOrderItemTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

RETURN

END



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemModifierTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderItemModifierTran_Insert]
	 @OrderItemModifierTranId bigint OUTPUT
	,@linktoOrderItemTranId bigint
	,@linktoItemMasterId int
	,@Rate money
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posOrderItemModifierTran
	(
		 linktoOrderItemTranId
		,linktoItemMasterId
		,Rate
	)
	VALUES
	(
		 @linktoOrderItemTranId
		,@linktoItemMasterId
		,@Rate
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OrderItemModifierTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderItemTran_Delete]
	 @linktoOrderMasterId bigint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posOrderItemTran
	WHERE
		linktoOrderMasterId = @linktoOrderMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderItemTran_Insert]
	 @OrderItemTranId bigint OUTPUT
	,@linktoOrderMasterId bigint
	,@linktoItemMasterId int
	,@Quantity smallint
	,@Rate money
	,@ItemPoint smallint
	,@DeductedPoint smallint
	,@ItemRemark varchar(250) = NULL
	,@linktoOrderStatusMasterId smallint = NULL
	,@DiscountPercentage int
	,@DiscountAmount money
	,@IsRateTaxInclusive bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posOrderItemTran
	(
		 linktoOrderMasterId
		,linktoItemMasterId
		,Quantity
		,Rate
		,ItemPoint
		,DeductedPoint
		,ItemRemark
		,linktoOrderStatusMasterId
		,DiscountPercentage
		,DiscountAmount
		,IsRateTaxInclusive
	)
	VALUES
	(
		 @linktoOrderMasterId
		,@linktoItemMasterId
		,@Quantity
		,@Rate
		,@ItemPoint
		,@DeductedPoint
		,@ItemRemark
		,@linktoOrderStatusMasterId
		,@DiscountPercentage
		,@DiscountAmount
		,@IsRateTaxInclusive
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OrderItemTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posOrderItemTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderItemTran_SelectAll]
	 @linktoOrderMasterId bigint = NULL

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posOrderItemTran.*
		, (SELECT OrderNumber FROM posOrderMaster WHERE OrderMasterId = linktoOrderMasterId) AS OrderNumber	
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
		,(SELECT StatusName FROM posOrderStatusMaster WHERE OrderStatusMasterId = linktoOrderStatusMasterId) AS OrderStatus		
	FROM
		 posOrderItemTran
	WHERE
		linktoOrderMasterId = ISNULL(@linktoOrderMasterId,linktoOrderMasterId)

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemTranAndOrderMasterOrderStatus_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderItemTranAndOrderMasterOrderStatus_Update]
	 @linktoOrderMasterId bigint
	,@linktoOrderStatusMasterId smallint
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	BEGIN TRAN

	BEGIN TRY

		UPDATE posOrderItemTran
	SET
		linktoOrderStatusMasterId = @linktoOrderStatusMasterId
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		 linktoOrderMasterId = @linktoOrderMasterId
		

		IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
		 ROLLBACK TRAN
		
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

		UPDATE posOrderMaster
		SET
			 linktoOrderStatusMasterId = @linktoOrderStatusMasterId
			,UpdateDateTime = @UpdateDateTime
			,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		WHERE
			 OrderMasterId = @linktoOrderMasterId
		

		IF @@ERROR <> 0
		BEGIN
			SET @Status = -1
			 ROLLBACK TRAN
		
		END
		ELSE
		BEGIN
			SET @Status = 0
		END



END TRY
BEGIN CATCH

 
   ROLLBACK TRAN

END CATCH
COMMIT TRAN



	
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemTranByOrderMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderItemTranByOrderMasterId_SelectAll]
	 @OrderMasterIds varchar(1000)
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		DISTINCT
			sum(Quantity) Quantity,
			sum(Rate) Rate
			,linktoOrderStatusMasterId
			,STUFF((SELECT distinct ',' + CAST(OrderItemTranId As varchar(1000))
					FROM posOrderItemTran 
							where linktoItemMasterId = OIT.linktoItemMasterId and linktoOrderMasterId = OIT.linktoOrderMasterId
						FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'),1,1,'') OrderItemTranIds
		,STUFF((SELECT distinct ',' + CAST(OTMT.Rate As varchar(1000))
					FROM posOrderItemModifierTran OTMT join posOrderItemTran on OrderItemTranId = OTMT.linktoOrderItemTranId 
					where OTMT.linktoItemMasterId = OIT.linktoItemMasterId and linktoOrderMasterId = OIT.linktoOrderMasterId
						FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'),1,1,'')AS ModifierRates
		,(SELECT OrderNumber FROM posOrderMaster WHERE OrderMasterId = linktoOrderMasterId) AS OrderNumber	
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS ItemName	
		,(SELECT OrderMasterId FROM posOrderMaster WHERE OrderMasterId = linktoOrderMasterId) AS linktoOrderMasterId
		,(SELECT RateIndex FROM posOrderMaster WHERE OrderMasterId = linktoOrderMasterId) AS RateIndex
		,linktoItemMasterId		
	FROM
			posOrderItemTran OIT
	WHERE
		linktoOrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds,','))
	GROUP BY 
		linktoItemMasterId,linktoOrderMasterId,linktoOrderStatusMasterId
	ORDER BY linktoOrderStatusMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemTranbyOrderStatus_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOrderItemTranbyOrderStatus_Update]
	 @OrderItemTranIds varchar(1000) 
	,@linktoOrderStatusMasterId smallint 
	,@UpdateDateTime datetime = NULL 
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF 
	UPDATE posOrderItemTran
	SET  
		 linktoOrderStatusMasterId = @linktoOrderStatusMasterId 
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		OrderItemTranId In (SELECT * from dbo.Parse(@OrderItemTranIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemTranKOTBlock_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--DO NOT DELETE PLEASE 
CREATE PROCEDURE [dbo].[posOrderItemTranKOTBlock_SelectAll]
	
AS
BEGIN

	SET NOCOUNT ON
	 

	 SELECT OM.linktoTableMasterIds,OM.OrderNumber, OT.* 
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item	
		,(SELECT StatusName FROM posOrderStatusMaster WHERE OrderStatusMasterId = OT.linktoOrderStatusMasterId) AS OrderStatus
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId =linktoWaiterMasterId ) AS WaiterName
		,STUFF((SELECT DISTINCT ',' + TableName FROM posTableMaster 
			 WHERE convert(VARCHAR(max),TableMasterId) = 
			 CASE WHEN CharIndex(',',linktoTableMasterIds) = 0 THEN linktoTableMasterIds 
			 ELSE Substring(linktoTableMasterIds,0,CharIndex(',',linktoTableMasterIds)) end
			 FOR XML PATH(''), TYPE) .value('.', 'NVARCHAR(MAX)'),1,1,'') TableName
			
			,CASE WHEN CharIndex(',',linktoTableMasterIds) = 0 THEN linktoTableMasterIds 
			ELSE Substring(linktoTableMasterIds,0,CharIndex(',',linktoTableMasterIds)) END FirstTableMasterID
	 
	 
	 FROM 
		posOrderMaster OM  JOIN posOrderItemTran OT 
	 ON 
		OT.linktoOrderMasterId = OM.OrderMasterId
	 WHERE 
		CONVERT(Varchar(10),OM.OrderDateTime,112) =  CONVERT(Varchar(10),GETDATE(),112)
		AND OM.linktoSalesMasterId IS NULL
		AND OM.linktoOrderStatusMasterId < 2 
		

	
	RETURN
END



	-- select OM.linktoTableMasterIds, OM.OrderNumber, OT.*
	--,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item	
	--,(SELECT StatusName FROM posOrderStatusMaster WHERE OrderStatusMasterId = OT.linktoOrderStatusMasterId) AS OrderStatus
	--,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId =linktoWaiterMasterId ) AS WaiterName
	
	-- from posOrderMaster OM
	-- join posOrderItemTran OT on OT.linktoOrderMasterId = OM.OrderMasterId
	-- where OM.OrderDateTime < = GETDATE() and OM.linktoSalesMasterId is null


	--SELECT DISTINCT
	--	OT.*
	--	,(SELECT OrderNumber FROM posOrderMaster WHERE OrderMasterId = linktoOrderMasterId) AS OrderNumber	
	--	,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
	--	,(SELECT StatusName FROM posOrderStatusMaster WHERE OrderStatusMasterId = OT.linktoOrderStatusMasterId) AS OrderStatus	
	--	--,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId =linktoWaiterMasterId ) AS WaiterName
	--	--,(SELECT TableName FROM posTableMaster WHERE TableMasterId IN Select * from  dbo.Parse(OM.li) AS TableName
	--FROM
	--	 posOrderItemTran OT ,posOrderMaster OM
	--WHERE
	--	linktoOrderMasterId = linktoOrderMasterId
	--	AND OM.OrderDateTime <= GETDATE()
	--	AND OM.linktoSalesMasterId IS NULL



GO
/****** Object:  StoredProcedure [dbo].[posOrderItemTranKOTPrint_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- [posOrderItemTranKOTPrint_SelectAll] 2
CREATE PROCEDURE [dbo].[posOrderItemTranKOTPrint_SelectAll]

	@linktoOrderMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT  OIT.linktoItemMasterId,oit.Quantity,0 As ItemType,IM.ItemName,
		CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.PrinterName 
		ELSE (select PrinterName from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PrinterName	
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Copy 
		ELSE (select Copy from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END NumberOfCopy	
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Size 
		ELSE (select size from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PageSize	
	from posOrderItemTran OIT
	join posItemMaster IM ON IM.ItemMasterId=OIT.linktoItemMasterId
	join posOrderMaster OM ON OM.OrderMasterId = OIT.linktoOrderMasterId
	left join posCounterPrinterTran CPT ON CPT.linktoCounterMasterId = OM.linktoCounterMasterId AND CPT.linktoCategoryMasterId = IM.linktoCategoryMasterId AND CPT.IsReceiptPrinter = 0
	where OIT.linktoOrderMasterId=@linktoOrderMasterId 

	UNION ALL

	SELECT OIT.linktoItemMasterId,0 As Qty,1 As Modifier
		,'   + ' + IM.ItemName AS modifier,
		CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.PrinterName 
			ELSE (select PrinterName from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
			AND linktoCategoryMasterId = 0) END PrinterName	
			, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Copy 
			ELSE (select Copy from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
			AND linktoCategoryMasterId = 0) END NumberOfCopy	
			, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Size 
			ELSE (select size from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
			AND linktoCategoryMasterId = 0) END PageSize	
	FROM 
		posOrderItemModifierTran OIMT
		JOIN posOrderItemTran OIT ON OIT.OrderItemTranId=OIMT.linktoOrderItemTranId 
		JOIN posItemMaster IM ON IM.ItemMasterId=OIMT.linktoItemMasterId 
		join posOrderMaster OM ON OM.OrderMasterId = OIT.linktoOrderMasterId
		join posItemMaster ITEM ON ITEM.ItemMasterId=OIT.linktoItemMasterId
		left join posCounterPrinterTran CPT ON CPT.linktoCounterMasterId = OM.linktoCounterMasterId AND CPT.linktoCategoryMasterId = ITEM.linktoCategoryMasterId AND CPT.IsReceiptPrinter = 0
		WHERE OIT.linktoOrderMasterId=@linktoOrderMasterId

	UNION ALL

	SELECT  OIT.linktoItemMasterId,0 As Quantity,2 As ItemRemark
		,'   * ' + ISNULL(OIT.ItemRemark,''),
		CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.PrinterName 
		ELSE (select PrinterName from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PrinterName
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Copy 
		ELSE (select Copy from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END NumberOfCopy	
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Size 
		ELSE (select size from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = OM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PageSize	
	from posOrderItemTran OIT
	join posOrderMaster OM ON OM.OrderMasterId = OIT.linktoOrderMasterId
		join posItemMaster ITEM ON ITEM.ItemMasterId=OIT.linktoItemMasterId
		left join posCounterPrinterTran CPT ON CPT.linktoCounterMasterId = OM.linktoCounterMasterId AND CPT.linktoCategoryMasterId = ITEM.linktoCategoryMasterId AND CPT.IsReceiptPrinter = 0
	where OIT.linktoOrderMasterId=@linktoOrderMasterId AND oit.ItemRemark is not NULL AND OIT.ItemRemark != ''

	order by linktoItemMasterId,ItemType

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMaster_Insert]
	 @OrderMasterId bigint OUTPUT
	,@OrderNumber varchar(20) = ''
	,@OrderDateTime datetime
	,@linktoCounterMasterId smallint
	,@linktoTableMasterIds varchar(50)
	,@linktoCustomerMasterId int = NULL
	,@linktoOrderTypeMasterId smallint
	,@linktoOrderStatusMasterId smallint = NULL
	,@TotalAmount money
	,@TotalTax money
	,@DiscountPercentage money
	,@Discount money
	,@NetAmount money
	,@NoOfAdults smallint
	,@NoOfChildren smallint
	,@RateIndex smallint = NULL
	,@ExtraAmount money
	,@TotalItemPoint smallint
	,@TotalDeductedPoint smallint
	,@Remark varchar(250) = NULL
	,@linktoSalesMasterId bigint = NULL
	,@PrintCount smallint = NULL
	,@linktoSourceMasterId smallint
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoWaiterMasterId smallint = NULL
	,@linktoWaiterMasterIdCaptain smallint = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT OrderMasterId FROM posOrderMaster WHERE OrderNumber = @OrderNumber AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @OrderMasterId = OrderMasterId FROM posOrderMaster WHERE OrderNumber = @OrderNumber AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END

	IF (@OrderNumber = '')
	BEGIN
	   EXEC posOrderMasterOrderNumberAutoIncrement_Select @linktoCounterMasterId, @OrderDateTime, @OrderNumber OUTPUT
	END

	INSERT INTO posOrderMaster
	(
		 OrderNumber
		,OrderDateTime
		,linktoCounterMasterId
		,linktoTableMasterIds
		,linktoCustomerMasterId
		,linktoOrderTypeMasterId
		,linktoOrderStatusMasterId
		,TotalAmount
		,TotalTax
		,DiscountPercentage
		,Discount
		,ExtraAmount
		,TotalItemPoint
		,TotalDeductedPoint
		,Remark
		,linktoSalesMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoWaiterMasterId
		,linktoWaiterMasterIdCaptain
		,NetAmount
		,NoOfAdults
		,NoOfChildren
		,RateIndex
		,linktoBusinessMasterId
		,PrintCount
		,linktoSourceMasterId
	)
	VALUES
	(
		 @OrderNumber
		,@OrderDateTime
		,@linktoCounterMasterId
		,@linktoTableMasterIds
		,@linktoCustomerMasterId
		,@linktoOrderTypeMasterId
		,@linktoOrderStatusMasterId
		,@TotalAmount
		,@TotalTax
		,@DiscountPercentage
		,@Discount
		,@ExtraAmount
		,@TotalItemPoint
		,@TotalDeductedPoint
		,@Remark
		,@linktoSalesMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoWaiterMasterId
		,@linktoWaiterMasterIdCaptain
		,@NetAmount
		,@NoOfAdults
		,@NoOfChildren
		,@RateIndex
		,@linktoBusinessMasterId
		,@PrintCount
		,@linktoSourceMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OrderMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posOrderMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posOrderMaster_SelectAll '','','20160401','20160801',1
CREATE PROCEDURE [dbo].[posOrderMaster_SelectAll]
	@OrderNumber varchar(20)= null
	,@linktoTableMasterIds varchar(50)= null
	,@OrderDateTime date = NULL
	,@ToDateTime date = NULL	
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posOrderMaster.*
		,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS Counter				
		,(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = linktoOrderTypeMasterId) AS OrderType
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS WaiterName	
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterIdCaptain) AS CaptainName			
		,STUFF((SELECT distinct ', ' + TableName FROM posTableMaster WHERE TableMasterId IN (SELECT parseValue FROM dbo.Parse(linktoTableMasterIds,','))
			FOR XML PATH(''), TYPE).value('.', 'VARCHAR(MAX)'), 1, 1, '') As TableName
	FROM
		 posOrderMaster
	WHERE
		OrderNumber like @OrderNumber + '%'
		AND ',' + linktoTableMasterIds + ',' LIKE CASE WHEN @linktoTableMasterIds IS NULL OR @linktoTableMasterIds = '' THEN ',' + linktoTableMasterIds + ',' ELSE '%,' + @linktoTableMasterIds + ',%' END
		AND CONVERT(varchar(8), OrderDateTime, 112) BETWEEN  ISNULL(CONVERT(varchar(8), @OrderDateTime, 112),CONVERT(varchar(8), OrderDateTime, 112)) AND ISNULL(CONVERT(varchar(8), @ToDateTime, 112),CONVERT(varchar(8), OrderDateTime, 112))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
		--AND linktoSalesMasterId IS NULL
		
 		
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMaster_Update]
	 @OrderMasterId bigint
	,@OrderNumber varchar(20)
	,@OrderDateTime datetime
	,@linktoCounterMasterId smallint
	,@linktoTableMasterIds varchar(50)
	,@linktoCustomerMasterId int = NULL
	,@linktoOrderTypeMasterId smallint
	,@linktoOrderStatusMasterId smallint = NULL
	,@TotalAmount money
	,@TotalTax money
	,@DiscountPercentage money
	,@Discount money
	,@NetAmount money
	,@NoOfAdults smallint
	,@NoOfChildren smallint
	,@RateIndex smallint = NULL
	,@ExtraAmount money
	,@TotalItemPoint smallint
	,@TotalDeductedPoint smallint
	,@Remark varchar(250) = NULL	
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@linktoWaiterMasterId smallint = NULL
	,@linktoWaiterMasterIdCaptain smallint = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT OrderMasterId FROM posOrderMaster WHERE OrderNumber = @OrderNumber AND OrderMasterId != @OrderMasterId
				AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posOrderMaster
	SET
		 OrderNumber = @OrderNumber
		,OrderDateTime = @OrderDateTime		 	
		,linktoCounterMasterId = @linktoCounterMasterId
		,linktoTableMasterIds = @linktoTableMasterIds
		,linktoCustomerMasterId = @linktoCustomerMasterId
		,linktoOrderTypeMasterId = @linktoOrderTypeMasterId
		,linktoOrderStatusMasterId = @linktoOrderStatusMasterId
		,TotalAmount = @TotalAmount
		,TotalTax = @TotalTax
		,DiscountPercentage = @DiscountPercentage
		,Discount = @Discount
		,ExtraAmount = @ExtraAmount
		,TotalItemPoint = @TotalItemPoint
		,TotalDeductedPoint = @TotalDeductedPoint
		,Remark = @Remark		
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoWaiterMasterId = @linktoWaiterMasterId
		,linktoWaiterMasterIdCaptain=@linktoWaiterMasterIdCaptain
		,NetAmount = @NetAmount
		,NoOfAdults = @NoOfAdults
		,NoOfChildren = @NoOfChildren
		,RateIndex = @RateIndex
		,linktoBusinessMasterId = @linktoBusinessMasterId
	WHERE
		OrderMasterId = @OrderMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterByFromDate_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterByFromDate_SelectAll]
	 @linktoCounterMasterId smallint,
	 @FromDate Datetime,
	 @linktoBusinessMasterId smallint
AS
BEGIN
	SET NOCOUNT ON;

	SELECT
			 COUNT(OM.OrderMasterId)TotalKOT
			,OM.linktoTableMasterIds
			,SUM(TotalAmount)TotalAmount
			,SUM(TotalTax)TotalTax
			,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS Counter	
			,(SELECT
				TOP 1 OrderDateTime
				FROM posOrderMaster TOM 
				WHERE
						OM.linktoTableMasterIds = TOM.linktoTableMasterIds
						and OM.linktoCounterMasterId = 1
						and linktoSalesMasterId IS NULL
						and CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8), @FromDate, 112))AS OrderDateTime		
			,(SELECT count(*)as TotalItem FROM
			 ( 
				SELECT count(OIT.OrderItemTranId)as Item 
				FROM posOrderItemTran OIT join posOrderMaster TOM on  TOM.OrderMasterId = OIT.linktoOrderMasterId 				
				WHERE
						OM.linktoTableMasterIds = TOM.linktoTableMasterIds
						and OM.linktoCounterMasterId = 1
						and linktoSalesMasterId IS NULL
						and CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8), @FromDate, 112)
				GROUP By linktoItemMasterId)AS OrderItem
			 )AS TotalItem
			,STUFF((SELECT DISTINCT ',' + TableName FROM posTableMaster 
			 WHERE convert(VARCHAR(max),TableMasterId) = 
			 CASE WHEN CharIndex(',',linktoTableMasterIds) = 0 THEN linktoTableMasterIds 
			 ELSE Substring(linktoTableMasterIds,0,CharIndex(',',linktoTableMasterIds)) end
			 FOR XML PATH(''), TYPE) .value('.', 'NVARCHAR(MAX)'),1,1,'') TableName

			,STUFF((SELECT DISTINCT ',' + cast(linktoOrderTypeMasterId as varchar) FROM posTableMaster 
			 WHERE convert(VARCHAR(max),TableMasterId) = 
			 CASE WHEN CharIndex(',',linktoTableMasterIds) = 0 THEN linktoTableMasterIds 
			 ELSE Substring(linktoTableMasterIds,0,CharIndex(',',linktoTableMasterIds)) end
			 FOR XML PATH(''), TYPE) .value('.', 'NVARCHAR(MAX)'),1,1,'') linktoOrderTypeMasterId
		FROM 
			posOrderMaster OM
		WHERE
			OM.linktoCounterMasterId = @linktoCounterMasterId
			and linktoSalesMasterId IS NULL
			and CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8), @FromDate, 112)
			and linktoBusinessMasterId=@linktoBusinessMasterId
  		GROUP BY OM.linktoTableMasterIds,OM.linktoCounterMasterId

		Order BY OM.linktoTableMasterIds DESC

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterbyHomeDeliveryOrders_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posOrderMasterbyHomeDeliveryOrders_Update '58,66',8,'2015-12-10 18:30:05.733',1,11
CREATE PROCEDURE [dbo].[posOrderMasterbyHomeDeliveryOrders_Update]
	 @OrderMasterIds varchar(1000) 
	,@linktoOrderStatusMasterId smallint 
	,@UpdateDateTime datetime = NULL 
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoWaiterMasterId smallint 
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF 
	UPDATE posOrderMaster
	SET  
		 linktoOrderStatusMasterId = @linktoOrderStatusMasterId 
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoWaiterMasterId = @linktoWaiterMasterId 
	WHERE
		OrderMasterId In (SELECT * from dbo.Parse(@OrderMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterByOrderNumber_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterByOrderNumber_SelectAll]
	 @OrderMasterIds varchar(100)
	,@linktoBusinessMasterId smallint
AS
BEGIN
	
	SELECT 
		posOrderMaster.*
		,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS Counter				
		,(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = linktoOrderTypeMasterId) AS OrderType
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS WaiterName	
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterIdCaptain) AS CaptainName	
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktocustomerMasterId) AS Customer	
		,(SELECT CASE WHEN RateCaption IS NULL OR RateCaption = '' THEN RateName ELSE RateCaption END FROM posRateCaptionMaster RCM WHERE RCM.RateIndex = posOrderMaster.RateIndex AND RCM.linktoBusinessMasterId = @linktoBusinessMasterId) AS RateName	
		,STUFF((SELECT distinct ',' + ShortName  
			from posTableMaster
			WHERE TableMasterId in (SELECT parsevalue FROM dbo.Parse(linktoTableMasterIds,','))
        FOR XML PATH(''), TYPE   
        ).value('.', 'VARCHAR(MAX)')   
        ,1,1,'') As TableName
	FROM
		 posOrderMaster
	WHERE
		OrderMasterId IN (SELECT ParseValue FROM dbo.Parse(@OrderMasterIds,',')) AND linktoBusinessMasterId = @linktoBusinessMasterId
		
		
END




GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterByOrderStatus_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterByOrderStatus_Update]
	 @OrderMasterId bigint
	,@linktoOrderStatusMasterId smallint 
	,@UpdateDateTime datetime = NULL 
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoWaiterMasterId smallint  = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF 
	UPDATE posOrderMaster
	SET  
		 linktoOrderStatusMasterId = @linktoOrderStatusMasterId 
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoWaiterMasterId = @linktoWaiterMasterId 
	WHERE
		OrderMasterId = @OrderMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END










GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterByOrderStatusMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterByOrderStatusMasterId_SelectAll]
	 @linktoCounterMasterId smallint,
	 @linktoOrderStatusMasterId smallint = NULL,
	 @linktoTableMasterIds varchar(100) = NULL,
	 @linktoOrderTypeMasterId smallint = NULL,
	 @linktoBusinessMasterId smallint,
	 @FromDate Datetime
AS
BEGIN
	SET NOCOUNT ON;

	SELECT
			OM.*
			,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS Counter		
			,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer		
			,(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = linktoOrderTypeMasterId) AS OrderType	
			,(SELECT count(*)as TotalItem FROM
			 ( 
				SELECT count(*)as Item FROM posOrderItemTran WHERE linktoOrderMasterId = OM.OrderMasterId Group By linktoOrderMasterId,linktoItemMasterId)AS OrderItem
			 )AS TotalItem
			,STUFF((SELECT DISTINCT ',' + TableName FROM posTableMaster 
			 WHERE convert(VARCHAR(max),TableMasterId) = 
			 CASE WHEN CharIndex(',',linktoTableMasterIds) = 0 THEN linktoTableMasterIds 
			 ELSE Substring(linktoTableMasterIds,0,CharIndex(',',linktoTableMasterIds)) end
			 FOR XML PATH(''), TYPE) .value('.', 'NVARCHAR(MAX)'),1,1,'') TableName
			
		FROM 
			posOrderMaster OM
		WHERE
			OM.linktoCounterMasterId = @linktoCounterMasterId
			and (@linktoOrderStatusMasterId IS NOT NULL and OM.linktoOrderStatusMasterId = @linktoOrderStatusMasterId
			OR @linktoOrderStatusMasterId IS NULL and OM.linktoOrderStatusMasterId IS NULL OR OM.linktoOrderStatusMasterId = ISNULL(@linktoOrderStatusMasterId,OM.linktoOrderStatusMasterId))
			and ',' + linktoTableMasterIds + ','  like '%,' + CONVERT(varchar,ISNULL(@linktoTableMasterIds,OM.linktoTableMasterIds))+ ',%'
			and OM.linktoOrderTypeMasterId = ISNULL(@linktoOrderTypeMasterId,linktoOrderTypeMasterId)
			and CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8), @FromDate, 112)
			and linktoSalesMasterId IS NULL
			and linktoBusinessMasterId=@linktoBusinessMasterId

		ORDER BY OrderMasterId DESC

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterBySales_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posOrderMasterBySales_SelectAll 
CREATE PROCEDURE [dbo].[posOrderMasterBySales_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posOrderMaster.*
	
	FROM
		 posOrderMaster
	WHERE 
		linktoSalesMasterId IS NULL AND linktoBusinessMasterId = @linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterByTableMasterId_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterByTableMasterId_Select]
	 @linktoTableMasterId varchar(50)
	 ,@linktoBusinessMasterId smallint
	 ,@FromDate Datetime
AS
BEGIN
	SET NOCOUNT ON;

	SELECT
		
			Count(*)As OrderCount
			
		FROM 
			posOrderMaster OM
		WHERE
	        ',' + linktoTableMasterIds + ','  like '%,' + CONVERT(varchar,ISNULL(@linktoTableMasterId,OM.linktoTableMasterIds))+ ',%'
			and CONVERT(varchar(8),OrderDateTime, 112) = CONVERT(varchar(8), @FromDate, 112)
			and linktoSalesMasterId IS NULL
			and linktoBusinessMasterId=@linktoBusinessMasterId


	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterByTotalAmount_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterByTotalAmount_Update]
	 @OrderMasterId bigint
	,@TotalAmount money
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoWaiterMasterId smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posOrderMaster
	SET
		TotalAmount = @TotalAmount
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoWaiterMasterId = @linktoWaiterMasterId
	WHERE
		OrderMasterId = @OrderMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterDiscountByOrderMasterIds_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterDiscountByOrderMasterIds_Update]
	@OrderMasterIds varchar(100),
	@Discount money = 0.0,

	-- if its is 1 then discount is in perentage 
	-- if 0 then discount is in rupees
	@isPercentage bit ,
	@DiscountTemp money = NULL,
	@Status smallint OUTPUT
AS
BEGIN
		--if discount is in percentage then it count for all and updtae all raw
IF @isPercentage=1
	BEGIN
	
	UPDATE q 
	SET 
		q.DiscountPercentage= @Discount,
		@DiscountTemp= ((SELECT TotalAmount FROM posOrderMaster OM WHERE OM.OrderMasterId=q.OrderMasterId)*@Discount)/100,
		q.Discount = @DiscountTemp,
		q.NetAmount = (SELECT TotalAmount FROM posOrderMaster OM WHERE OM.OrderMasterId=q.OrderMasterId) - @DiscountTemp
		FROM posOrderMaster q
	WHERE
		q.OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ','))
	END
ELSE
	BEGIN
	
	-- if discount is in rupees then discount set on the max(totalamount) on the top if raw are greater than 1 
	UPDATE q
	SET 
		q.DiscountPercentage= 0.0,
		q.Discount = @Discount,
		q.NetAmount = (SELECT TotalAmount FROM posOrderMaster OM WHERE OM.OrderMasterId=q.OrderMasterId) - @Discount
		FROM posOrderMaster q
	WHERE
		q.OrderMasterId = (SELECT TOP 1 OrderMasterId FROM posOrderMaster WHERE  TotalAmount = ( SELECT MAX(TotalAmount) FROM posOrderMaster WHERE OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ',')))  AND OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ',')))


	-- in remain raw discount is set with 0
	UPDATE q
	SET 
		q.DiscountPercentage= 0.0,
		q.Discount = 0.0,
		q.NetAmount = (SELECT TotalAmount FROM posOrderMaster OM WHERE OM.OrderMasterId=q.OrderMasterId) 
		FROM posOrderMaster q
	WHERE
		q.OrderMasterId <> (SELECT TOP 1 OrderMasterId FROM posOrderMaster WHERE  TotalAmount = ( SELECT MAX(TotalAmount) FROM posOrderMaster WHERE OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ','))) AND OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ',')))
		AND q.OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ','))
	END

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END

GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterForKOT_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterForKOT_Select]
	@OrderMasterId bigint
AS
BEGIN
	SET NOCOUNT ON;
	
	SELECT OrderMasterId
		,OrderNumber
		,OrderDateTime
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS WaiterName
		,Remark
		,(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = linktoOrderTypeMasterId) AS OrderType
		,STUFF((SELECT distinct ', ' + TableName FROM posTableMaster WHERE TableMasterId IN (SELECT parseValue FROM dbo.Parse(linktoTableMasterIds,','))
			FOR XML PATH(''), TYPE).value('.', 'VARCHAR(MAX)'), 1, 1, '') As TableName
	FROM posOrderMaster
	WHERE OrderMasterId = @OrderMasterId

END

GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterForPrint_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOrderMasterForPrint_SelectAll]
	 @linktoBusinessMasterId smallint
	,@SystemDateTime datetime
AS
BEGIN
	
	SET NOCOUNT ON;

    SELECT *
	FROM posOrderMaster
	WHERE linktoBusinessMasterId = @linktoBusinessMasterId
		AND CONVERT(varchar(8), OrderDateTime, 112) = CONVERT(varchar(8), @SystemDateTime, 112)
		AND PrintCount = 0
		AND linktoSourceMasterId = 3 -- Android App
END

GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterKOTPrintHeader_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterKOTPrintHeader_SelectAll]
	@linktoOrderMasterId smallint 
AS
BEGIN 

	SET NOCOUNT ON

SELECT   OrderNumber,OrderDateTime 
		,STUFF((SELECT distinct ',' + TableName  
			from posTableMaster
			WHERE TableMasterId in (select parsevalue from dbo.Parse(OM.linktoTableMasterIds,','))FOR XML PATH(''), TYPE).value('.', 'VARCHAR(MAX)'),1,1,'') AS TableName
	    ,(SELECT WaiterName FROM posWaiterMaster WM WHERE WM.WaiterMasterId=OM.linktoWaiterMasterId) AS WaiterName
		,(SELECT OTM.OrderType FROM posOrderTypeMaster OTM WHERE OTM.OrderTypeMasterId= OM.linktoOrderTypeMasterId) AS OrderType
		
FROM	
		posOrderMaster OM
WHERE
		OrderMasterId= @linktoOrderMasterId

	RETURN
END










GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterlinktoSalesMasterId_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterlinktoSalesMasterId_Update]
	@OrderMasterIds varchar(200)
	,@linktoSalesMasterId bigint
	,@UpdateDateTime datetime = NULL 
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF 
	UPDATE posOrderMaster
	SET  
		 linktoSalesMasterId = @linktoSalesMasterId 
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy		
	WHERE
		OrderMasterId In (SELECT * from dbo.Parse(@OrderMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterOrderItemTranReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterOrderItemTranReport_SelectAll]
 @CounterMasterId smallint = NULL
,@FromDate Datetime 
,@ToDate DateTime
AS
BEGIN

	SET NOCOUNT ON

	SELECT DISTINCT  
		ItemMasterId,ItemCode,ItemName,sum(Quantity) Quantity,sum(Quantity * Rate) as Amount 
	FROM 
		posOrderMaster OM
		JOIN posOrderItemTran OT ON OT.linktoOrderMasterId = OM.OrderMasterId
		JOIN posItemMaster IM ON Im.ItemMasterId = OT.linktoItemMasterId
	WHERE 
		OM.linktoCounterMasterId = ISNULL(@CounterMasterId,OM.linktoCounterMasterId)
		AND  CONVERT(varchar(8),OM.OrderDateTime,112)   BETWEEN  CONVERT(varchar(8),@FromDate,112)  AND CONVERT(varchar(8), @ToDate,112) 
	GROUP BY 
		ItemMasterId,ItemCode,ItemName
	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterOrderNumber_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterOrderNumber_SelectAll]
	 @TableMasterId int = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 OrderMasterId
		,OrderNumber
		,linktoSalesMasterId
	FROM
		 posOrderMaster
	WHERE
		(linktoTableMasterIds = ISNULL(CAST(@TableMasterId AS varchar(50)), linktoTableMasterIds)
		OR @TableMasterId IN (SELECT * FROM dbo.Parse(linktoTableMasterIds, ',')))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
		AND linktoSalesMasterId IS NULL
	--WHERE 
	--	(linktoTableMasterIds = ISNULL(@linktoTableMasterIds, linktoTableMasterIds)
	--		OR linktoTableMasterIds LIKE '%,' + @linktoTableMasterIds
	--		OR linktoTableMasterIds LIKE @linktoTableMasterIds + ',%'
	--		OR linktoTableMasterIds LIKE '%,' + @linktoTableMasterIds + ',%')
	--	AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY OrderNumber

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterOrderNumberAutoIncrement_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterOrderNumberAutoIncrement_Select]
	 @linktoCounterMasterId smallint
	,@SystemDateTime datetime
	,@KOTNumber varchar(20) OUTPUT
AS
BEGIN
	
	DECLARE @Type varchar(10), @Numbering varchar(10), @prefix varchar(10), @digits smallint, @OrderNumber varchar(20)

	SELECT @Type = value FROM posCounterSettingValueTran
	WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCounterSettingMasterId = 15

	SELECT @Numbering = value FROM posCounterSettingValueTran
	WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCounterSettingMasterId = 16

	SELECT @prefix = value FROM posCounterSettingValueTran
	WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCounterSettingMasterId = 17

	SELECT @digits = value FROM posCounterSettingValueTran
	WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCounterSettingMasterId = 18

	IF (@type = 'Automatic')
	BEGIN
	   IF (@Numbering = 'Yearly')
		  SELECT @OrderNumber = ISNULL((MAX(CONVERT(int, SUBSTRING(OrderNumber, PATINDEX('%[0-9]%', OrderNumber), LEN(OrderNumber)))) + 1), 1) FROM posOrderMaster WHERE linktoCounterMasterId = @linktoCounterMasterId AND YEAR(OrderDateTime) = YEAR(@SystemDateTime)
	   ELSE IF(@Numbering = 'Monthly')
		  SELECT @OrderNumber = ISNULL((MAX(CONVERT(int, SUBSTRING(OrderNumber, PATINDEX('%[0-9]%', OrderNumber), LEN(OrderNumber)))) + 1), 1) FROM posOrderMaster WHERE linktoCounterMasterId = @linktoCounterMasterId AND YEAR(OrderDateTime) = YEAR(@SystemDateTime) AND MONTH(OrderDateTime) = MONTH(@SystemDateTime)
	   ELSE
		  SELECT @OrderNumber = ISNULL((MAX(CONVERT(int, SUBSTRING(OrderNumber, PATINDEX('%[0-9]%', OrderNumber), LEN(OrderNumber)))) + 1), 1) FROM posOrderMaster WHERE linktoCounterMasterId = @linktoCounterMasterId AND CONVERT(varchar(8), OrderDateTime, 112) = CONVERT(varchar(8), @SystemDateTime, 112)

	   SET @KOTNumber = @prefix + RIGHT('00000000000000000000' + @OrderNumber, @digits)
	END

END


GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterPrintCount_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterPrintCount_Update]
	 @OrderMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posOrderMaster
	SET
		 PrintCount = PrintCount + 1
	WHERE
		OrderMasterId = @OrderMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderMasterSalesMasterIdByTableMasterId_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderMasterSalesMasterIdByTableMasterId_Select] 
	@linktoTableMasterIds varchar(50)
AS
BEGIN
	SELECT	
		linktoSalesMasterId
	FROM 
		posOrderMaster OM
	WHERE
        ',' + linktoTableMasterIds + ','  like '%,' + CONVERT(varchar,ISNULL(@linktoTableMasterIds,OM.linktoTableMasterIds))+ ',%'
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderTaxTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOrderTaxTran_Delete]
	 @linktoOrderMasterId bigint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posOrderTaxTran
	WHERE
		linktoOrderMasterId = @linktoOrderMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderTaxTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOrderTaxTran_Insert]
	 @OrderTaxTranId bigint OUTPUT
	,@linktoOrderMasterId bigint
	,@TaxIndex smallint
	,@TaxRate numeric(5,2)
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DECLARE @linktoTaxMasterId smallint,@TaxName varchar(50),@IsEnabled bit
	
	SELECT @linktoTaxMasterId = TaxMasterId,@TaxName = TaxName,@IsEnabled = IsEnabled FROM posTaxMaster WHERE TaxIndex = @TaxIndex AND linktoBusinessMasterId = @linktoBusinessMasterId

	if(@IsEnabled = 1)
	BEGIN
		INSERT INTO posOrderTaxTran
		(
			 linktoOrderMasterId
			,linktoTaxMasterId
			,TaxName
			,TaxRate
		)
		VALUES
		(
			 @linktoOrderMasterId
			,@linktoTaxMasterId
			,@TaxName
			,@TaxRate
		)
	END
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OrderTaxTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderTrackTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posOrderTrackTran_Insert]
	 @OrderTrackTranId bigint OUTPUT
	,@linktoOrderMasterId bigint
	,@linktoOrderStatusMasterId smallint = NULL
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy  smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF 

	INSERT INTO posOrderTrackTran
	(
		  linktoOrderMasterId
		 ,linktoOrderStatusMasterId
		 ,UpdateDateTime
		 ,linktoUserMasterIdUpdatedBy 
	)
	VALUES
	(
		  @linktoOrderMasterId
		 ,@linktoOrderStatusMasterId
		 ,@UpdateDateTime
		 ,@linktoUserMasterIdUpdatedBy 
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @OrderTrackTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderTrackTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posOrderTrackTran_SelectAll 2
CREATE PROCEDURE [dbo].[posOrderTrackTran_SelectAll]

	@linktoOrderStatusMasterId  smallint

AS
BEGIN 
	SET NOCOUNT ON 
		SELECT 
			linktoOrderMasterId,linktoSalesMasterId,BillNumber,CustomerName,Phone
			,CASE WHEN IsPrimary IS NULL THEN (SELECT TOP 1 Address FROM posCustomerAddressTran WHERE linktoCustomerMasterId = CustomerMasterId AND IsPrimary = 0) ELSE Address END Address
			,CASE WHEN IsPrimary IS NULL THEN (SELECT TOP 1 CityName FROM posCustomerAddressTran CAT1 JOIN posCityMaster CM1 ON CM1.CityMasterId = CAT1.linktoCityMasterId WHERE CAT1.linktoCustomerMasterId = CustomerMasterId AND CAT1.IsPrimary = 0) ELSE City END City
			,CASE WHEN IsPrimary IS NULL THEN (SELECT TOP 1 AreaName FROM posCustomerAddressTran CAT2 JOIN posAreaMaster AM1 ON AM1.AreaMasterId = CAT2.linktoAreaMasterId WHERE CAT2.linktoCustomerMasterId = CustomerMasterId AND CAT2.IsPrimary = 0) ELSE Area END Area 
		FROM
		(
			SELECT DISTINCT
				OM.OrderMasterId As linktoOrderMasterId, SM.SalesMasterId AS linktoSalesMasterId,SM.BillNumber,CM.CustomerName
				,CM.Phone1 As Phone,CAT.Address,CTM.CityName As City,AM.AreaName AS Area,CAT.IsPrimary,CustomerMasterId
			FROM 
				posOrderMaster OM
			JOIN 
				posSalesMaster SM ON SM.SalesMasterId = OM.linktoSalesMasterId
			JOIN
				posCustomerMaster CM ON	CM.CustomerMasterId = SM.linktoCustomerMasterId
			LEFT JOIN
				posCustomerAddressTran CAT ON CAT.linktoCustomerMasterId = CM.CustomerMasterId AND IsPrimary = 1
			LEFT JOIN 
				posCityMaster CTM ON CTM.CityMasterId = CAT.linktoCityMasterId
			LEFT JOIN	
				posAreaMaster AM ON AM.AreaMasterId = CAT.linktoAreaMasterId	 
			WHERE 
				SM.linktoOrderStatusMasterId = @linktoOrderStatusMasterId 
		)TempTable
		ORDER BY linktoSalesMasterId
	 
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderTrackTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderTrackTran_Update]
	 @OrderTrackTranId bigint 
	,@linktoOrderStatusMasterId smallint
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy  smallint
	,@OrderMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF  
	 
	INSERT INTO posOrderTrackTran
	(
		  linktoOrderMasterId
		 ,linktoOrderStatusMasterId
		 ,UpdateDateTime
		 ,linktoUserMasterIdUpdatedBy 
	)
	select
		 parsevalue
		,@linktoOrderStatusMasterId
		,@UpdateDateTime
		,@linktoOrderStatusMasterId
	FROM dbo.Parse(@OrderMasterIds, ',')
	 
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderTypeMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderTypeMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posOrderTypeMaster.*
	FROM
		 posOrderTypeMaster
	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posOrderTypeMasterOrderType_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posOrderTypeMasterOrderType_SelectAll]
  @linktoBusinessTypeMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 OrderTypeMasterId
		,OrderType
	FROM
		 posOrderTypeMaster
	WHERE 
		linktoBusinessTypeMasterId=@linktoBusinessTypeMasterId
	ORDER BY OrderType

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPaymentTypeCateogryMasterPaymentTypeCategory_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPaymentTypeCateogryMasterPaymentTypeCategory_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 PaymentTypeCategoryMasterId
		,PaymentTypeCategory
	FROM
		 posPaymentTypeCateogryMaster
	ORDER BY PaymentTypeCategory

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPaymentTypeMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPaymentTypeMaster_DeleteAll]
	 @PaymentTypeMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRawCount int

	DELETE
	FROM
		 posPaymentTypeMaster
	WHERE
		PaymentTypeMasterId IN (SELECT * from dbo.Parse(@PaymentTypeMasterIds, ','))
		AND PaymentTypeMasterId NOT IN
		(
			SELECT linktoPaymentTypeMasterId FROM posSalesPaymentTran where IsDeleted = 0 AND IsEnabled = 1 
			AND linktoPaymentTypeMasterId IN (SELECT * from dbo.Parse(@PaymentTypeMasterIds, ','))
		)
	
	SET @UpdatedRawCount = @@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

		SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@PaymentTypeMasterIds, ','))
		IF @TotalRecords = @UpdatedRawCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END


	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPaymentTypeMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPaymentTypeMaster_Insert]
	 @PaymentTypeMasterId smallint OUTPUT
	,@ShortName varchar(10)
	,@PaymentType varchar(50)
	,@Description varchar(500) = NULL
	,@linktoPaymentTypeCategoryMasterId smallint
	,@IsEnabled bit
	,@linktoBusinessMasterId smallint
	,@linktoUserMasterIdCreatedBy smallint
	,@CreateDateTime datetime
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT PaymentTypeMasterId FROM posPaymentTypeMaster WHERE PaymentType = @PaymentType AND linktoPaymentTypeCategoryMasterId = @linktoPaymentTypeCategoryMasterId AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @PaymentTypeMasterId = PaymentTypeMasterId FROM posPaymentTypeMaster WHERE PaymentType = @PaymentType AND linktoPaymentTypeCategoryMasterId = @linktoPaymentTypeCategoryMasterId AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posPaymentTypeMaster
	(
		 ShortName
		,PaymentType
		,Description
		,linktoPaymentTypeCategoryMasterId
		,IsEnabled
		,linktoBusinessMasterId
		,linktoUserMasterIdCreatedBy
		,CreateDateTime
		
	)
	VALUES
	(
		 @ShortName
		,@PaymentType
		,@Description
		,@linktoPaymentTypeCategoryMasterId
		,@IsEnabled
		,@linktoBusinessMasterId
		,@linktoUserMasterIdCreatedBy
		,@CreateDateTime
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @PaymentTypeMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPaymentTypeMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPaymentTypeMaster_Select]
	 @PaymentTypeMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posPaymentTypeMaster.*
		,(SELECT PaymentTypeCategory FROM posPaymentTypeCateogryMaster WHERE PaymentTypeCategoryMasterId = linktoPaymentTypeCategoryMasterId) AS PaymenTypeCategory
	FROM
		 posPaymentTypeMaster
	WHERE
		PaymentTypeMasterId = @PaymentTypeMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPaymentTypeMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPaymentTypeMaster_SelectAll]
	 @IsEnabled bit = NULL
	 ,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posPaymentTypeMaster.*
		,(SELECT PaymentTypeCategory FROM posPaymentTypeCateogryMaster WHERE PaymentTypeCategoryMasterId = linktoPaymentTypeCategoryMasterId) AS PaymentTypeCategory		
	FROM
		 posPaymentTypeMaster
	WHERE
		((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPaymentTypeMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPaymentTypeMaster_Update]
	 @PaymentTypeMasterId smallint
	,@ShortName varchar(10)
	,@PaymentType varchar(50)
	,@Description varchar(500) = NULL
	,@linktoPaymentTypeCategoryMasterId smallint
	,@IsEnabled bit
	,@linktoBusinessMasterId smallint
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@UpdateDateTime datetime = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT PaymentTypeMasterId FROM posPaymentTypeMaster WHERE PaymentTypeMasterId != @PaymentTypeMasterId AND
				PaymentType = @PaymentType AND linktoPaymentTypeCategoryMasterId = @linktoPaymentTypeCategoryMasterId AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posPaymentTypeMaster
	SET
		 ShortName = @ShortName
		,PaymentType = @PaymentType
		,Description = @Description
		,linktoPaymentTypeCategoryMasterId = @linktoPaymentTypeCategoryMasterId
		,IsEnabled = @IsEnabled
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,UpdateDateTime = @UpdateDateTime
		
	WHERE
		PaymentTypeMasterId = @PaymentTypeMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPaymentTypeMasterPaymentType_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPaymentTypeMasterPaymentType_SelectAll]

 @linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 PaymentTypeMasterId
		,PaymentType
	FROM
		 posPaymentTypeMaster
	WHERE
		IsEnabled = 1
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY PaymentType

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseItemTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseItemTran_Delete]
	 @linktoPurchaseMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posPurchaseItemTran
	WHERE
		linktoPurchaseMasterId = @linktoPurchaseMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseItemTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseItemTran_Insert]
	 @PurchaseItemTranId int OUTPUT
	,@linktoPurchaseMasterId int
	,@linktoItemMasterId int
	,@Quantity numeric(9, 2)
	,@linktoUnitMasterId smallint
	,@Rate money
	,@Tax money
	,@Discount money
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posPurchaseItemTran
	(
		 linktoPurchaseMasterId
		,linktoItemMasterId
		,Quantity
		,linktoUnitMasterId
		,Rate
		,Tax
		,Discount
	)
	VALUES
	(
		 @linktoPurchaseMasterId
		,@linktoItemMasterId
		,@Quantity
		,@linktoUnitMasterId
		,@Rate
		,@Tax
		,@Discount
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @PurchaseItemTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseItemTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseItemTran_SelectAll]

@linktoPurchaseMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posPurchaseItemTran.*
		,(SELECT VoucherNumber FROM posPurchaseMaster WHERE PurchaseMasterId = linktoPurchaseMasterId) AS Purchase		
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item	
		,(SELECT ItemCode FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS ItemCode		
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterId) AS Unit
	FROM
		 posPurchaseItemTran
	WHERE
		linktoPurchaseMasterId=@linktoPurchaseMasterId
	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseMaster_DeleteAll]
	 @PurchaseMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	DELETE 
	FROM
		posPurchaseItemTran
	WHERE	
		linktoPurchaseMasterId IN (SELECT * from dbo.Parse(@PurchaseMasterIds, ','))

	DELETE
	FROM
		 posPurchaseMaster
	WHERE
		PurchaseMasterId IN (SELECT * from dbo.Parse(@PurchaseMasterIds, ','))
		
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseMaster_Insert]
	 @PurchaseMasterId int OUTPUT
	,@linktoDepartmentMasterId smallint
	,@VoucherNumber varchar(20)
	,@PurchaseDate date
	,@linktoSupplierMasterId smallint
	,@InvoiceNumber varchar(20) = NULL
	,@InvoiceDate date = NULL
	,@TotalAmount money
	,@TotalTax money
	,@TotalDiscount money
	,@NetAmount money
	,@Remark varchar(250) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	IF EXISTS(SELECT PurchaseMasterId FROM posPurchaseMaster WHERE VoucherNumber = @VoucherNumber AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @PurchaseMasterId= PurchaseMasterId FROM posPurchaseMaster WHERE VoucherNumber = @VoucherNumber AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posPurchaseMaster
	(
		 linktoDepartmentMasterId
		,VoucherNumber
		,PurchaseDate
		,linktoSupplierMasterId
		,InvoiceNumber
		,InvoiceDate
		,TotalAmount
		,TotalTax
		,TotalDiscount
		,NetAmount
		,Remark
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @linktoDepartmentMasterId
		,@VoucherNumber
		,@PurchaseDate
		,@linktoSupplierMasterId
		,@InvoiceNumber
		,@InvoiceDate
		,@TotalAmount
		,@TotalTax
		,@TotalDiscount
		,@NetAmount
		,@Remark
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @PurchaseMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseMaster_Select]
	 @PurchaseMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posPurchaseMaster.*
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department
		,(SELECT SupplierName FROM posSupplierMaster WHERE SupplierMasterId = linktoSupplierMasterId) AS Supplier
	FROM
		 posPurchaseMaster
	WHERE
		PurchaseMasterId = @PurchaseMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseMaster_SelectAll]
	 @VoucherNumber varchar(20)
	,@PurchaseDate date = NULL
	,@linktoSupplierMasterId smallint = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posPurchaseMaster.*
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department		
		,(SELECT SupplierName FROM posSupplierMaster WHERE SupplierMasterId = linktoSupplierMasterId) AS Supplier		
	FROM
		 posPurchaseMaster
	WHERE
		VoucherNumber LIKE @VoucherNumber + '%'
		AND CONVERT(varchar(8), PurchaseDate, 112) = CONVERT(varchar(8), @PurchaseDate, 112)
		AND linktoSupplierMasterId = ISNULL(@linktoSupplierMasterId, linktoSupplierMasterId)
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseMaster_Update]
	 @PurchaseMasterId int
	,@linktoDepartmentMasterId smallint
	,@VoucherNumber varchar(20)
	,@PurchaseDate date
	,@linktoSupplierMasterId smallint
	,@InvoiceNumber varchar(20) = NULL
	,@InvoiceDate date = NULL
	,@TotalAmount money
	,@TotalTax money
	,@TotalDiscount money
	,@NetAmount money
	,@Remark varchar(250) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	IF EXISTS(SELECT PurchaseMasterId FROM posPurchaseMaster WHERE VoucherNumber = @VoucherNumber AND PurchaseMasterId!=@PurchaseMasterId AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @PurchaseMasterId= PurchaseMasterId FROM posPurchaseMaster WHERE VoucherNumber = @VoucherNumber AND PurchaseMasterId!=@PurchaseMasterId AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	UPDATE posPurchaseMaster
	SET
		 linktoDepartmentMasterId = @linktoDepartmentMasterId
		,VoucherNumber = @VoucherNumber
		,PurchaseDate = @PurchaseDate
		,linktoSupplierMasterId = @linktoSupplierMasterId
		,InvoiceNumber = @InvoiceNumber
		,InvoiceDate = @InvoiceDate
		,TotalAmount = @TotalAmount
		,TotalTax = @TotalTax
		,TotalDiscount = @TotalDiscount
		,NetAmount = @NetAmount
		,Remark = @Remark
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoBusinessMasterId=@linktoBusinessMasterId
	WHERE
		PurchaseMasterId = @PurchaseMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMasterBylinktoSupplierMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseMasterBylinktoSupplierMasterId_SelectAll]
	@linktoSupplierMasterId smallint,
	@FromDate datetime,
	@ToDate datetime,
	@linktoBusinessMasterId smallint
 AS
BEGIN
	SELECT
			*
	  FROM 
			posPurchaseMaster 
	  WHERE 
			linktoSupplierMasterId = @linktoSupplierMasterId 
			AND linktoBusinessMasterId=@linktoBusinessMasterId
			AND
			CONVERT(varchar(8),PurchaseDate,112) BETWEEN CONVERT(varchar(8), @FromDate,112) AND CONVERT(varchar(8), @ToDate,112)	
	
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMasterByRemaingPaymentAmount_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posPurchaseMasterByRemaingPaymentAmount_Select 19
CREATE PROCEDURE [dbo].[posPurchaseMasterByRemaingPaymentAmount_Select]
	@PurchaseMasterId smallint
	
 AS
BEGIN
		
	DECLARE @NetAmount money
			,@Amount money

	SELECT @NetAmount = NetAmount 
	FROM posPurchaseMaster 
	WHERE PurchaseMasterId = @PurchaseMasterId

	SELECT @Amount = SUM(Amount)
	FROM posSupplierPaymentTran 
	WHERE linktoPurchaseMasterId = @PurchaseMasterId
		AND IsDeleted = 0

	SELECT SUM(@NetAmount) - ISNULL(SUM(@Amount), 0) AS ReaminigAmount
	
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMasterCategoryWiseReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posPurchaseMasterCategoryWiseReport_SelectAll '12/3/2015 10:22:38 AM','12/3/2015 10:22:38 AM','8,9'
CREATE PROCEDURE [dbo].[posPurchaseMasterCategoryWiseReport_SelectAll]
	@FromDate datetime,
	@ToDate datetime,
	@CategoryMasterIds varchar(100)
	,@linktoBusinessMasterId smallint
AS
BEGIN
if @CategoryMasterIds='0'
BEGIN
	SELECT 
		SM.SupplierName,sum(PM.TotalAmount) TotalAmount,sum(Pm.NetAmount) NetAmount
	FROM
		posPurchaseMaster PM,posPurchaseItemTran PIT,posItemMaster IM, posSupplierMaster SM
	WHERE
		PM.linktoSupplierMasterId=SM.SupplierMasterId 
	AND
		PIT.linktoPurchaseMasterId=PM.PurchaseMasterId
	AND
		PIT.linktoItemMasterId=IM.ItemMasterId	
	AND
	PM.linktoBusinessMasterId=@linktoBusinessMasterId
	AND

	CONVERT(varchar(8),PM.PurchaseDate,112) Between CONVERT(varchar(8),@FromDate,112) AND CONVERT(varchar(8),@ToDate,112)

	GROUP BY SupplierName,SupplierMasterId
END
ELSE
	BEGIN
	SELECT 
		SM.SupplierName,sum(PM.TotalAmount) TotalAmount,sum(Pm.NetAmount) NetAmount
	FROM
		posPurchaseMaster PM,posPurchaseItemTran PIT,posItemMaster IM, posSupplierMaster SM
	WHERE
		PM.linktoSupplierMasterId=SM.SupplierMasterId 
	AND
		PIT.linktoPurchaseMasterId=PM.PurchaseMasterId
	AND
		PIT.linktoItemMasterId=IM.ItemMasterId
	AND
		PM.linktoBusinessMasterId=@linktoBusinessMasterId
	AND
	CONVERT(varchar(8),PM.PurchaseDate,112) Between CONVERT(varchar(8),@FromDate,112) AND CONVERT(varchar(8),@ToDate,112)
	AND
	IM.linktoCategoryMasterId IN ( SELECT parseValue FROM dbo.Parse(@CategoryMasterIds,','))

	GROUP BY SupplierName,SupplierMasterId
	END
END






GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMasterDayWiseReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseMasterDayWiseReport_SelectAll]
	@FromDate datetime,
	@ToDate datetime
	,@linktoBusinessMasterId smallint
AS
BEGIN
	SELECT 
		 PurchaseDate,Datename(dw,PurchaseDate) PurchaseDay ,sum(TotalAmount) As TotalAmount 
	FROM 
		posPurchaseMaster
	WHERE 
		linktoBusinessMasterId=@linktoBusinessMasterId
		AND
		convert(varchar(8),PurchaseDate,112) BETWEEN convert(varchar(8),@FromDate,112) AND convert(varchar(8),@ToDate,112)

	GROUP BY PurchaseDate
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMasterPurchaseDateWisedetailReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posPurchaseMasterPurchaseDateWisedetailReport_SelectAll]
	@linktoBusinessMasterId smallint,
	@FromDate datetime,
	@ToDate datetime,
	@linktoSuppliermasterId smallint, 
	@CategoryMasterIds varchar(1000), 
	@ItemMasterIds varchar(1000) 
AS
BEGIN
	SELECT 
		PurchaseDate,DepartmentName,VoucherNumber,InvoiceNumber,ItemName,UnitName,SupplierName,Quantity,Rate,PIT.Tax,Discount,
		convert(numeric(18,2),(Quantity * Rate)) TotalAmount,convert(numeric(18,2),(((Quantity * Rate) + PIT.Tax) - Discount)) AS NetAmount
	FROM 
		posPurchaseMaster PM
	JOIN
		posDepartmentMaster	DM ON DM.DepartmentMasterId = PM.linktoDepartmentMasterId
	JOIN
		posPurchaseItemTran PIT ON PIT.linktoPurchaseMasterId = PM.PurchaseMasterId
	JOIN
		posItemMaster IM ON IM.ItemMasterId = PIT.linktoItemMasterId
	JOIN
		posUnitMaster UM ON UM.UnitMasterId = PIT.linktoUnitMasterId
	JOIN
		posSupplierMaster SM ON SM.SupplierMasterId = linktoSupplierMasterId
	WHERE 
		DM.linktoBusinessMasterId = @linktoBusinessMasterId AND IM.IsEnabled = 1 AND IM.IsDeleted = 0
		AND Convert(varchar(8),PM.PurchaseDate,112) BETWEEN Convert(varchar(8),@FromDate,112) AND Convert(varchar(8),@ToDate,112)
		AND linktoSupplierMasterId = Case WHEN @linktoSuppliermasterId=0 THEN linktoSupplierMasterId ELSE @linktoSuppliermasterId END
		AND linktoCategoryMasterId IN (SELECT parseValue FROM DBO.Parse(@CategoryMasterIds,','))
		AND linktoItemMasterId IN (SELECT parseValue FROM DBO.Parse(@ItemMasterIds,','))
	ORDER BY
		DepartmentName
END



GO
/****** Object:  StoredProcedure [dbo].[posPurchaseMasterSupplierWiseReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posPurchaseMasterSupplierWiseReport_SelectAll '0','12/26/2015 12:00:00 AM','12/26/2015 12:00:00 AM' ,linktoCategoryMasterId
CREATE PROCEDURE [dbo].[posPurchaseMasterSupplierWiseReport_SelectAll]
	@SupplierIds varchar(1000),
	@FromDate datetime,
	@ToDate datetime,
	@linktoCategoryMasterId smallint  
 ,@linktoBusinessMasterId smallint
AS
BEGIN
	IF @SupplierIds = '0' 
			BEGIN
			SELECT 
				SM.SupplierName ,UM.UnitName,IM.ItemName ,SUM(TotalAmount) as Rate,SUM(Quantity) as Quantity ,SUM(pm.TotalAmount * pit.Quantity) as Amount
			FROM 
				posPurchaseMaster pm,posPurchaseItemTran pit,posUnitMaster UM,posItemMaster IM,posSupplierMaster SM
			WHERE 
				pit.linktoPurchaseMasterId = pm.PurchaseMasterId
				AND pit.linktoUnitMasterId = um.UnitMasterId
				AND Im.ItemMasterId = pit.linktoItemMasterId
				AND pm.linktoSupplierMasterId = sm.SupplierMasterId
				AND IM.linktoCategoryMasterId = CASE WHEN @linktoCategoryMasterId = 0 THEN IM.linktoCategoryMasterId ELSE @linktoCategoryMasterId END  
				AND CONVERT(varchar(8),PM.PurchaseDate,112) Between CONVERT(varchar(8),@FromDate,112) AND CONVERT(varchar(8),@ToDate,112)
				AND PM.linktoBusinessMasterId=@linktoBusinessMasterId
			GROUP BY 
				UM.UnitName,IM.ItemName,SM.SupplierName
			ORDER BY
				 SupplierName 
			END
	ELSE 
			BEGIN
			SELECT 
				SM.SupplierName ,UM.UnitName,IM.ItemName,SUM(TotalAmount) as Rate,SUM(Quantity) as Quantity,SUM(pm.TotalAmount * pit.Quantity) as Amount
			FROM 
				posPurchaseMaster pm,posPurchaseItemTran pit,posUnitMaster UM,posItemMaster IM,posSupplierMaster SM
			WHERE 
				pit.linktoPurchaseMasterId = pm.PurchaseMasterId
				AND pit.linktoUnitMasterId = um.UnitMasterId
				AND Im.ItemMasterId = pit.linktoItemMasterId
				AND pm.linktoSupplierMasterId = sm.SupplierMasterId
				AND PM.linktoBusinessMasterId=@linktoBusinessMasterId
				AND pm.linktoSupplierMasterId in (SELECT * FROM dbo.Parse(@SupplierIds,',')) 
				AND IM.linktoCategoryMasterId = CASE WHEN @linktoCategoryMasterId = 0 THEN IM.linktoCategoryMasterId ELSE @linktoCategoryMasterId END  
				AND CONVERT(varchar(8),PM.PurchaseDate,112) Between CONVERT(varchar(8),@FromDate,112) AND CONVERT(varchar(8),@ToDate,112)
			GROUP BY 
				UM.UnitName ,IM.ItemName,SM.SupplierName
			ORDER BY
					SupplierName 
			END 
END

 


GO
/****** Object:  StoredProcedure [dbo].[posRateCaptionMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRateCaptionMaster_Insert]
	 @RateCaptionMasterId smallint OUTPUT
	,@RateName varchar(20)
	,@RateCaption varchar(20)
	,@linktoBusinessMasterId smallint
	,@RateIndex smallint
	,@IsEnabled bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @ID as smallint

	SELECT @ID = RateCaptionMasterId FROM posRateCaptionMaster WHERE linktoBusinessMasterId = @linktoBusinessMasterId 
	AND RateIndex = @RateIndex

	IF @ID > 0
	BEGIN

		UPDATE posRateCaptionMaster
		SET
			 RateName = @RateName
			,RateCaption = @RateCaption
			,linktoBusinessMasterId = @linktoBusinessMasterId
			,RateIndex = @RateIndex
			,IsEnabled = @IsEnabled
		WHERE
			RateCaptionMasterId = @ID

		SET @RateCaptionMasterId = @ID
	END
	ELSE
	BEGIN
		INSERT INTO posRateCaptionMaster
		(
			 RateName
			,RateCaption
			,linktoBusinessMasterId
			,RateIndex
			,IsEnabled
		)
		VALUES
		(
			 @RateName
			,@RateCaption
			,@linktoBusinessMasterId
			,@RateIndex
			,@IsEnabled
		)

		SET @RateCaptionMasterId = @@IDENTITY
	END

	IF (@IsEnabled = 0)
	BEGIN
		DECLARE @Sql varchar(100)
		SET @Sql = 'UPDATE posItemRateTran SET Rate' + CAST(@RateIndex AS varchar) + ' = 0'
		EXEC(@Sql)
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN		
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posRateCaptionMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posRateCaptionMaster_SelectAll 2
CREATE PROCEDURE [dbo].[posRateCaptionMaster_SelectAll]

	@linktoBusinessMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 RateCaptionMasterId,RateName,CASE WHEN RateCaption IS NULL OR RateCaption = '' THEN RateName ELSE RateCaption END AS RateCaption,linktoBusinessMasterId,RateIndex,IsEnabled
	FROM
		 posRateCaptionMaster
	WHERE
		linktoBusinessMasterId = @linktoBusinessMasterId 
	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posRegisteredUserMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posRegisteredUserMaster_Insert]
	 @RegisteredUserMasterId int OUTPUT
	,@Email varchar(80)
	,@Phone varchar(15) = NULL
	,@Password varchar(25)
	,@FirstName varchar(25) = NULL
	,@LastName varchar(25) = NULL
	,@Gender varchar(6) = NULL
	,@BirthDate date = NULL
	,@linktoAreaMasterId smallint = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint = NULL
	,@LastLoginDateTime datetime = NULL
	,@linktoSourceMasterId smallint
	,@Comment varchar(100) = NULL
	,@IsEnabled bit
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT RegisteredUserMasterId FROM posRegisteredUserMaster WHERE Email = @Email AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @RegisteredUserMasterId = RegisteredUserMasterId FROM posRegisteredUserMaster WHERE Email = @Email AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posRegisteredUserMaster
	(
		 Email
		,Phone
		,Password
		,FirstName
		,LastName
		,Gender
		,BirthDate
		,linktoAreaMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,LastLoginDateTime
		,linktoSourceMasterId
		,Comment
		,IsEnabled
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @Email
		,@Phone
		,@Password
		,@FirstName
		,@LastName
		,@Gender
		,@BirthDate
		,@linktoAreaMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@LastLoginDateTime
		,@linktoSourceMasterId
		,@Comment
		,@IsEnabled
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @RegisteredUserMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posRegisteredUserMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRegisteredUserMaster_Select]
	 @RegisteredUserMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posRegisteredUserMaster.*
		,(SELECT AreaName FROM posAreaMaster WHERE AreaMasterId = linktoAreaMasterId) AS Area
	FROM
		 posRegisteredUserMaster
	WHERE
		RegisteredUserMasterId = @RegisteredUserMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posRegisteredUserMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRegisteredUserMaster_Update]
	 @RegisteredUserMasterId int
	,@Phone varchar(15) = NULL
	,@FirstName varchar(25) = NULL
	,@LastName varchar(25) = NULL
	,@Gender varchar(6) = NULL
	,@BirthDate date = NULL
	,@linktoAreaMasterId smallint = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Comment varchar(100) = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posRegisteredUserMaster
	SET
		 Phone = @Phone
		,FirstName = @FirstName
		,LastName = @LastName
		,Gender = @Gender
		,BirthDate = @BirthDate
		,linktoAreaMasterId = @linktoAreaMasterId
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,Comment = @Comment
	WHERE
		RegisteredUserMasterId = @RegisteredUserMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posRegisteredUserMasterByEmail_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRegisteredUserMasterByEmail_Select]
	@Email varchar(80)
AS
BEGIN
	
	SET NOCOUNT ON

	SELECT
		posRegisteredUserMaster.*
		,(SELECT AreaName FROM posAreaMaster WHERE AreaMasterId=linktoAreaMasterId)AS Area

	FROM
		posRegisteredUserMaster
	WHERE
		Email=@Email
END



GO
/****** Object:  StoredProcedure [dbo].[posRegisteredUserMasterPassword_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRegisteredUserMasterPassword_Update]
	 @RegisteredUserMasterId int
	,@Password varchar(25)
	,@OldPassword varchar(25)
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT Password FROM posRegisteredUserMaster WHERE RegisteredUserMasterId = @RegisteredUserMasterId AND Password = @OldPassword) OR @OldPassword = 'New'
	BEGIN
		UPDATE posRegisteredUserMaster
		SET
			Password = @OldPassword
			,UpdateDateTime = @UpdateDateTime
			,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		WHERE
			RegisteredUserMasterId = @RegisteredUserMasterId

		IF @@ERROR <> 0
			SET @Status = -1
		ELSE
			SET @Status = 0

		RETURN
	END		
	ELSE
	BEGIN
		SET @Status = -3
		RETURN
	END	
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posRoleMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRoleMaster_DeleteAll]
	 @RoleMasterIds varchar(1000)
	 ,@linktoUserMasterIdUpdatedBy smallint
	 ,@UpdateDateTime datetime
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecoeds int ,@UpdatedRawCount int
	set @TotalRecoeds = (SELECT count(*) FROM dbo.Parse(@RoleMasterIds, ','))

	UPDATE
		posRoleMaster
	SET 
		IsDeleted = 1,
		linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy,
		UpdateDateTime = @UpdateDateTime
	WHERE
		RoleMasterId IN (SELECT * from dbo.Parse(@RoleMasterIds, ','))
		AND RoleMasterId NOT IN 
		(
			SELECT linktoRoleMasterId FROM posUserMaster WHERE IsDeleted = 0 AND IsEnabled = 1 
			AND linktoRoleMasterId IN (SELECT * FROM dbo.Parse(@RoleMasterIds, ','))
		)

	SET @UpdatedRawCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		if @TotalRecoeds = @UpdatedRawCount
		begin
			SET @Status = 0
		end
		else
		begin
			SET @Status = -2
		end
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posRoleMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRoleMaster_Insert]
	 @RoleMasterId smallint OUTPUT
	,@Role varchar(50)
	,@Description varchar(100) = NULL
	,@IsEnabled bit
	,@linktoBusinessMasterId smallint
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT RoleMasterId FROM posRoleMaster WHERE Role = @Role AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsDeleted = 0)
	BEGIN
		SELECT @RoleMasterId = RoleMasterId FROM posRoleMaster WHERE Role = @Role  AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsDeleted = 0
		SET @Status = -2
		RETURN
	END
	INSERT INTO posRoleMaster
	(
		 Role
		,Description
		,linktoBusinessMasterId
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
	)
	VALUES
	(
		 @Role
		,@Description
		,@linktoBusinessMasterId
		,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
	)

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
	BEGIN
		SET @RoleMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posRoleMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posRoleMaster_Select]
	 @RoleMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT 
		 posRoleMaster.	*
	FROM
		posRoleMaster
	WHERE
		RoleMasterId = @RoleMasterId

	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posRoleMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRoleMaster_SelectAll]
	 @IsEnabled bit
	,@linktoBusinessMasterId smallint 
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posRoleMaster.*
	FROM
		posRoleMaster
	WHERE 
		IsDeleted = 0
		AND IsEnabled = @IsEnabled
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	
	ORDER BY Role

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posRoleMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posRoleMaster_Update]
	 @RoleMasterId smallint
	,@Role varchar(50)
	,@Description varchar(100) = NULL
	,@IsEnabled bit
	,@linktoBusinessMasterId   smallint
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT RoleMasterId FROM posRoleMaster	WHERE Role = @Role AND RoleMasterId != @RoleMasterId  AND linktoBusinessMasterId=@linktoBusinessMasterId
				AND IsDeleted = 0)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posRoleMaster
	SET
		 Role = @Role
		,Description = @Description
		,IsEnabled = @IsEnabled
		,linktoBusinessMasterId =@linktoBusinessMasterId 
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		RoleMasterId = @RoleMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posRoleMasterRole_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posRoleMasterRole_SelectAll]
@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 RoleMasterId
		,Role
	FROM
		posRoleMaster
	WHERE
		IsEnabled = 1 
		AND IsDeleted = 0
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY Role

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posSaleMasterCounterWiseTimeWiseSaleReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posSaleMasterCounterWiseTimeWiseSaleReport_SelectAll '2016-03-14 12:06:06.937','2016-03-15 12:06:06.937',null,null,3
CREATE PROCEDURE [dbo].[posSaleMasterCounterWiseTimeWiseSaleReport_SelectAll]
	 @FromBillDate datetime
	,@ToBillDate datetime 
	,@linktoCounterMasterId smallint = NULL
	,@TimeInterval smallint
	,@linktoBusinessMasterId int

AS
BEGIN

	SET NOCOUNT ON 

Declare @TimeSlot smallint = 0,@Date smalldatetime,@cols as varchar(max),@query as varchar(max)

CREATE TABLE #Temp(ID int identity(1,1),TimeInterval time(7),TimeIntervalString Varchar(100),BillDate Date,CounterName varchar(50),NetAmount money)

set @Date = '2016-03-10 00:00:00'

		WHILE(@TimeSlot < 24)
		BEGIN
			INSERT INTO #Temp(TimeInterval,TimeIntervalString,BillDate,CounterName,NetAmount)	
			SELECT 
				Convert(varchar(8),@Date,108),Convert(varchar,DATEPART(hh,Convert(varchar(8),@Date,108))) + ' - ' + Convert(Varchar,DATEPART(hh,CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, 1,@Date)),108)))
				,Convert(varchar(8),BillDateTime,112),CounterName,SUM(NetAmount)
			FROM 
				posSalesMaster SM
			JOIN
				posCounterMaster CM ON CM.CounterMasterId = SM.linktoCounterMasterId
			JOIN
				posTableMaster TM ON ',' + SM.linktoTableMasterIds + ',' LIKE '%,' + convert(varchar,TM.TableMasterId) + ',%'
			WHERE
				CONVERT(VARCHAR(8),DATEADD(MINUTE, -1,BillDateTime),108) BETWEEN CONVERT(VARCHAR(8), @Date,108) AND CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, -1,@Date)),108)		
				AND CONVERT(varchar(8),SM.BillDateTime,112) BETWEEN CONVERT(varchar(8),@FromBillDate,112) AND CONVERT(varchar(8),@ToBillDate,112)  
				AND SM.linktoCounterMasterId =ISNULL(@linktoCounterMasterId,SM.linktoCounterMasterId)	
				AND TM.linktoBusinessMasterId=@linktoBusinessMasterId
				AND SM.linktoBusinessMasterId=@linktoBusinessMasterId			
			GROUP BY
				Convert(varchar(8),BillDateTime,112),CounterName

			set @Date = DATEADD(hour,@TimeInterval,@Date)
			SET @TimeSlot = @TimeSlot + @TimeInterval
		END 


set @TimeSlot = 0
set @Date = '2016-03-10 00:00:00'

		WHILE(@TimeSlot < 24)
		BEGIN

			IF NOT EXISTS(SELECT ID FROM #Temp WHERE TimeInterval = Convert(varchar(8),@Date,108))
			BEGIN
				INSERT INTO #Temp(TimeInterval,TimeIntervalString)	
				values(Convert(varchar(8),@Date,108),Convert(varchar,DATEPART(hh,Convert(varchar(8),@Date,108))) + ' - ' + Convert(Varchar,DATEPART(hh,CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, 1,@Date)),108))))
			END

			set @Date = DATEADD(hour,@TimeInterval,@Date)
			SET @TimeSlot = @TimeSlot + @TimeInterval
		END


	SELECT @cols = STUFF((SELECT ',' + '[' + TimeIntervalString   + ']'
				FROM #Temp
				GROUP BY TimeInterval,TimeIntervalString
				ORDER BY TimeInterval,TimeIntervalString	
				FOR XML PATH(''), TYPE
				).value('.', 'NVARCHAR(MAX)') 
		   ,1,1,'') 

	SET @query =	'SELECT DISTINCT BillDate,CounterName,' + @cols + ' FROM
					(
						SELECT DISTINCT TimeIntervalString,BillDate ,CounterName,NetAmount
						FROM #Temp  
					)A
					PIVOT
					(
						SUM(NetAmount)
						FOR TimeIntervalString in (' + @Cols + ')
					)pvt
					WHERE BillDate IS NOT NULL'
	Exec (@query)

DROP Table #Temp

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSaleMasterOrderTypeWiseSale_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posSaleMasterOrderTypeWiseSale_SelectAll  '20160101','20160401',0
CREATE PROCEDURE [dbo].[posSaleMasterOrderTypeWiseSale_SelectAll]
 
 @FromDate DateTime ,
 @ToDate DateTime,
 @linktoCounterMasterId smallint 

AS
BEGIN

	SET NOCOUNT ON;
		SELECT 
			BillDateTime,SUM(Quantity) Quantity,SUM(NetAmount) NetAmount,OrderType 
		FROM
		(
			SELECT 
				CONVERT(Date, SM.BillDateTime) BillDateTime ,SUM(SIT.Quantity) Quantity,NetAmount,OT.OrderType
			FROM 
				posSalesMaster SM
				JOIN posSalesItemTran SIT ON Sm.SalesMasterId = SIT.linktoSalesMasterId
				JOIN posOrderTypeMaster OT ON OT.OrderTypeMasterId =SM.linktoOrderTypeMasterId
			WHERE 
				linktoCounterMasterId=CASE WHEN @linktoCounterMasterId=0 THEN linktoCounterMasterId ELSE @linktoCounterMasterId END
				AND CONVERT (VARCHAR(8),SM.BillDateTime,112) BETWEEN CONVERT (VARCHAR(8),@FromDate,112) AND CONVERT (VARCHAR(8),@ToDate,112)
			GROUP BY CONVERT(Date, SM.BillDateTime),OT.OrderType,NetAmount
		)Temp
		GROUP BY BillDateTime,OrderType
END



GO
/****** Object:  StoredProcedure [dbo].[posSaleMasterTimeIntervalWiseCatergorySale_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSaleMasterTimeIntervalWiseCatergorySale_SelectAll]
	  @FromBillDate datetime
	 ,@ToBillDate datetime 
	 ,@CategoryMasterId smallint = null
	 ,@TimeInterval smallint 
	 ,@AmountQuantity bit
	 ,@linktoBusinessMasterId int
AS
BEGIN
	
	SET NOCOUNT ON;

--	DECLARE @FromBillDate datetime,@ToBillDate datetime 
--SET @FromBillDate = '2016-03-14 0:00:00.00'
--SET @ToBillDate = '2016-03-14 12:06:06.937'

--DECLARE @linktoCounterMasterId smallint,@linktoSectionMasterId smallint
--SET @linktoCounterMasterId = null
--SET @linktoSectionMasterId = null

 Declare @TimeSlot smallint = 0,@Date smalldatetime,@cols as varchar(max),@query as varchar(max)

CREATE TABLE #Temp(ID int identity(1,1),TimeInterval time(7),TimeIntervalString Varchar(100),BillDate Date,CategoryName varchar(50),Rate money,Quantity int)

		set @Date = '2016-03-10 00:00:00' -- Static Date For Time Interval

		WHILE(@TimeSlot < 24)
		BEGIN
		INSERT INTO #Temp(TimeInterval,TimeIntervalString,BillDate,CategoryName,Rate,Quantity)	
		SELECT 
		Convert(varchar(8),@Date,108),Convert(varchar,DATEPART(hh,Convert(varchar(8),@Date,108))) + ' - ' + Convert(Varchar,DATEPART(hh,CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, 1,@Date)),108)))
		,Convert(varchar(8),BillDateTime,112),CM.CategoryName,SUM(((SIT.Rate*SIT.Quantity)+SIT.Tax1 +SIT.Tax2+SIT.Tax3+SIT.Tax4+SIT.Tax5)-SIT.DiscountAmount) as Rate,SUM(Quantity)
		FROM 
		posSalesMaster SM
		JOIN
		posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId
		JOIN
		posItemMaster IM ON IM.ItemMasterId=SIT.linktoItemMasterId
		JOIN
		posCategoryMaster CM ON CM.CategoryMasterId=IM.linktoCategoryMasterId		

		WHERE
		CONVERT(VARCHAR(8),DATEADD(MINUTE, -1,BillDateTime),108) BETWEEN CONVERT(VARCHAR(8), @Date,108) AND CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, 1,@Date)),108)		
		AND CONVERT(VARCHAR(8),BillDateTime,112) BETWEEN CONVERT(VARCHAR(8),@FromBillDate,112) AND CONVERT(VARCHAR(8),@ToBillDate,112)
		AND CM.CategoryMasterId =ISNULL(@CategoryMasterId,CM.CategoryMasterId)
		AND SM.SalesMasterId=SIT.linktoSalesMasterId
		AND SM.linktoBusinessMasterId=@linktoBusinessMasterId

		GROUP BY
		Convert(varchar(8),BillDateTime,112),CM.CategoryName

		set @Date = DATEADD(hour,@TimeInterval,@Date)
		SET @TimeSlot = @TimeSlot + @TimeInterval
		END

		set @TimeSlot = 0
		set @Date = '2016-03-10 00:00:00'

		WHILE(@TimeSlot < 24)
		BEGIN

		IF NOT EXISTS(SELECT ID FROM #Temp WHERE TimeInterval = Convert(varchar(8),@Date,108))
		BEGIN
		INSERT INTO #Temp(TimeInterval,TimeIntervalString)	
		values(Convert(varchar(8),@Date,108),Convert(varchar,DATEPART(hh,Convert(varchar(8),@Date,108))) + ' - ' + Convert(Varchar,DATEPART(hh,CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, 1,@Date)),108))))
		END

		set @Date = DATEADD(hour,@TimeInterval,@Date)
		SET @TimeSlot = @TimeSlot + @TimeInterval
		END

		SELECT @cols = STUFF((SELECT ',' + '[' + TimeIntervalString + ']'
		FROM #Temp
		GROUP BY TimeInterval,TimeIntervalString
		ORDER BY TimeInterval,TimeIntervalString	
		FOR XML PATH(''), TYPE
		).value('.', 'NVARCHAR(MAX)') 
		,1,1,'')

		if @AmountQuantity = 1
			BEGIN
		SET @query =	'SELECT DISTINCT BillDate,CategoryName,' + @cols + ' FROM
			(
				SELECT DISTINCT TimeIntervalString,BillDate,CategoryName,Rate
				FROM #Temp  
			)A
			PIVOT
			(
				SUM(Rate)
				FOR TimeIntervalString in (' + @Cols + ')
			)pvt
			WHERE BillDate IS NOT NULL'
			END
		ELSE
			BEGIN
			SET @query =	'SELECT DISTINCT BillDate,CategoryName,' + @cols + ' FROM
			(
				SELECT DISTINCT TimeIntervalString,BillDate,CategoryName,Quantity
				FROM #Temp  
			)A
			PIVOT
			(
				SUM(Quantity)
				FOR TimeIntervalString in (' + @Cols + ')
			)pvt
			WHERE BillDate IS NOT NULL'
			END
		EXEC(@query)
		DROP Table #Temp
	
END



GO
/****** Object:  StoredProcedure [dbo].[posSaleMasterTimeIntervalWiseItemSale_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSaleMasterTimeIntervalWiseItemSale_SelectAll]
	 @FromBillDate datetime
	 ,@ToBillDate datetime 
	 ,@CategoryMasterId smallint = null
	 ,@TimeInterval smallint 
	 ,@AmountQuantity bit
	 ,@linktoBusinessMasterId int
AS
BEGIN
	
	Declare @TimeSlot smallint = 0,@Date smalldatetime,@cols as varchar(max),@query as varchar(max)

	CREATE TABLE #Temp(ID int identity(1,1),TimeInterval time(7),TimeIntervalString Varchar(100),BillDate Date,CategoryName varchar(50),ItemName varchar(50),Rate money,Quantity int)

	SET @Date = '2016-03-10 00:00:00'

	WHILE(@TimeSlot < 24)
	BEGIN
		INSERT INTO #Temp(TimeInterval,TimeIntervalString,BillDate,CategoryName,ItemName,Rate,Quantity)	
		SELECT 
			Convert(varchar(8),@Date,108),Convert(varchar,DATEPART(hh,Convert(varchar(8),@Date,108))) + ' - ' + Convert(Varchar,DATEPART(hh,CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, 1,@Date)),108)))
			,Convert(varchar(8),BillDateTime,112),CategoryName,SIT.ItemName,SUM(((SIT.Quantity * SIT.Rate) + SIT.Tax1 + SIT.Tax2 + SIT.Tax3 + SIT.Tax4 + SIT.Tax5)-SIT.DiscountAmount),SUM(Quantity)
		FROM 
			posSalesMaster SM
		JOIN
			posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId
		JOIN
			posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
		JOIN
			posCategoryMaster CM ON CM.CategoryMasterId = IM.linktoCategoryMasterId
		WHERE
			CONVERT(VARCHAR(8),DATEADD(MINUTE, -1,BillDateTime),108) BETWEEN CONVERT(VARCHAR(8), @Date,108) AND CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, -1,@Date)),108)		
			AND CONVERT(VARCHAR(8),BillDateTime,112) BETWEEN CONVERT(VARCHAR(8),@FromBillDate,112) AND CONVERT(VARCHAR(8),@ToBillDate,112)
			AND CM.CategoryMasterId =ISNULL(@CategoryMasterId,CM.CategoryMasterId)
			AND SM.SalesMasterId=SIT.linktoSalesMasterId
			AND SM.linktoBusinessMasterId=@linktoBusinessMasterId
		GROUP BY
			Convert(varchar(8),BillDateTime,112),CategoryName,SIT.ItemName

		SET @Date = DATEADD(hour,@TimeInterval,@Date)
		SET @TimeSlot = @TimeSlot + @TimeInterval
	END



	SET @TimeSlot = 0
	SET @Date = '2016-03-10 00:00:00'

	WHILE(@TimeSlot < 24)
	BEGIN

		IF NOT EXISTS(SELECT ID FROM #Temp WHERE TimeInterval = Convert(varchar(8),@Date,108))
		BEGIN
			INSERT INTO #Temp(TimeInterval,TimeIntervalString)	
			values(Convert(varchar(8),@Date,108),Convert(varchar,DATEPART(hh,Convert(varchar(8),@Date,108))) + ' - ' + Convert(Varchar,DATEPART(hh,CONVERT(VARCHAR(8),DATEADD(HOUR,@TimeInterval,DATEADD(MINUTE, 1,@Date)),108))))
		END

		SET @Date = DATEADD(hour,@TimeInterval,@Date)
		SET @TimeSlot = @TimeSlot + @TimeInterval
	END



	SELECT @cols = STUFF((SELECT ',' + '[' + TimeIntervalString + ']'
				FROM #Temp
				GROUP BY TimeInterval,TimeIntervalString
				ORDER BY TimeInterval,TimeIntervalString	
				FOR XML PATH(''), TYPE
				).value('.', 'NVARCHAR(MAX)') 
		   ,1,1,'')

	IF @AmountQuantity = 1
	BEGIN
		SET @query =	'SELECT DISTINCT BillDate,CategoryName,ItemName,' + @cols + ' FROM
					(
						SELECT DISTINCT TimeIntervalString,BillDate,CategoryName,ItemName,Rate
						FROM #Temp  
					)A
					PIVOT
					(
						SUM(Rate)
						FOR TimeIntervalString in (' + @Cols + ')
					)pvt
					WHERE BillDate IS NOT NULL'
	END
	ELSE
	BEGIN
		SET @query =	'SELECT DISTINCT BillDate,CategoryName,ItemName,' + @cols + ' FROM
					(
						SELECT DISTINCT TimeIntervalString,BillDate,CategoryName,ItemName,Quantity
						FROM #Temp  
					)A
					PIVOT
					(
						SUM(Quantity)
						FOR TimeIntervalString in (' + @Cols + ')
					)pvt
					WHERE BillDate IS NOT NULL'
	END

	EXEC(@query)

	DROP Table #Temp
END



GO
/****** Object:  StoredProcedure [dbo].[posSaleMasterUserPaymentTypeWiseReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posSaleMasterUserPaymentTypeWiseReport_SelectAll '20160101','20160331',0,0,1
CREATE PROCEDURE [dbo].[posSaleMasterUserPaymentTypeWiseReport_SelectAll]
	@FromDate DateTime
	,@ToDate DateTime
	,@linktoUserMasterIdCreatedBy int
	,@linktoCounterMasterId int
	,@linktoBusinessMasterId smallint
	
AS
BEGIN
	SELECT 
	UM.Username, Convert (date,SM.BillDateTime) BillDateTime, 
	PM.PaymentType,SUM(SPT.AmountPaid) AmountPaid,SUM(SM.TotalAmount) TotalAmount ,SUM(DiscountAmount) DiscountAmount,SUM(TotalTax) TotalTax,SUM(SM.NetAmount) NetAmount
FROM 
	posSalesMaster SM, posUserMaster UM, posSalesPaymentTran SPT, posPaymentTypeMaster PM
WHERE 
	UM.UserMasterId = SM.linktoUserMasterIdCreatedBy AND SPT.linktoSalesMasterId = SM.SalesMasterId
	AND SPT.linktoPaymentTypeMasterId = PM.PaymentTypeMasterId
	
	AND CONVERT(VARCHAR(8),SM.BillDateTime,112) BETWEEN CONVERT(VARCHAR(8),@FromDate,112) AND CONVERT(VARCHAR(8),@ToDate,112)
	AND SM.linktoUserMasterIdCreatedBy = CASE WHEN @linktoUserMasterIdCreatedBy = 0 THEN SM.linktoUserMasterIdCreatedBy ELSE @linktoUserMasterIdCreatedBy END
	AND SM.linktoBusinessMasterId=@linktoBusinessMasterId
	
GROUP BY UM.Username,PM.PaymentType, Convert (date,SM.BillDateTime) 
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesItemModifierTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesItemModifierTran_Delete]
	 @linktoSalesItemTranId bigint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posSalesItemModifierTran
	WHERE
		linktoSalesItemTranId = @linktoSalesItemTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesItemModifierTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesItemModifierTran_Insert]
	 @SalesItemModifierTranId bigint OUTPUT
	,@linktoSalesItemTranId bigint
	,@linktoItemMasterId int
	,@Rate money
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posSalesItemModifierTran
	(
		 linktoSalesItemTranId
		,linktoItemMasterId
		,Rate
	)
	VALUES
	(
		 @linktoSalesItemTranId
		,@linktoItemMasterId
		,@Rate
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SalesItemModifierTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesItemTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesItemTran_Delete]
	 @linktoSalesMasterId bigint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posSalesItemTran
	WHERE
		linktoSalesMasterId = @linktoSalesMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesItemTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesItemTran_Insert]
	 @SalesItemTranId bigint OUTPUT
	,@linktoSalesMasterId bigint
	,@linktoItemMasterId int	
	,@ItemCode varchar(20)
	,@ItemName varchar(50)
	,@Quantity smallint
	,@Rate money
	,@DiscountPercentage numeric(5, 2)
	,@DiscountAmount money
	,@Tax money	
	,@ItemRemark varchar(250) = NULL
	,@linktoOrderStatusMasterId smallint = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@ItemPoint smallint
	,@DeductedPoint smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @linktoUnitMasterId smallint,@IsRateTaxInclusive bit,@PurchaseRate money

	SELECT 
		@linktoUnitMasterId = IM.linktoUnitMasterId,@IsRateTaxInclusive = IRT.IsRateTaxInclusive,@PurchaseRate = IRT.PurchaseRate
	FROM 
		posItemMaster IM
	JOIN 
		posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
	WHERE 
		ItemMasterId = @linktoItemMasterId

	INSERT INTO posSalesItemTran
	(
		 linktoSalesMasterId
		,linktoItemMasterId
		,linktoUnitMasterId
		,ItemCode
		,ItemName
		,Quantity
		,Rate
		,DiscountPercentage
		,DiscountAmount
		,Tax1
		,IsRateTaxInclusive
		,PurchaseRate
		,ItemPoint
		,DeductedPoint
		,ItemRemark
		,linktoOrderStatusMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoSalesMasterId
		,@linktoItemMasterId
		,@linktoUnitMasterId
		,@ItemCode
		,@ItemName
		,@Quantity
		,@Rate
		,@DiscountPercentage
		,@DiscountAmount
		,@Tax
		,@IsRateTaxInclusive
		,@PurchaseRate
		,@ItemPoint
		,@DeductedPoint
		,@ItemRemark
		,@linktoOrderStatusMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SalesItemTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesItemTranBillPrint_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesItemTranBillPrint_SelectAll]

	@linktoSalesMasterId smallint 
AS
BEGIN

	SET NOCOUNT ON

	SELECT  SIT.linktoItemMasterId,sit.Quantity,0 As Item,(sit.Quantity * SIT.Rate) AS Rate,IM.ItemName,
		CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.PrinterName 
		ELSE (select PrinterName from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PrinterName	
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Copy 
		ELSE (select Copy from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END NumberOfCopy	
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Size 
		ELSE (select size from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PageSize	

from posSalesItemTran SIT
join posItemMaster IM ON IM.ItemMasterId=SIT.linktoItemMasterId
join posSalesMaster SM ON SM.SalesMasterId = SIT.linktoSalesMasterId
left join posCounterPrinterTran CPT ON CPT.linktoCounterMasterId = SM.linktoCounterMasterId AND CPT.linktoCategoryMasterId = IM.linktoCategoryMasterId AND CPT.IsReceiptPrinter = 0
where SIT.linktoSalesMasterId=@linktoSalesMasterId 
UNION ALL
SELECT SIT.linktoItemMasterId,0 As Qty,1 As Modifier,(sit.Quantity * SIT.Rate) AS Rate
 ,IM.ItemName AS modifier,
 CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.PrinterName 
		ELSE (select PrinterName from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PrinterName	
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Copy 
		ELSE (select Copy from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END NumberOfCopy	
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Size 
		ELSE (select size from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PageSize	

FROM 
 posSalesItemModifierTran SIMT
 JOIN posSalesItemTran SIT ON SIT.SalesItemTranId=SIMT.linktoSalesItemTranId 
 JOIN posItemMaster IM ON IM.ItemMasterId=SIMT.linktoItemMasterId 
 join posSalesMaster SM ON SM.SalesMasterId = SIT.linktoSalesMasterId
 join posItemMaster ITEM ON ITEM.ItemMasterId=SIT.linktoItemMasterId
 left join posCounterPrinterTran CPT ON CPT.linktoCounterMasterId = SM.linktoCounterMasterId AND CPT.linktoCategoryMasterId = ITEM.linktoCategoryMasterId AND CPT.IsReceiptPrinter = 0
 WHERE SIT.linktoSalesMasterId=@linktoSalesMasterId
 UNION ALL
 SELECT  SIT.linktoItemMasterId,0 As Quantity,2 As Item,(sit.Quantity * SIT.Rate) AS Rate
		,ISNULL(SIT.ItemRemark,''),
		CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.PrinterName 
		ELSE (select PrinterName from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PrinterName
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Copy 
		ELSE (select Copy from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END NumberOfCopy	
		, CASE WHEN CPT.linktoCategoryMasterId > 0 THEN CPT.Size 
		ELSE (select size from posCounterPrinterTran where IsReceiptPrinter = 0 and linktoCounterMasterId = SM.linktoCounterMasterId 
		AND linktoCategoryMasterId = 0) END PageSize	
from posSalesItemTran SIT
join posSalesMaster SM ON SM.SalesMasterId = SIT.linktoSalesMasterId
 join posItemMaster ITEM ON ITEM.ItemMasterId=SIT.linktoItemMasterId
 left join posCounterPrinterTran CPT ON CPT.linktoCounterMasterId = SM.linktoCounterMasterId AND CPT.linktoCategoryMasterId = ITEM.linktoCategoryMasterId AND CPT.IsReceiptPrinter = 0
where SIT.linktoSalesMasterId=@linktoSalesMasterId AND Sit.ItemRemark is not NULL AND SIT.ItemRemark != ''

order by linktoItemMasterId,Item

	RETURN
END










GO
/****** Object:  StoredProcedure [dbo].[posSalesItemTranByItemReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posSalesItemTranByItemReport_SelectAll '20150101','20161212','0,42,95,37,94',1,'0,4,11,6,26'
CREATE PROCEDURE [dbo].[posSalesItemTranByItemReport_SelectAll]
 @FromDate DateTime
,@ToDate DateTime
,@CounterMasterId smallint
,@CategoryMasterIds varchar(1000)
,@linktoBussinessMasterId smallint
AS
BEGIN


	SET NOCOUNT ON


	SELECT 
		CategoryMasterId,CategoryName,ItemName,ItemCode,SUM(Quantity) As Quantity,SUM(TotalRate) As TotalRate,SUM(DiscountAmount) As DiscountAmount,SUM(Tax) As Tax,
		SUM(NetAmount) As NetAmount 
	FROM
	(
		SELECT 
			CM.CategoryMasterId,CM.CategoryName,SIT.ItemName,SIT.ItemCode,SIT.Quantity,(SIT.Quantity * SIT.Rate) As TotalRate,SIT.DiscountAmount,
			(SIT.Tax1 + SIT.Tax2 + SIT.Tax3 + SIT.Tax4 + SIT.Tax5) AS Tax,
			(((SIT.Quantity * SIT.Rate) + SIT.Tax1 + SIT.Tax2 + SIT.Tax3 + SIT.Tax4 + SIT.Tax5) - SIT.DiscountAmount) As NetAmount
		FROM
			posSalesMaster SM
		JOIN 
			posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId
		JOIN
			posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
		JOIN
			posCategoryMaster CM ON CM.CategoryMasterId = IM.linktoCategoryMasterId
		WHERE
			SM.linktoCounterMasterId = CASE WHEN  @CounterMasterId = 0 THEN SM.linktoCounterMasterId ELSE @CounterMasterId END AND
			CM.CategoryMasterId IN (SELECT * FROM dbo.Parse(@CategoryMasterIds, ',')) AND
			CONVERT(Varchar(8),SM.BillDateTime,112) BETWEEN CONVERT(Varchar(8),@FromDate,112)  AND CONVERT(Varchar(8),@ToDate,112) AND
			SM.linktoBusinessMasterId = @linktoBussinessMasterId
	)Temp
	Group by CategoryMasterId,CategoryName,ItemName,ItemCode

	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesItemTranMostSaleItemsChart_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posSalesMasterSaleDailyWeeaklyMonthly_Select 1
CREATE PROCEDURE [dbo].[posSalesItemTranMostSaleItemsChart_SelectAll]

	  @linktoBusinessMasterId smallint	
AS
BEGIN
	SET NOCOUNT ON
		SELECT
			  Top 10 Count(linktoItemMasterId) linktoItemMasterId,ItemName 
		FROM 
			 posSalesItemTran  SIT
		JOIN posSalesMaster SM ON SM.linktoBusinessMasterId=@linktoBusinessMasterId AND SM.SalesMasterId= SIT.linktoSalesMasterId
		AND DATEPART(MONTH,BillDateTime)=DATEPART(MONTH,GETDATE())
		GROUP BY ItemName
		ORDER BY linktoItemMasterId DESC
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesItemTranProfitORLoss_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesItemTranProfitORLoss_SelectAll] 
	@FromDate DateTime,
	@ToDate DateTime,
	@linktoCounterMasterId smallint
	,@linktoBusinessMasterId int
AS
BEGIN
	SELECT 
	BillDateTime,ItemName,ItemCode,SUM(PurchaseRate) As PurchaseRate,SUM(Quantity) As Quantity,SUM(Rate) As Rate,
	SUM(TotalRate) As TotalRate, SUM(TotalCost) As TotalCost,SUM(ProfitORLoss) As ProfitORLoss 
	FROM
	(
		SELECT DISTINCT
			CONVERT(Varchar(10),BillDateTime,105) BillDateTime,SIT.ItemName,SIT.ItemCode,IRT.PurchaseRate,
			SIT.Quantity,SIT.Rate,(SIT.Quantity * SIT.Rate) As TotalRate,(SIT.Quantity * IRT.PurchaseRate) As TotalCost
			,((SIT.Quantity * SIT.Rate) - (SIT.Quantity * IRT.PurchaseRate)) AS ProfitORLoss
		FROM 
			posSalesMaster SM
		JOIN 
			posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId
		JOIN
			posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
		JOIN
			posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
		WHERE
			CONVERT(Varchar(8),BillDateTime,112) BETWEEN CONVERT(Varchar(8),@FromDate,112) AND CONVERT(Varchar(8),@ToDate,112)
			AND linktoCounterMasterId = CASE WHEN @linktoCounterMasterId = 0 THEN linktoCounterMasterId ELSE @linktoCounterMasterId END
			AND SM.SalesMasterId=SIT.linktoSalesMasterId
			AND SM.linktoBusinessMasterId=@linktoBusinessMasterId
	)Temp
	GROUP BY BillDateTime,ItemName,ItemCode
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesItemTranWithAllTax_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesItemTranWithAllTax_Insert] 
	 @SalesItemTranId bigint OUTPUT
	,@linktoSalesMasterId bigint
	,@linktoItemMasterId int		
	,@ItemName varchar(50)
	,@Quantity smallint
	,@Rate money
	,@DiscountPercentage numeric(5, 2)
	,@DiscountAmount money
	,@Tax1 money	
	,@Tax2 money	
	,@Tax3 money	
	,@Tax4 money	
	,@Tax5 money	
	,@IsRateTaxInclusive bit
	,@AddLessAmount money
	,@ItemRemark varchar(250) = NULL
	,@linktoOrderStatusMasterId smallint = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@ItemPoint smallint
	,@DeductedPoint smallint
	,@Status smallint OUTPUT
AS
BEGIN
	Declare @linktoUnitMasterId smallint,@PurchaseRate money,@ItemCode Varchar(20)

	SELECT 
		@linktoUnitMasterId = IM.linktoUnitMasterId,@PurchaseRate = IRT.PurchaseRate,
		@ItemCode = IM.ItemCode
	FROM 
		posItemMaster IM
	JOIN 
		posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
	WHERE 
		ItemMasterId = @linktoItemMasterId

	INSERT INTO posSalesItemTran
	(
		 linktoSalesMasterId
		,linktoItemMasterId
		,linktoUnitMasterId
		,ItemCode
		,ItemName
		,Quantity
		,Rate
		,DiscountPercentage
		,DiscountAmount
		,Tax1
		,Tax2
		,Tax3
		,Tax4
		,Tax5
		,AddLessAmount
		,IsRateTaxInclusive
		,PurchaseRate
		,ItemPoint
		,DeductedPoint
		,ItemRemark
		,linktoOrderStatusMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoSalesMasterId
		,@linktoItemMasterId
		,@linktoUnitMasterId
		,@ItemCode
		,@ItemName
		,@Quantity
		,@Rate
		,@DiscountPercentage
		,@DiscountAmount
		,@Tax1
		,@Tax2
		,@Tax3
		,@Tax4
		,@Tax5
		,@AddLessAmount
		,@IsRateTaxInclusive
		,@PurchaseRate
		,@ItemPoint
		,@DeductedPoint
		,@ItemRemark
		,@linktoOrderStatusMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SalesItemTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesMaster_Insert]
	 @SalesMasterId bigint OUTPUT
	,@BillNumber varchar(50) = ''
	,@BillDateTime datetime = NULL
	,@linktoCounterMasterId smallint
	,@linktoTableMasterIds varchar(50)
	,@linktoWaiterMasterId int = NULL
	,@linktoWaiterMasterIdCaptain int = NULL
	,@linktoCustomerMasterId int = NULL
	,@linktoOrderTypeMasterId smallint
	,@linktoOrderStatusMasterId smallint = NULL
	,@NoOfAdults smallint = NULL
	,@NoOfChildren smallint = NULL
	,@RateIndex smallint = NULL
	,@TotalAmount money
	,@TotalTax money
	,@DiscountPercentage money
	,@DiscountAmount money
	,@ExtraAmount money
	,@TotalItemDiscount money
	,@TotalItemTax money
	,@NetAmount money
	,@PaidAmount money
	,@BalanceAmount money
	,@Remark varchar(250) = NULL
	,@IsComplimentary bit
	,@TotalItemPoint smallint
	,@TotalDeductedPoint smallint
	,@linktoBusinessMasterId smallint
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@IsDeleted bit
	,@Rounding money
	,@OfferCode varchar(50) = NULL
	,@OrderMasterIds varchar(50) = NULL
	,@linktoOfferMasterId int = NULL
	,@linktoSourceMasterId smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT SalesMasterId FROM posSalesMaster WHERE BillNumber = @BillNumber AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SELECT @SalesMasterId = SalesMasterId FROM posSalesMaster WHERE BillNumber = @BillNumber AND linktoBusinessMasterId = @linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END

	IF (@BillNumber = '')
	BEGIN
	   EXEC posSalesMasterBillNumberAutoIncrement_Select @linktoCounterMasterId, @IsComplimentary, @BillDateTime, @BillNumber OUTPUT
	END

	IF EXISTS(SELECT SalesMasterId FROM posSalesMaster WHERE BillNumber = @BillNumber)
	BEGIN
		SELECT @SalesMasterId = SalesMasterId FROM posSalesMaster WHERE BillNumber = @BillNumber
		SET @Status = -2
		RETURN
	END
	INSERT INTO posSalesMaster
	(
		 BillNumber
		,BillDateTime
		,linktoCounterMasterId
		,linktoTableMasterIds
		,linktoWaiterMasterId
		,linktoWaiterMasterIdCaptain
		,linktoCustomerMasterId
		,linktoOrderTypeMasterId
		,linktoOrderStatusMasterId
		,NoOfAdults
		,NoOfChildren
		,RateIndex
		,TotalAmount
		,TotalTax
		,DiscountPercentage
		,DiscountAmount
		,ExtraAmount
		,TotalItemDiscount
		,TotalItemTax
		,Rounding
		,NetAmount
		,PaidAmount
		,BalanceAmount
		,Remark
		,Iscomplimentary
		,TotalItemPoint
		,TotalDeductedPoint
		,linktoBusinessMasterId
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,OfferCode
		,linktoOfferMasterId
	)
	VALUES
	(
		 @BillNumber
		,@BillDateTime
		,@linktoCounterMasterId
		,@linktoTableMasterIds
		,@linktoWaiterMasterId
		,@linktoWaiterMasterIdCaptain
		,@linktoCustomerMasterId
		,@linktoOrderTypeMasterId
		,@linktoOrderStatusMasterId
		,@NoOfAdults
		,@NoOfChildren
		,@RateIndex
		,@TotalAmount
		,@TotalTax
		,@DiscountPercentage
		,@DiscountAmount
		,@ExtraAmount
		,@TotalItemDiscount
		,@TotalItemTax
		,@Rounding
		,@NetAmount
		,@PaidAmount
		,@BalanceAmount
		,@Remark
		,@IsComplimentary
		,@TotalItemPoint
		,@TotalDeductedPoint
		,@linktoBusinessMasterId
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@OfferCode
		,@linktoOfferMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SalesMasterId = @@IDENTITY
		SET @Status = 0
	

	IF (@OfferCode IS NOT NULL AND @linktoOfferMasterId > 0)
	BEGIN
		
		DECLARE
		@isPercentage bit = (SELECT IsDiscountPercentage FROM posOfferMaster WHERE OfferMasterId = @linktoOfferMasterId)
		,@Discount money = (SELECT Discount FROM posOfferMaster WHERE OfferMasterId = @linktoOfferMasterId)
		,@DiscountTemp money = NULL

			IF @isPercentage=1
			BEGIN
	
			UPDATE q 
			SET 
				q.DiscountPercentage= @Discount,
				@DiscountTemp= ((SELECT TotalAmount FROM posOrderMaster OM WHERE OM.OrderMasterId=q.OrderMasterId)*@Discount)/100,
				q.Discount = @DiscountTemp,
				q.NetAmount = (SELECT TotalAmount FROM posOrderMaster OM WHERE OM.OrderMasterId=q.OrderMasterId) - @DiscountTemp
				FROM posOrderMaster q
			WHERE
				q.OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ','))
			END
		ELSE
			BEGIN
	
			-- if discount is in rupees then discount set on the max(totalamount) on the top if raw are greater than 1 
			UPDATE q
			SET 
				q.DiscountPercentage= 0.0,
				q.Discount = @Discount,
				q.NetAmount = (SELECT TotalAmount FROM posOrderMaster OM WHERE OM.OrderMasterId=q.OrderMasterId) - @Discount
				FROM posOrderMaster q
			WHERE
				q.OrderMasterId = (SELECT TOP 1 OrderMasterId FROM posOrderMaster WHERE  TotalAmount = ( SELECT MAX(TotalAmount) FROM posOrderMaster WHERE OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ','))))


			-- in remain raw discount is set with 0
			UPDATE q
			SET 
				q.DiscountPercentage= 0.0,
				q.Discount = 0.0,
				q.NetAmount = (SELECT TotalAmount FROM posOrderMaster OM WHERE OM.OrderMasterId=q.OrderMasterId) 
				FROM posOrderMaster q
			WHERE
				q.OrderMasterId <> (SELECT TOP 1 OrderMasterId FROM posOrderMaster WHERE  TotalAmount = ( SELECT MAX(TotalAmount) FROM posOrderMaster WHERE OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ','))))
				AND q.OrderMasterId IN (SELECT * FROM dbo.Parse(@OrderMasterIds, ','))
			END

		
			INSERT INTO posOfferCodesTran
			(
				linktoOfferMasterId
				,OfferCode
				,linktoCustomerMasterId
				,CreateDateTime
				,linktoUserMasterIdCreatedBy
				,RedeemDateTime
				,linktoUserMasterIdRedeemedBy			
				,linktoSourceMasterId
			)
			VALUES
			(
				@linktoOfferMasterId
				,@OfferCode
				,@linktoCustomerMasterId
				,@CreateDateTime
				,@linktoUserMasterIdCreatedBy
				,@CreateDateTime
				,@linktoUserMasterIdCreatedBy
				,@linktoSourceMasterId
			)		 
	END


	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posSalesMaster_Select 1
CREATE PROCEDURE [dbo].[posSalesMaster_Select]
	 @SalesMasterId bigint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posSalesMaster.*
		,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS Counter
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS Waiter
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer
		,(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = linktoOrderTypeMasterId) AS OrderType		
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterIdCaptain) AS CaptainName	
		,STUFF((SELECT distinct ', ' + TableName FROM posTableMaster WHERE TableMasterId IN (SELECT parseValue FROM dbo.Parse(linktoTableMasterIds,','))
			FOR XML PATH(''), TYPE).value('.', 'VARCHAR(MAX)'), 1, 1, '') As TableName
		,(SELECT CASE WHEN RateCaption IS NULL OR RateCaption = '' THEN RateName ELSE RateCaption END FROM posRateCaptionMaster RCM WHERE RCM.RateIndex = posSalesMaster.RateIndex AND RCM.linktoBusinessMasterId = posSalesMaster.linktoBusinessMasterId) AS RateName	
	FROM
		 posSalesMaster
	WHERE
		SalesMasterId = @SalesMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posSalesMaster_SelectAll '',null,'20160401','20160401',1
CREATE PROCEDURE [dbo].[posSalesMaster_SelectAll]
	
	  @BillNumber varchar(20)= null
	  ,@linktoTableMasterIds varchar(50)= null
	  ,@BillDateTime date = NULL
	  ,@ToBillDateTime date = NULL
	  ,@linktoBusinessMasterId smallint	
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posSalesMaster.*
		,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS Counter		
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS Waiter	
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterIdCaptain) AS Captain		
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer		
		,(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = linktoOrderTypeMasterId) AS OrderType		
		,(SELECT StatusName FROM posOrderStatusMaster WHERE OrderStatusMasterId = linktoOrderStatusMasterId) AS OrderStatus		
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
		
		,STUFF((SELECT distinct ', ' + TableName  
			from posTableMaster
			WHERE TableMasterId in (select parsevalue from dbo.Parse(linktoTableMasterIds,','))
        FOR XML PATH(''), TYPE   
        ).value('.', 'VARCHAR(MAX)')   
        ,1,1,'') As TableName
		,(SELECT CustomerInvoiceTranId FROM posCustomerInvoiceTran CIT JOIN posCustomerInvoiceMaster CIM ON CIM.CustomerInvoiceMasterId = CIT.linktoCustomerInvoiceMasterId 
			WHERE linktoSalesMasterId = SalesMasterId AND CIM.IsDeleted = 0
		) As CustomerInvoiceTranId
		,STUFF((SELECT distinct ', ' + OrderNumber  
			from posOrderMaster
			WHERE linktoSalesMasterId = SalesMasterId
        FOR XML PATH(''), TYPE   
        ).value('.', 'VARCHAR(MAX)')   
        ,1,1,'') As OrderNumber
			,STUFF((SELECT distinct ', ' + CONVERT(Varchar,OrderMasterId)  
			from posOrderMaster
			WHERE linktoSalesMasterId = SalesMasterId
        FOR XML PATH(''), TYPE   
        ).value('.', 'VARCHAR(MAX)')   
        ,1,1,'') As OrderMasterIds
	FROM
		 posSalesMaster
	WHERE
		BillNumber like @BillNumber + '%'
	 	AND linktoTableMasterIds=ISNULL(@linktoTableMasterIds,linktoTableMasterIds)
	   AND CONVERT(varchar(8), BillDateTime, 112) BETWEEN  CONVERT(varchar(8), @BillDateTime, 112) AND CONVERT(varchar(8), @ToBillDateTime, 112)
	   AND linktoBusinessMasterId=@linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesMaster_Update]
	 @SalesMasterId bigint		
	,@BillDateTime Datetime
	,@linktoCounterMasterId smallint
	,@linktoTableMasterIds varchar(50)
	,@linktoWaiterMasterId int = NULL
	,@linktoWaiterMasterIdCaptain int = NULL
	,@linktoCustomerMasterId int = NULL
	,@linktoOrderTypeMasterId smallint
	,@linktoOrderStatusMasterId smallint = NULL
	,@NoOfAdults smallint = NULL
	,@NoOfChildren smallint = NULL
	,@TotalAmount money
	,@TotalTax money
	,@DiscountPercentage money
	,@DiscountAmount money
	,@ExtraAmount money
	,@TotalItemDiscount money
	,@TotalItemTax money
	,@Rounding money
	,@NetAmount money
	,@PaidAmount money
	,@BalanceAmount money
	,@Remark varchar(250) = NULL
	,@RateIndex smallint = NULL
	,@IsComplimentary bit
	,@TotalItemPoint smallint
	,@TotalDeductedPoint smallint
	,@linktoBusinessMasterId smallint
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	
	UPDATE posSalesMaster
	SET		 
		 BillDateTime = @BillDateTime
		,linktoCounterMasterId = @linktoCounterMasterId
		,linktoTableMasterIds = @linktoTableMasterIds
		,linktoWaiterMasterId = @linktoWaiterMasterId
		,linktoWaiterMasterIdCaptain = @linktoWaiterMasterIdCaptain
		,linktoCustomerMasterId = @linktoCustomerMasterId
		,linktoOrderTypeMasterId = @linktoOrderTypeMasterId
		,linktoOrderStatusMasterId = @linktoOrderStatusMasterId
		,NoOfAdults = @NoOfAdults
		,NoOfChildren = @NoOfChildren
		,TotalAmount = @TotalAmount
		,TotalTax = @TotalTax
		,DiscountPercentage = @DiscountPercentage
		,DiscountAmount = @DiscountAmount
		,ExtraAmount = @ExtraAmount
		,TotalItemDiscount = @TotalItemDiscount
		,TotalItemTax = @TotalItemTax
		,Rounding = @Rounding
		,NetAmount = @NetAmount
		,PaidAmount = @PaidAmount
		,BalanceAmount = @BalanceAmount
		,Remark = @Remark
		,RateIndex = @RateIndex
		,Iscomplimentary = @IsComplimentary
		,TotalItemPoint = @TotalItemPoint
		,TotalDeductedPoint = @TotalDeductedPoint
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		SalesMasterId = @SalesMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterBillNumber_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesMasterBillNumber_SelectAll]

	 @linktoBusinessMasterId smallint	
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 SalesMasterId
		,BillNumber
	FROM
		 posSalesMaster
	WHERE	
		linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY BillNumber

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterBillNumberAutoIncrement_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesMasterBillNumberAutoIncrement_Select] 
	 @linktoCounterMasterId smallint
	,@IsComplimentary bit
	,@SystemDateTime datetime
	,@BillNumber varchar(20) OUTPUT
AS
BEGIN
	
	DECLARE @Type varchar(10), @Numbering varchar(10), @prefix varchar(10), @digits smallint

	SELECT @Type = value FROM posCounterSettingValueTran
	WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCounterSettingMasterId = 19

	SELECT @Numbering = value FROM posCounterSettingValueTran
	WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCounterSettingMasterId = 20

	SELECT @prefix = value FROM posCounterSettingValueTran
	WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCounterSettingMasterId = (CASE WHEN @IsComplimentary = 0 THEN 21 ELSE 23 END)

	SELECT @digits = value FROM posCounterSettingValueTran
	WHERE linktoCounterMasterId = @linktoCounterMasterId AND linktoCounterSettingMasterId = (CASE WHEN @IsComplimentary = 0 THEN 22 ELSE 24 END)

	IF (@type = 'Automatic')
	BEGIN
	   IF (@Numbering = 'Yearly')
		  SELECT @BillNumber = ISNULL((MAX(CONVERT(int, SUBSTRING(BillNumber, PATINDEX('%[0-9]%', BillNumber), LEN(BillNumber)))) + 1), 1) FROM posSalesMaster WHERE linktoCounterMasterId = @linktoCounterMasterId AND YEAR(BillDateTime) = YEAR(@SystemDateTime)
	   ELSE IF(@Numbering = 'Monthly')
		  SELECT @BillNumber = ISNULL((MAX(CONVERT(int, SUBSTRING(BillNumber, PATINDEX('%[0-9]%', BillNumber), LEN(BillNumber)))) + 1), 1) FROM posSalesMaster WHERE linktoCounterMasterId = @linktoCounterMasterId AND YEAR(BillDateTime) = YEAR(@SystemDateTime) AND MONTH(BillDateTime) = MONTH(@SystemDateTime)
	   ELSE
		  SELECT @BillNumber = ISNULL((MAX(CONVERT(int, SUBSTRING(BillNumber, PATINDEX('%[0-9]%', BillNumber), LEN(BillNumber)))) + 1), 1) FROM posSalesMaster WHERE linktoCounterMasterId = @linktoCounterMasterId AND CONVERT(varchar(8), BillDateTime, 112) = CONVERT(varchar(8), @SystemDateTime, 112)

	   SET @BillNumber = @prefix + RIGHT('00000000000000000000' + @BillNumber, @digits)
	END
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterByHomeDeliveryOrders_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 
CREATE PROCEDURE  [dbo].[posSalesMasterByHomeDeliveryOrders_Update]
	 @OrderMasterIds varchar(1000)
	 ,@linktoOrderStatusMasterId smallint
	 ,@linktoWaiterMasterId smallint
	 ,@UpdateDateTime datetime
	 ,@linktoUserMasterIdUpdatedBy smallint
	 ,@Status smallint OUTPUT

AS
BEGIN 
	SET NOCOUNT ON;

      UPDATE
			posSalesMaster 
	  SET
			linktoOrderStatusMasterId = @linktoOrderStatusMasterId
			,linktoWaiterMasterId = @linktoWaiterMasterId
			,UpdateDateTime = @UpdateDateTime
			,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		SalesMasterId 
			IN(SELECT linktoSalesMasterId from posOrderMaster 
			 WHERE
				OrderMasterId IN(SELECT parsevalue  FROM dbo.Parse(@OrderMasterIds,',')))  

	 IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END
 



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterComparisonReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posSalesMasterComparisonReport_SelectAll '20160301','20160430',1,3,1
CREATE PROCEDURE [dbo].[posSalesMasterComparisonReport_SelectAll]
	@FromDate datetime,
	@ToDate datetime,	
	@linktoBusinessMasterId smallint,
	@Option smallint, -- 1 - Daily, 2 - Day Of Week, 3 - Weekly, 4 - Monthly, 5 - Yearly
	@DayOfWeek as smallint
AS
BEGIN
	CREATE TABLE #Temp(ID smallint identity(1,1),BillDate varchar(30),NetAmount money,SrNo int)

	IF @Option = 3
	BEGIN
		set @FromDate = DATEADD(wk, DATEDIFF(wk,0,@fromDate),-1)
		set @ToDate = dateadd(wk, datediff(wk, 0,@ToDate), 5)
	END

	WHILE(Convert(varchar(8),@FromDate,112) <= Convert(varchar(8),@ToDate,112))
	BEGIN
	
		IF @Option = 1
		BEGIN
			INSERT INTO #Temp(BillDate,NetAmount,SrNo)
			SELECT 
				Convert(varchar(10),@fromDate,105), SUM(SM.NetAmount),DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				posSalesMaster SM			
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 				
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY Convert(varchar(8),BillDateTime,112)

			IF NOT EXISTS(select ID from #temp where BillDate = CONVERT(varchar(10),@FromDate,105))
			BEGIN
				INSERT INTO #Temp(BillDate,NetAmount,SrNo)
				VALUES(CONVERT(varchar(10),@FromDate,105),0,DATEPART(DAYOFYEAR, @FromDate))				
			END

			SET @FromDate = DATEADD(dd,1,@FromDate)
			
		END
		ELSE IF @Option = 2
		BEGIN
			INSERT INTO #Temp(BillDate,NetAmount,SrNo)
			SELECT 
				Convert(varchar(10),@fromDate,105), SUM(SM.NetAmount),DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				posSalesMaster SM			
			WHERE				
				SM.linktoBusinessMasterId = @linktoBusinessMasterId AND DATEPART(weekday,BillDateTime) = @DayOfWeek
			AND 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112)
			GROUP BY Convert(varchar(8),BillDateTime,112)

			IF NOT EXISTS(select ID from #temp where BillDate = CONVERT(varchar(10),@FromDate,105) AND DATEPART(weekday,BillDate) = @DayOfWeek)
			BEGIN
				IF DATEPART(weekday,@FromDate) = @DayOfWeek
				BEGIN
					INSERT INTO #Temp(BillDate,NetAmount,SrNo)
					VALUES(CONVERT(varchar(10),@FromDate,105),0,DATEPART(DAYOFYEAR, @FromDate))				
				END
			END

			SET @FromDate = DATEADD(dd,1,@FromDate)	
		END
		ELSE IF @Option = 3
		BEGIN			
			DECLARE @LastDayOfWeek datetime

			set @LastDayOfWeek = dateadd(wk, datediff(wk, 0,@FromDate), 5)

			INSERT INTO #Temp(BillDate,NetAmount,SrNo)
			SELECT 
				Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105), SM.NetAmount,
				datediff(wk, 0,@FromDate)
			FROM 
				posSalesMaster SM				
			WHERE 
				Convert(varchar(8),BillDateTime,112) BETWEEN Convert(varchar(8),@fromDate,112) AND Convert(varchar(8),@LastDayOfWeek,112)				
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId			

			IF NOT EXISTS(select ID from #temp where BillDate = Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105))
			BEGIN
				INSERT INTO #Temp(BillDate,NetAmount,SrNo)
				VALUES(Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105),0,datediff(wk, 0,@FromDate))
			END

			SET @FromDate = DATEADD(dd,1,@LastDayOfWeek)
		END
		ELSE IF @Option = 4
		BEGIN		

			INSERT INTO #Temp(BillDate,NetAmount,SrNo)
			SELECT 
				DATENAME(MONTH,DATEADD(MONTH,DATEPART(m,@FromDate),0 ) - 1 ), SM.NetAmount,DATEPART(m,@FromDate)
			FROM 
				posSalesMaster SM				
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 				
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId			

			IF NOT EXISTS(select ID from #temp where BillDate =	CONVERT(varchar(10),@FromDate,105))
			BEGIN
				INSERT INTO #Temp(BillDate,NetAmount,SrNo)
				VALUES(DATENAME(MONTH,DATEADD(MONTH,DATEPART(m,@FromDate),0 ) - 1 ),0,DATEPART(m,@FromDate))				
			END
			
			SET @FromDate = DATEADD(dd,1,@FromDate)	
		END
		ELSE IF @Option = 5
		BEGIN		

			INSERT INTO #Temp(BillDate,NetAmount,SrNo)
			SELECT 
				DATEPART(YEAR,@FromDate), SM.NetAmount,DATEPART(YEAR,@FromDate)
			FROM 
				posSalesMaster SM				
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 				
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			

			IF NOT EXISTS(select ID from #temp where BillDate =	CONVERT(varchar(10),@FromDate,105))
			BEGIN
				INSERT INTO #Temp(BillDate,NetAmount,SrNo)
				VALUES(DATEPART(YEAR,@FromDate),0,DATEPART(YEAR,@FromDate))				
			END

			SET @FromDate = DATEADD(dd,1,@FromDate)	
		END				
	END	

	SELECT 
		BillDate,SUM(NetAmount) NetAmount,SrNo
	FROM #Temp
	GROUP BY BillDate,SrNo
	ORDER BY Srno

	DROP TABLE #Temp
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterComparisonReportCategoryWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posSalesMasterComparisonReportCategoryWise_SelectAll '20160301','20160430','5,20',1,5,1
CREATE PROCEDURE [dbo].[posSalesMasterComparisonReportCategoryWise_SelectAll]
	@FromDate datetime,
	@ToDate datetime,
	@CategoryMasterIds varchar(500),
	@linktoBusinessMasterId smallint,
	@Option smallint ,
	@DayOfWeek as smallint 
AS
BEGIN
	CREATE TABLE #Temp(ID smallint identity(1,1),BillDate varchar(30),NetAmount money,Quantity int,CategoryMasterId int,CategoryName varchar(50),SrNo int)

	IF @Option = 3
	BEGIN
		set @FromDate = DATEADD(wk, DATEDIFF(wk,0,@fromDate),-1)
		set @ToDate = dateadd(wk, datediff(wk, 0,@ToDate), 5)
	END

	WHILE(Convert(varchar(8),@FromDate,112) <= Convert(varchar(8),@ToDate,112))
	BEGIN
	
		IF @Option = 1
		BEGIN
			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,CategoryName,SrNo)
			SELECT 
				Convert(varchar(10),@fromDate,105), SUM(SIT.Rate),SUM(SIT.Quantity),IM.linktoCategoryMasterId,CM.CategoryName,DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId	
			JOIN
				posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
			JOIN
				posCategoryMaster CM ON CM.CategoryMasterId = IM.linktoCategoryMasterId	
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 
				AND IM.linktoCategoryMasterId IN (SELECT parsevalue from dbo.Parse(@CategoryMasterIds,','))
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY IM.linktoCategoryMasterId,CM.CategoryName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,SrNo)
			SELECT 
				CONVERT(varchar(10),@FromDate,105),0,0,parsevalue,DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				dbo.Parse(@CategoryMasterIds,',')		
			WHERE 
				parsevalue NOT IN (select CategoryMasterId from #temp where BillDate = CONVERT(varchar(10),@FromDate,105))	

			SET @FromDate = DATEADD(dd,1,@FromDate)
			
		END
		ELSE IF @Option = 2
		BEGIN
			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,CategoryName,SrNo)
			SELECT 
				Convert(varchar(10),@fromDate,105), SUM(SIT.Rate),SUM(SIT.Quantity),IM.linktoCategoryMasterId,CM.CategoryName,DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId		
			JOIN
				posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
			JOIN
				posCategoryMaster CM ON CM.CategoryMasterId = IM.linktoCategoryMasterId	
			WHERE 
				IM.linktoCategoryMasterId IN (SELECT parsevalue from dbo.Parse(@CategoryMasterIds,','))
			AND 
				SM.linktoBusinessMasterId = @linktoBusinessMasterId AND DATEPART(weekday,BillDateTime) = @DayOfWeek
			AND 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112)
			GROUP BY IM.linktoCategoryMasterId,CM.CategoryName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,SrNo)
			SELECT 
				CONVERT(varchar(10),@FromDate,105),0,0,parsevalue,DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				dbo.Parse(@CategoryMasterIds,',')
			WHERE 
				parsevalue NOT IN (select CategoryMasterId from #temp where BillDate = CONVERT(varchar(10),@FromDate,105)
				AND DATEPART(weekday,BillDate) = @DayOfWeek) AND DATEPART(weekday,@FromDate) = @DayOfWeek	
			
			SET @FromDate = DATEADD(dd,1,@FromDate)	
		END
		ELSE IF @Option = 3
		BEGIN			
			DECLARE @LastDayOfWeek datetime

			set @LastDayOfWeek = dateadd(wk, datediff(wk, 0,@FromDate), 5)

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,CategoryName,SrNo)
			SELECT 
				Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105), SUM(SIT.Rate),
				SUM(SIT.Quantity),IM.linktoCategoryMasterId,CM.CategoryName,datediff(wk, 0,@FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId	
			JOIN
				posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
			JOIN
				posCategoryMaster CM ON CM.CategoryMasterId = IM.linktoCategoryMasterId		
			WHERE 
				Convert(varchar(8),BillDateTime,112) BETWEEN Convert(varchar(8),@fromDate,112) AND Convert(varchar(8),@LastDayOfWeek,112)
				AND IM.linktoCategoryMasterId IN (SELECT parsevalue from dbo.Parse(@CategoryMasterIds,','))
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY IM.linktoCategoryMasterId,CM.CategoryName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,SrNo)
			SELECT 
				Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105),0,0,parsevalue,datediff(wk, 0,@FromDate)
			FROM 
				dbo.Parse(@CategoryMasterIds,',')		
			WHERE 
				parsevalue NOT IN (select CategoryMasterId from #temp where BillDate = 
					Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105))		

			SET @FromDate = DATEADD(dd,1,@LastDayOfWeek)
		END
		ELSE IF @Option = 4
		BEGIN		

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,CategoryName,SrNo)
			SELECT 
				DATENAME(MONTH,DATEADD(MONTH,DATEPART(m,@FromDate),0 ) - 1 ), SUM(SIT.Rate),
				SUM(SIT.Quantity),IM.linktoCategoryMasterId,CM.CategoryName,DATEPART(m,@FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId	
			JOIN
				posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
			JOIN
				posCategoryMaster CM ON CM.CategoryMasterId = IM.linktoCategoryMasterId		
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 
				AND IM.linktoCategoryMasterId IN (SELECT parsevalue from dbo.Parse(@CategoryMasterIds,','))
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY IM.linktoCategoryMasterId,CM.CategoryName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,SrNo)
			SELECT 
				DATENAME(MONTH,DATEADD(MONTH,DATEPART(m,@FromDate),0 ) - 1 ),0,0,parsevalue,DATEPART(m,@FromDate)
			FROM 
				dbo.Parse(@CategoryMasterIds,',')		
			WHERE 
				parsevalue NOT IN (select CategoryMasterId from #temp where BillDate =	CONVERT(varchar(10),@FromDate,105))		
			
			SET @FromDate = DATEADD(dd,1,@FromDate)	
		END
		ELSE IF @Option = 5
		BEGIN		

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,CategoryName,SrNo)
			SELECT 
				DATEPART(YEAR,@FromDate), SUM(SIT.Rate),
				SUM(SIT.Quantity),IM.linktoCategoryMasterId,CM.CategoryName,DATEPART(YEAR,@FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId	
			JOIN
				posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
			JOIN
				posCategoryMaster CM ON CM.CategoryMasterId = IM.linktoCategoryMasterId		
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 
				AND  IM.linktoCategoryMasterId IN (SELECT parsevalue from dbo.Parse(@CategoryMasterIds,','))
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY IM.linktoCategoryMasterId,CM.CategoryName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,CategoryMasterId,SrNo)
			SELECT 
				DATEPART(YEAR,@FromDate),0,0,parsevalue,DATEPART(YEAR,@FromDate)
			FROM 
				dbo.Parse(@CategoryMasterIds,',')		
			WHERE 
				parsevalue NOT IN (select CategoryMasterId from #temp where BillDate =	DATEPART(YEAR,@FromDate))	
			
			SET @FromDate = DATEADD(dd,1,@FromDate)		
		END				
	END

	UPDATE 
		t1
	SET 
		t1.CategoryName = COALESCE(t1.CategoryName, t2.CategoryName)
	FROM 
		#Temp t1 
	INNER JOIN 
		#Temp t2 ON t1.CategoryMasterId = t2.CategoryMasterId

	UPDATE 
		t1
	SET 
		t1.CategoryName = COALESCE(t1.CategoryName, CM.CategoryName)
	FROM 
		#Temp t1 
	INNER JOIN 
		posCategoryMaster CM ON CM.CategoryMasterId = t1.CategoryMasterId

	SELECT 
		BillDate,SUM(NetAmount) NetAmount, SUM(Quantity) Quantity, CategoryMasterId,CategoryName
	FROM #Temp
	GROUP BY BillDate,CategoryMasterId,CategoryName,SrNo
	ORDER BY Srno

	DROP TABLE #Temp
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterComparisonReportItemWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posSalesMasterComparisonReportItemWise_SelectAll '20160301','20160430','37,42',1,5,1
CREATE PROCEDURE [dbo].[posSalesMasterComparisonReportItemWise_SelectAll]
	@FromDate datetime,
	@ToDate datetime,
	@ItemMasterIds varchar(500),
	@linktoBusinessMasterId smallint,
	@Option smallint, -- 1 - Daily, 2 - Day Of Week, 3 - Weekly, 4 - Monthly, 5 - Yearly
	@DayOfWeek as smallint
AS
BEGIN
	CREATE TABLE #Temp(ID smallint identity(1,1),BillDate varchar(30),NetAmount money,Quantity int,ItemMasterId int,ItemName varchar(50),SrNo int)

	IF @Option = 3
	BEGIN
		set @FromDate = DATEADD(wk, DATEDIFF(wk,0,@fromDate),-1)
		set @ToDate = dateadd(wk, datediff(wk, 0,@ToDate), 5)
	END

	WHILE(Convert(varchar(8),@FromDate,112) <= Convert(varchar(8),@ToDate,112))
	BEGIN
	
		IF @Option = 1
		BEGIN
			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,ItemName,SrNo)
			SELECT 
				Convert(varchar(10),@fromDate,105), SUM(SIT.Rate),SUM(SIT.Quantity),SIT.linktoItemMasterId,SIT.ItemName,DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId		
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 
				AND SIT.linktoItemMasterId IN (SELECT parsevalue from dbo.Parse(@ItemMasterIds,','))
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY SIT.linktoItemMasterId,SIT.ItemName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,SrNo)
			SELECT 
				CONVERT(varchar(10),@FromDate,105),0,0,parsevalue,DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				dbo.Parse(@ItemMasterIds,',')		
			WHERE 
				parsevalue NOT IN (select ItemMasterId from #temp where BillDate = CONVERT(varchar(10),@FromDate,105))	

			SET @FromDate = DATEADD(dd,1,@FromDate)
			
		END
		ELSE IF @Option = 2
		BEGIN
			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,ItemName,SrNo)
			SELECT 
				Convert(varchar(10),@fromDate,105), SUM(SIT.Rate),SUM(SIT.Quantity),SIT.linktoItemMasterId,SIT.ItemName,DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId		
			WHERE 
				SIT.linktoItemMasterId IN (SELECT parsevalue from dbo.Parse(@ItemMasterIds,','))
			AND 
				SM.linktoBusinessMasterId = @linktoBusinessMasterId AND DATEPART(weekday,BillDateTime) = @DayOfWeek
			AND 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112)
			GROUP BY SIT.linktoItemMasterId,SIT.ItemName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,SrNo)
			SELECT 
				CONVERT(varchar(10),@FromDate,105),0,0,parsevalue,DATEPART(DAYOFYEAR, @FromDate)
			FROM 
				dbo.Parse(@ItemMasterIds,',')
			WHERE 
				parsevalue NOT IN (select ItemMasterId from #temp where BillDate = CONVERT(varchar(10),@FromDate,105)
				AND DATEPART(weekday,BillDate) = @DayOfWeek) AND DATEPART(weekday,@FromDate) = @DayOfWeek	
			
			SET @FromDate = DATEADD(dd,1,@FromDate)	
		END
		ELSE IF @Option = 3
		BEGIN			
			DECLARE @LastDayOfWeek datetime

			set @LastDayOfWeek = dateadd(wk, datediff(wk, 0,@FromDate), 5)

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,ItemName,SrNo)
			SELECT 
				Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105), SUM(SIT.Rate),
				SUM(SIT.Quantity),SIT.linktoItemMasterId,SIT.ItemName,datediff(wk, 0,@FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId		
			WHERE 
				Convert(varchar(8),BillDateTime,112) BETWEEN Convert(varchar(8),@fromDate,112) AND Convert(varchar(8),@LastDayOfWeek,112)
				AND SIT.linktoItemMasterId IN (SELECT parsevalue from dbo.Parse(@ItemMasterIds,','))
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY SIT.linktoItemMasterId,SIT.ItemName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,SrNo)
			SELECT 
				Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105),0,0,parsevalue,datediff(wk, 0,@FromDate)
			FROM 
				dbo.Parse(@ItemMasterIds,',')		
			WHERE 
				parsevalue NOT IN (select ItemMasterId from #temp where BillDate =
					Convert(varchar(10),@FromDate,105) + ' - ' + Convert(varchar(10),@LastDayOfWeek,105))		

			SET @FromDate = DATEADD(dd,1,@LastDayOfWeek)
		END
		ELSE IF @Option = 4
		BEGIN		

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,ItemName,SrNo)
			SELECT 
				DATENAME(MONTH,DATEADD(MONTH,DATEPART(m,@FromDate),0 ) - 1 ), SUM(SIT.Rate),
				SUM(SIT.Quantity),SIT.linktoItemMasterId,SIT.ItemName,DATEPART(m,@FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId		
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 
				AND SIT.linktoItemMasterId IN (SELECT parsevalue from dbo.Parse(@ItemMasterIds,','))
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY SIT.linktoItemMasterId,SIT.ItemName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,SrNo)
			SELECT 
				DATENAME(MONTH,DATEADD(MONTH,DATEPART(m,@FromDate),0 ) - 1 ),0,0,parsevalue,DATEPART(m,@FromDate)
			FROM 
				dbo.Parse(@ItemMasterIds,',')		
			WHERE 
				parsevalue NOT IN (select ItemMasterId from #temp where BillDate =	CONVERT(varchar(10),@FromDate,105))		
			
			SET @FromDate = DATEADD(dd,1,@FromDate)	
		END
		ELSE IF @Option = 5
		BEGIN		

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,ItemName,SrNo)
			SELECT 
				DATEPART(YEAR,@FromDate), SUM(SIT.Rate),
				SUM(SIT.Quantity),SIT.linktoItemMasterId,SIT.ItemName,DATEPART(YEAR,@FromDate)
			FROM 
				posSalesMaster SM
			JOIN 
				posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId		
			WHERE 
				Convert(varchar(8),BillDateTime,112) = Convert(varchar(8),@fromDate,112) 
				AND SIT.linktoItemMasterId IN (SELECT parsevalue from dbo.Parse(@ItemMasterIds,','))
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
			GROUP BY SIT.linktoItemMasterId,SIT.ItemName

			INSERT INTO #Temp(BillDate,NetAmount,Quantity,ItemMasterId,SrNo)
			SELECT 
				DATEPART(YEAR,@FromDate),0,0,parsevalue,DATEPART(YEAR,@FromDate)
			FROM 
				dbo.Parse(@ItemMasterIds,',')		
			WHERE 
				parsevalue NOT IN (select ItemMasterId from #temp where BillDate =	CONVERT(varchar(10),@FromDate,105))	
			
			SET @FromDate = DATEADD(dd,1,@FromDate)		
		END				
	END

	UPDATE 
		t1
	SET 
		t1.ItemName = COALESCE(t1.ItemName, t2.ItemName)
	FROM 
		#Temp t1 
	INNER JOIN 
		#Temp t2 ON t1.ItemMasterId = t2.ItemMasterId

	UPDATE 
		t1
	SET 
		t1.ItemName = COALESCE(t1.ItemName, IM.ItemName)
	FROM 
		#Temp t1 
	INNER JOIN 
		posItemMaster IM ON IM.ItemMasterId = t1.ItemMasterId

	SELECT 
		BillDate,SUM(NetAmount) NetAmount, SUM(Quantity) Quantity, ItemMasterId,ItemName,SrNo
	FROM #Temp
	GROUP BY BillDate,ItemMasterId,ItemName,SrNo
	ORDER BY Srno

	DROP TABLE #Temp
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterCustomerWiseSaleReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 
 -- posSalesMasterCustomerWiseSaleReport_SelectAll '20160114','20161212',null,1,1

CREATE PROCEDURE [dbo].[posSalesMasterCustomerWiseSaleReport_SelectAll] 

      @FromBillDate datetime
	 ,@ToBillDate datetime 
	 ,@linktoCustomerMasterId smallint = null
	 ,@CustomerType smallint 
	 ,@linktoBusinessMasterId smallint
AS
BEGIN
	 
	SET NOCOUNT ON;

	SELECT 
		Convert(varchar(10),SM.BillDateTime,105) BillDate,CM.CustomerName,CM.CustomerType,SUM(SM.TotalAmount) TotalAmount
		,SUM(SM.DiscountAmount) DiscountAmount,SUM(SM.TotalTax) Tax,SUM(SM.NetAmount) NetAmount
	FROM 
		posSalesMaster SM
	JOIN
		posCustomerMaster CM ON CM.CustomerMasterId = SM.linktoCustomerMasterId
	WHERE 
		CM.CustomerType = @CustomerType
		AND SM.linktoCustomerMasterId = ISNULL(@linktoCustomerMasterId,linktoCustomerMasterId)
		AND CONVERT(VARCHAR(8),SM.BillDateTime,112) BETWEEN CONVERT(VARCHAR(8),@FromBillDate,112) AND CONVERT(VARCHAR(8),@ToBillDate,112)
		AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
	GROUP BY 
		Convert(varchar(10),SM.BillDateTime,105),CM.CustomerName,CM.CustomerType
    
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterForBill_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesMasterForBill_Select]
	@SalesMasterId bigint
AS
BEGIN
	SET NOCOUNT ON;
	
	SELECT SalesMasterId
		,BillNumber
		,BillDateTime
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS WaiterName
		,Remark
		,(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = linktoOrderTypeMasterId) AS OrderType
		,STUFF((SELECT distinct ', ' + TableName FROM posTableMaster WHERE TableMasterId IN (SELECT parseValue FROM dbo.Parse(linktoTableMasterIds,','))
			FOR XML PATH(''), TYPE).value('.', 'VARCHAR(MAX)'), 1, 1, '') As TableName
	FROM posSalesMaster
	WHERE SalesMasterId = @SalesMasterId

END

GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterLeastSellingDay_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesMasterLeastSellingDay_Select]
	@CancelOrderStatus smallint,
	@linktoBusinessMasterId smallint,
	@CurrentDate Datetime
AS
BEGIN
		SELECT TOP 1
			DATENAME(dw,BillDateTime) as theDayName ,CONVERT(varchar(10),BillDateTime,105) OrderDate,ISNULL(SUM(NetAmount),0) SellingAmt
		FROM 
			posSalesMaster 
		WHERE 
			ISNULL(linktoOrderStatusMasterId,0) <> @CancelOrderStatus
			AND BillDateTime BETWEEN DATEADD(wk,DATEDIFF(wk,7,@CurrentDate),0) AND DATEADD(wk,DATEDIFF(wk,7,@CurrentDate),6)
			AND linktoBusinessMasterId = @linktoBusinessMasterId
		GROUP BY
			CONVERT(varchar(10),BillDateTime,105),DATENAME(dw,BillDateTime)
		ORDER BY SellingAmt
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterOrderTypeWiseChart_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesMasterOrderTypeWiseChart_SelectAll]
	@FromDate datetime,
	@ToDate datetime,
	@linktoBusinessMasterId int
AS
BEGIN
	DECLARE  @TotalAmount money	
		
	SELECT 
		@TotalAmount = SUM(NetAmount)
	FROM 
		posSalesMaster
	WHERE 
		CONVERT (VARCHAR(8),BillDateTime,112) BETWEEN CONVERT (VARCHAR(8),@FromDate,112) AND CONVERT (VARCHAR(8),@ToDate,112)
		AND linktoBusinessMasterId = @linktoBusinessMasterId

	IF(@TotalAmount = 0)
		SET @TotalAmount = 1

	SELECT 
		*,CONVERT(decimal(10,2),((NetAmount * 100)/@TotalAmount)) SalesInPercentage
	FROM
	(
		SELECT 
			SUM(SM.NetAmount) NetAmount,OT.OrderType
		FROM 
			posSalesMaster SM	
		JOIN 
			posOrderTypeMaster OT ON OT.OrderTypeMasterId = SM.linktoOrderTypeMasterId
		WHERE 
			CONVERT (VARCHAR(8),BillDateTime,112) BETWEEN CONVERT (VARCHAR(8),@FromDate,112) AND CONVERT (VARCHAR(8),@ToDate,112)
			AND linktoBusinessMasterId = @linktoBusinessMasterId
		GROUP BY OT.OrderType
	)TEMP
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterOrderWiseSalesReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--posSalesMasterOrderWiseSalesReport_SelectAll '01-12-2015 00:00:00','31-12-2015 00:00:00'
CREATE PROCEDURE [dbo].[posSalesMasterOrderWiseSalesReport_SelectAll]

 @FromDate Datetime 
,@ToDate DateTime
,@linktoBusinessMasterId int
AS
BEGIN

	SET NOCOUNT ON

		Declare @Cols as varchar(max)
		Declare @Query as varchar(max)
		Declare @columnHeaders as varchar(max)
		Declare @GrandTotalRow as varchar(max)

		
		SELECT @columnHeaders  = COALESCE (@columnHeaders + ',[' + OrderType + ']', '[' + OrderType + ']') FROM    posOrderTypeMaster

		SELECT @Cols = COALESCE (@Cols + 'ISNULL ([' + CAST (OrderType AS VARCHAR) + '],0) + ', 'ISNULL([' + CAST(OrderType AS VARCHAR)+ '],0) + ') FROM	posOrderTypeMaster

		SET @Cols = LEFT (@Cols, LEN (@Cols)-1)
	
		
		-- for reference only
		--(SELECT Icon FROM dpsMenuMaster AS MMS INNER JOIN dpsPageMaster As PAG On MMS.linktoPageMasterId = PAG.PageMasterId Inner Join dpsPageTypeMaster As PTM On PAG.linktoPageTypeMasterId = PTM.PageTypeMasterId Where MMS.MenuMasterId = MM.MenuMasterId) AS Icon,

		SELECT 
			@GrandTotalRow = COALESCE(@GrandTotalRow + ',ISNULL(SUM([' + CAST(OrderType AS VARCHAR)+']),0)', 'ISNULL(SUM([' + CAST(OrderType AS VARCHAR)+']),0)')
			--,(select linktoBusinessMasterId 'as linktoBusinessMasterId' from posSalesMaster as SM, posOrderTypeMaster as OTM where SM.linktoOrderTypeMasterId=OTM.OrderTypeMasterId) as temp
		FROM	
			posOrderTypeMaster as OTM , posSalesMaster as SM
			where 
			SM.linktoBusinessMasterId=@linktoBusinessMasterId and OTM.OrderTypeMasterId=SM.linktoOrderTypeMasterId
		
		SET @Query = 'SELECT 
								*,('+ @Cols + ')  As [TotalSale] INTO #temp_MatchesTotal
							FROM 
						(
							Select 
								CONVERT(varchar,BillDateTime ,105) As BillDate,TotalAmount,OrderType 
							FROM 
								posSalesMaster, posOrderTypeMaster 
							WHERE 
								linktoOrderTypeMasterId = OrderTypeMasterId	
								and posSalesMaster.linktoBusinessMasterId=('+CONVERT(varchar(10), @linktoBusinessMasterId)  + ')						
								AND  CONVERT(varchar(8),BillDateTime ,112) BETWEEN  CONVERT(varchar(8), ''' + Convert(varchar(8),@FromDate,112) +  ''',112)  AND   CONVERT(varchar(8), ''' + Convert(varchar(8), @ToDate,112)  +''' ,112)
						) X
						pivot
						(
							SUM(TotalAmount)
							FOR OrderType in (' + @columnHeaders + ')
						)p
						SELECT * FROM #temp_MatchesTotal
		DROP TABLE #temp_MatchesTotal'

		--print @Query
		exec(@Query)
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterSaleDailyWeeaklyMonthly_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posSalesMasterSaleDailyWeeaklyMonthly_Select 1
CREATE PROCEDURE [dbo].[posSalesMasterSaleDailyWeeaklyMonthly_Select]

	  @linktoBusinessMasterId smallint	
AS
BEGIN
	SET NOCOUNT ON

CREATE TABLE #TEMP (DailySale Money,WeeaklySale money,MonthlySale money)

INSERT INTO #TEMP 

VALUES(
		(
		SELECT
			SUM(NetAmount) TodaySaleAmount
		FROM 
			posSalesMaster 
		WHERE 
			CONVERT(varchar(8),BillDateTime,112)=CONVERT(varchar(8),GETDATE(),112)
			AND linktoBusinessMasterId=@linktoBusinessMasterId
			GROUP BY CONVERT(varchar(8),BillDateTime,112)
		),

		(
		SELECT
			SUM(NetAmount) WeeaklySale
		FROM 
			posSalesMaster 
		WHERE 
			DATEPART(WK,BillDateTime) = DATEPART(WK,GETDATE())
			AND linktoBusinessMasterId=@linktoBusinessMasterId
		),

		(
		SELECT
			 SUM(NetAmount) MonthlySale
		FROM 
			 posSalesMaster 
		WHERE
		     DATEPART(MONTH,BillDateTime) = DATEPART(MONTH,GETDATE())
			 AND linktoBusinessMasterId=@linktoBusinessMasterId
		)
     )
 SELECT * FROM #TEMP

 DROP TABLE #TEMP
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterSalesBillwiseReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 -- posSalesMasterSalesBillwiseReport_SelectAll '20160314','20160315',0,null,null,1
CREATE PROCEDURE [dbo].[posSalesMasterSalesBillwiseReport_SelectAll]
	@FromDate DateTime,
	@ToDate DateTime,	
	@linktoCounterMasterId  smallint,
	@FromBillNumber varchar(50) = null, 
	@ToBillNumber varchar(50)= null,
	@linktoBusinessMasterId smallint



AS
BEGIN 
	SELECT 
	SUM(TotalAmount) AS GrossAmount,SUM(TotalTax) As Tax,SUM(DiscountAmount) As DiscountAmount,
	SUM(Rounding) as Rounding,SUM(NetAmount) As NetAmount,SUM(Cash) As Cash,SUM(Bank) As Bank,SUM(Card) As Card,SUM(Complemetory) As Complemetory,SUM(Others) As Others
	,BillNumber,CounterName
FROM
(
	SELECT 
		TotalAmount,TotalTax,DiscountAmount,Rounding,NetAmount
		,SUM(Cash) As Cash,SUM(Bank) As Bank,SUM(Card) As Card,SUM(Complemetory) As Complemetory,SUM(Others) As Others,BillNumber,CounterName
	FROM
	(
		SELECT DISTINCT 
			TotalAmount,DiscountAmount,TotalTax,Rounding,NetAmount,Sm.BillNumber,CM.CounterName,
			
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 1 THEN AmountPaid ELSE 0 END As Cash,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 2 THEN AmountPaid ELSE 0 END As Bank,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 3 THEN AmountPaid ELSE 0 END As Card,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 4 THEN AmountPaid ELSE 0 END As Complemetory,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 5 THEN AmountPaid ELSE 0 END As Others 

		FROM
			posSalesMaster SM
		LEFT JOIN
			posSalesPaymentTran SPT ON SPT.linktoSalesMasterId = SM.SalesMasterId
		LEFT JOIN
			posPaymentTypeMaster PM ON PM.PaymentTypeMasterId = SPT.linktoPaymentTypeMasterId
		JOIN
			posCounterMaster CM ON CM.CounterMasterId = SM.linktoCounterMasterId
		WHERE 
		 SM.linktoCounterMasterId=CASE WHEN @linktoCounterMasterId=0 THEN SM.linktoCounterMasterId ELSE @linktoCounterMasterId END
		 AND CONVERT(Varchar(8),SM.BillDateTime,112) Between CONVERT(Varchar(8),@FromDate,112) AND CONVERT(Varchar(8),@ToDate,112)
		  AND BillNumber > = CASE WHEN  @FromBillNumber IS NULL THEN '0' ELSE @FromBillNumber END    AND  BillNumber < = CASE WHEN  @ToBillNumber IS NULL THEN (SELECT MAX(BillNumber) FROM posSalesMaster) ELSE @ToBillNumber END 
		  AND SM.linktoBusinessMasterId=@linktoBusinessMasterId
	)Temp
	GROUP BY TotalAmount,TotalTax,DiscountAmount,Rounding,NetAmount,BillNumber,CounterName
)Temp1
GROUP BY BillNumber,CounterName 
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterSalesCollectionReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 -- posSalesMasterSalesCollectionReport_SelectAll '20160314','20160315',0
CREATE PROCEDURE [dbo].[posSalesMasterSalesCollectionReport_SelectAll]
	@FromDate DateTime,
	@ToDate DateTime,
	@linktoOrderTypeMasterId smallint,
	@linktoBusinessMasterId int
AS
BEGIN 
	SELECT * ,
 	STUFF((
			SELECT DISTINCT ',' + CONVERT(VARCHAR,PaymentType)
			FROM posPaymentTypeMaster PTM
			JOIN posSalesPaymentTran SPT ON SPT.linktoPaymentTypeMasterId = PTM.PaymentTypeMasterId			
			JOIN posSalesMaster SM1 ON SM1.SalesMasterId = SPT.linktoSalesMasterId
			WHERE SPT.IsDeleted = 0 AND Convert(varchar(10),SM1.BillDateTime,105) = BillDate	
			AND linktoOrderTypeMasterId =  CASE WHEN @linktoOrderTypeMasterId = 0 THEN linktoOrderTypeMasterId ELSE @linktoOrderTypeMasterId END
			AND PTM.linktoBusinessMasterId=@linktoBusinessMasterId		
		FOR XML PATH(''), TYPE   
		).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As PaymentType
	FROM
	(
			SELECT 	
					Convert(varchar(10),BillDateTime,105) BillDate,	
					SUM(TotalAmount) TotalAmount,
					SUM(DiscountAmount) DiscountAmount,
					SUM(DiscountPercentage) DiscountPercentage,
					SUM(NetAmount) NetAmount
				FROM posSalesMaster
			 WHERE
			  CONVERT(VARCHAR(8),BillDateTime,112) BETWEEN CONVERT(VARCHAR(8),@FromDate,112) AND CONVERT(VARCHAR(8),@ToDate,112)
			  AND linktoOrderTypeMasterId = CASE WHEN @linktoOrderTypeMasterId = 0 THEN linktoOrderTypeMasterId ELSE @linktoOrderTypeMasterId END
			  AND linktoBusinessMasterId=@linktoBusinessMasterId
			 GROUP BY Convert(varchar(10),BillDateTime,105)
	 )
	 TempTable 
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterSalesItemBySalesMasterID_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posSalesMasterSalesItemBySalesMasterID_SelectAll '1'
CREATE PROCEDURE [dbo].[posSalesMasterSalesItemBySalesMasterID_SelectAll]
	@SalesMasterId bigint
AS
BEGIN
	SELECT 
		(-1 * DENSE_RANK() OVER (ORDER BY SalesItemTranId)) As OrderId,SalesItemTranId,ItemMasterId,ItemName,ItemMasterIdModifier,SUM(Quantity) Quantity,SUM(DiscountAmount) DiscountAmount,SUM(MRP) MRP,SUM(Rate1) Rate1,SUM(Rate2) Rate2,SUM(Rate3) Rate3,
		SUM(Rate4) Rate4,SUM(Rate5) Rate5,SUM(Tax1) Tax1,SUM(Tax2) Tax2,SUM(Tax3) Tax3,SUM(Tax4) Tax4,SUM(Tax5) Tax5,SUM(MRPWithTax) MRPWithTax,
		SUM(Rate1WithTax) Rate1WithTax,SUM(Rate2WithTax) Rate2WithTax,SUM(Rate3WithTax) Rate3WithTax,SUM(Rate4WithTax) Rate4WithTax,
		SUM(Rate5WithTax) Rate5WithTax,RateIndex,ItemRemark
	FROM
	( 
		SELECT
			SalesItemTranId,ItemMasterId,IM.ItemName,0 AS ItemMasterIdModifier,Quantity,SIT.DiscountAmount,
			CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (MRP - ((MRP * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE MRP END MRP,
				CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate1 - ((Rate1 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate1 END Rate1,
				CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate2 - ((Rate2 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate2 END Rate2,
				CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate3 - ((Rate3 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate3 END Rate3,
				CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate4 - ((Rate4 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate4 END Rate4,
				CASE WHEN IRT.IsRateTaxInclusive = 1 THEN (Rate5 - ((Rate5 * (IRT.Tax1 + IRT.Tax2 + IRT.Tax3 + IRT.Tax4 + IRT.Tax5))/100)) ELSE Rate5 END Rate5,
			IRT.Tax1,IRT.Tax2,IRT.Tax3,IRT.Tax4,IRT.Tax5,
			MRP As MRPWithTax,Rate1 As Rate1WithTax,Rate2 As Rate2WithTax,Rate3 As Rate3WithTax,Rate4 As Rate4WithTax,Rate5 As Rate5WithTax,SM.RateIndex,
			SIT.ItemRemark
		FROM
			posSalesMaster SM
		JOIN
			posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId
		JOIN
			posItemMaster IM ON IM.ItemMasterId = SIT.linktoItemMasterId
		JOIN
			posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
		WHERE 
			SM.SalesMasterId = @SalesMasterId
		UNION ALL
		SELECT
			SalesItemTranId,SIT.linktoItemMasterId,IM.ItemName,ItemMasterId,Quantity,0,
			MRP,MRP As Rate1,MRP As Rate2,MRP As Rate3,MRP As Rate4,MRP As Rate5,IRT.Tax1,IRT.Tax2,IRT.Tax3,IRT.Tax4,IRT.Tax5,
			0 As MRPWithTax,0 As Rate1WithTax,0 As Rate2WithTax,0 As Rate3WithTax,0 As Rate4WithTax,0 As Rate5WithTax,SM.RateIndex,
			SIT.ItemRemark
		FROM
			posSalesMaster SM
		JOIN
			posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId
		JOIN
			posSalesItemModifierTran SIMT ON SIMT.linktoSalesItemTranId = SIT.SalesItemTranId
		JOIN
			posItemMaster IM ON IM.ItemMasterId = SIMT.linktoItemMasterId
		JOIN
			posItemRateTran IRT ON IRT.linktoItemMasterId = IM.ItemMasterId
		WHERE 
			SM.SalesMasterId = @SalesMasterId
	)Temp
	GROUP BY SalesItemTranId,ItemMasterId,ItemName,ItemMasterIdModifier,RateIndex,ItemRemark
	ORDER BY SalesItemTranId,ItemMasterId,ItemRemark,ItemMasterIdModifier
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterSalesUserDateCounetrWiseReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 -- posSalesMasterSalesUserDateCounetrWiseReport_SelectAll '20160314','20160315',0,0
CREATE PROCEDURE [dbo].[posSalesMasterSalesUserDateCounetrWiseReport_SelectAll]
	@FromDate DateTime,
	@ToDate DateTime,
	@linktoUserMasterIdCreatedBy smallint,
	@linktoCounterMasterId  Smallint,
	@linktoBusinessMasterId int

AS
BEGIN 
	SELECT 
	BillDate,SUM(TotalAmount) AS GrossAmount,SUM(TotalTax) As Tax,SUM(DiscountAmount) As DiscountAmount,
	SUM(Rounding) as Rounding,SUM(NetAmount) As NetAmount,SUM(Cash) As Cash,SUM(Bank) As Bank,SUM(Card) As Card,SUM(Complemetory) As Complemetory,SUM(Others) As Others	,UserName
FROM
(
	SELECT 
		Convert(Varchar(10),BillDateTime,105) As BillDate,TotalAmount,TotalTax,DiscountAmount,Rounding,NetAmount
		,SUM(Cash) As Cash,SUM(Bank) As Bank,SUM(Card) As Card,SUM(Complemetory) As Complemetory,SUM(Others) As Others,UserName
	FROM
	(
		SELECT DISTINCT 
			BillDateTime,TotalAmount,DiscountAmount,TotalTax,Rounding,NetAmount,
			(SELECT Username FROM posUserMaster WHERE UserMasterId = CASE WHEN @linktoUserMasterIdCreatedBy = 0 THEN SM.linktoUserMasterIdCreatedBy ELSE @linktoUserMasterIdCreatedBy END) AS UserName,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 1 THEN AmountPaid ELSE 0 END As Cash,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 2 THEN AmountPaid ELSE 0 END As Bank,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 3 THEN AmountPaid ELSE 0 END As Card,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 4 THEN AmountPaid ELSE 0 END As Complemetory,
			CASE WHEN PM.linktoPaymentTypeCategoryMasterId = 5 THEN AmountPaid ELSE 0 END As Others 

		FROM
			posSalesMaster SM
		LEFT JOIN
			posSalesPaymentTran SPT ON SPT.linktoSalesMasterId = SM.SalesMasterId
		LEFT JOIN
			posPaymentTypeMaster PM ON PM.PaymentTypeMasterId = SPT.linktoPaymentTypeMasterId
		WHERE 
		 SM.linktoUserMasterIdCreatedBy= CASE WHEN @linktoUserMasterIdCreatedBy=0 THEN SM.linktoUserMasterIdCreatedBy ELSE @linktoUserMasterIdCreatedBy END
		 AND SM.linktoCounterMasterId=CASE WHEN @linktoCounterMasterId=0 THEN SM.linktoCounterMasterId ELSE @linktoCounterMasterId END
		 AND CONVERT(Varchar(8),SM.BillDateTime,112) Between CONVERT(Varchar(8),@FromDate,112) AND CONVERT(Varchar(8),@ToDate,112)
		 AND SM.linktoBusinessMasterId=@linktoBusinessMasterId
	)Temp
	GROUP BY Convert(Varchar(10),BillDateTime,105),TotalAmount,TotalTax,DiscountAmount,Rounding,NetAmount,UserName
)Temp1
GROUP BY BillDate,UserName
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterTableWiseSummaryReport_SelectALL]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posSalesMasterTableWiseSummaryReport_SelectALL 0,0,'20160101','20161231'
CREATE PROCEDURE [dbo].[posSalesMasterTableWiseSummaryReport_SelectALL]
@linktoCounterMasterId smallint
,@linktoBusinessMasterId Smallint
,@FromDate DateTime
,@ToDate DateTime

AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		TableName,SUM(TotalAmount) TotalAmount, SUM(DiscountAmount) DiscountAmount,SUM(TotalTax) TotalTax,SUM(NetAmount)NetAmount,SUM(Quantity) Quantity 
	FROM
	(
		SELECT DISTINCT SM.linktoTableMasterIds,
			STUFF((SELECT DISTINCT ',' + CONVERT(VARCHAR,TableName)  
							FROM posTableMaster
							WHERE TableMasterId IN (SELECT parsevalue FROM dbo.parse(SM.linktoTableMasterIds,','))
					 FOR XML PATH(''), TYPE   
					).value('.', 'VARCHAR(MAX)')   
					,1,1,'') AS TableName,		
			TotalAmount, SM.DiscountAmount,TotalTax,NetAmount,SUM(Quantity) Quantity
		FROM 
			posSalesMaster SM 
		JOIN
			posSalesItemTran SIT ON SIT.linktoSalesMasterId = SM.SalesMasterId
		WHERE 
			SM.linktoCounterMasterId = CASE WHEN @linktoCounterMasterId = 0 THEN SM.linktoCounterMasterId ELSE @linktoCounterMasterId END 
			AND CONVERT(varchar(8),SM.BillDateTime,112) between CONVERT(varchar(8),@fromDate,112) AND CONVERT(varchar(8),@ToDate,112)
			AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
		GROUP BY SM.linktoTableMasterIds,TotalAmount, SM.DiscountAmount,TotalTax,NetAmount
	)TEMP
	GROUP BY TableName
	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posSalesMasterYearlyRevenue_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesMasterYearlyRevenue_SelectAll]
	@Year varchar(100),
	@linktoBusinessMasterId smallint
AS
BEGIN
	CREATE TABLE #Temp(id int identity(1,1),months varchar(100),years varchar(100),Revenue money)

	DECLARE @TotalRecords smallint = 1,@TotalYears smallint = 1

	WHILE(@TotalYears <= 2)
	BEGIN
		WHILE (@TotalRecords < = 12)
		BEGIN
			INSERT INTO #Temp(months,years,Revenue)
			SELECT DATENAME(MONTH,DATEADD(MONTH,@TotalRecords,0 ) - 1 ),@year,ISNULL(SUM(NetAmount),0)
			FROM posSalesMaster WHERE linktoBusinessMasterId = @linktoBusinessMasterId
			AND DATEPART(YEAR,BillDateTime) = @year AND DATEPART(MONTH,BillDateTime) = @TotalRecords
		
			SET @TotalRecords = @TotalRecords + 1
		END
		SET @TotalYears = @TotalYears + 1
		SET @year = Convert(varchar,Convert(smallint,@year) - 1)	
		SET @TotalRecords = 1
	END

	SELECT * FROM #temp

	DROP TABLE #temp

END



GO
/****** Object:  StoredProcedure [dbo].[posSalesPaymentTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesPaymentTran_Insert]
	 @SalesPaymentTranId bigint OUTPUT
	,@linktoSalesMasterId bigint
	,@linktoPaymentTypeMasterId smallint
	,@linktoCustomerMasterId int = NULL
	,@PaymentDateTime datetime
	,@AmountPaid money
	,@Remark varchar(250) = NULL
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posSalesPaymentTran
	(
		 linktoSalesMasterId
		,linktoPaymentTypeMasterId
		,linktoCustomerMasterId
		,PaymentDateTime
		,AmountPaid
		,Remark
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoSalesMasterId
		,@linktoPaymentTypeMasterId
		,@linktoCustomerMasterId
		,@PaymentDateTime
		,@AmountPaid
		,@Remark
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SalesPaymentTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesPaymentTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesPaymentTran_SelectAll]
	@linktoSalesMasterId bigint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posSalesPaymentTran.*		
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType		
	FROM
		 posSalesPaymentTran
	WHERE
		IsDeleted = 0 AND linktoSalesMasterId = @linktoSalesMasterId AND linktoPaymentTypeMasterId > 0
UNION ALL
	SELECT
		posSalesPaymentTran.*					
		,(SELECT CustomerName FROM posCustomerMaster WHERE CustomerMasterId = linktoCustomerMasterId) AS Customer
	FROM
		 posSalesPaymentTran
	WHERE
		IsDeleted = 0 AND linktoSalesMasterId = @linktoSalesMasterId AND linktoCustomerMasterId > 0
	ORDER BY SalesPaymentTranId


	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesPaymentTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesPaymentTran_Update]
	 @SalesPaymentTranId bigint
	,@linktoSalesMasterId bigint
	,@linktoPaymentTypeMasterId smallint
	,@linktoCustomerMasterId int = NULL
	,@PaymentDateTime datetime
	,@AmountPaid money
	,@Remark varchar(250) = NULL
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posSalesPaymentTran
	SET
		 linktoSalesMasterId = @linktoSalesMasterId
		,linktoPaymentTypeMasterId = @linktoPaymentTypeMasterId
		,linktoCustomerMasterId = @linktoCustomerMasterId
		,PaymentDateTime = @PaymentDateTime
		,AmountPaid = @AmountPaid
		,Remark = @Remark
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		SalesPaymentTranId = @SalesPaymentTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesPaymentTranCounterWiseReport_SelectALL]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posSalesPaymentTranCounterWiseReport_SelectALL]

 @CounterMasterId smallint 
,@OrderTypeMasterId Smallint
,@FromDate DateTime
,@ToDate DateTime

AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		CounterName,PaymentType,SUM(AmountPaid) AS AmountPaid
	FROM
		posSalesPaymentTran SPT, posPaymentTypeMaster PTM, posSalesMaster SM, posCounterMaster CM  
	WHERE 
		SPT.linktoPaymentTypeMasterId = PTM.PaymentTypeMasterId
		AND SM.SalesMasterId = SPT.linktoSalesMasterId
		AND SM.linktoCounterMasterId = CASE WHEN @CounterMasterId = 0 THEN SM.linktoCounterMasterId ELSE @CounterMasterId END
		AND SM.linktoCounterMasterId = CM.CounterMasterId
		AND CONVERT(VARCHAR(8),SPT.PaymentDateTime,112) BETWEEN CONVERT(VARCHAR(8),@FromDate,112) AND CONVERT(VARCHAR(8),@ToDate,112)
		AND SM.linktoOrderTypeMasterId = CASE WHEN @OrderTypeMasterId = 0 THEN SM.linktoOrderTypeMasterId  ELSE @OrderTypeMasterId END
		
	GROUP BY 
		CounterName,PaymentType
	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posSalesPaymentTranCreditorPayment_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesPaymentTranCreditorPayment_SelectAll]
	@linktoCustomerMasterId Int
AS
BEGIN

	SET NOCOUNT ON

	SELECT DISTINCT
	Cm.CustomerName,
	CM.CustomerMasterId,
	SM.SalesMasterId,
	SM.BillNumber,
	SUM(AmountPaid) Amount,
	SM.TotalAmount,
	STUFF((SELECT DISTINCT ', ' + CONVERT(VARCHAR,TableName)  
				FROM posTableMaster
				WHERE TableMasterId in (SELECT parsevalue FROM dbo.parse(SM.linktoTableMasterIds,',')) 
		FOR XML PATH(''), TYPE   
		).value('.', 'VARCHAR(MAX)')   
		,1,1,'') As TableName
	FROM
		posSalesMaster Sm
	JOIN 
		posSalesPaymentTran SPT ON  SPT.linktoSalesMasterId=SM.SalesMasterId
	JOIN 
		posCustomerMaster CM ON CM.CustomerMasterId=SPT.linktoCustomerMasterId
	WHERE
		SM. IsDeleted = 0 AND SPT.IsDeleted = 0 AND SPT.linktoCustomerMasterId = @linktoCustomerMasterId
		AND SalesMasterId NOT IN (
			Select CIT.linktoSalesMasterId FROM posCustomerInvoiceMaster CIM 
			JOIN posCustomerInvoiceTran CIT ON CIT.linktoCustomerInvoiceMasterId = CIM.CustomerInvoiceMasterId 
			WHERE CIM.linktoCustomerMasterId = CM.CustomerMasterId and IsDeleted=0
		) 
	GROUP BY  CM.CustomerName,Sm.BillNumber,CM.CustomerMasterId,SM.SalesMasterId,Sm.linktoTableMasterIds,Sm.TotalAmount

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesPaymentTranDayWiseReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [dbo].[posSalesPaymentTranDayWiseReport_SelectAll]
	@FromDate datetime
	,@ToDate datetime
	,@linktoBusinessMasterId int
AS
BEGIN
	SELECT 
		PaymentDateTime  ,DATENAME(dw,PaymentDateTime) PaymentDay ,SUM(AmountPaid) As AmountPaid 
	FROM 
		posSalesPaymentTran SPT, posSalesMaster SM
	WHERE 
	   --(PaymentDateTime >= @FromDate) AND (PaymentDateTime <= @ToDate)
		CONVERT(varchar(8),PaymentDateTime,112) BETWEEN CONVERT(varchar(8),@FromDate,112) AND CONVERT(varchar(8),@ToDate,112)
		AND SM.SalesMasterId=SPT.linktoSalesMasterId
		AND SM.linktoBusinessMasterId=@linktoBusinessMasterId
	GROUP BY PaymentDateTime
END





GO
/****** Object:  StoredProcedure [dbo].[posSalesPaymentTranReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesPaymentTranReport_SelectAll] 
	@FromDate Datetime
	,@ToDate DateTime
	,@linktoBusinessMasterId int
AS
BEGIN
	SELECT 
		--SalesPaymentTranId
		PaymentType
		,SUM(AmountPaid) AS AmountPaid
		--,SalesMasterId
	FROM 
		posSalesPaymentTran SPT, posPaymentTypeMaster PTM, posSalesMaster SM
		
	WHERE 
		SPT.linktoPaymentTypeMasterId = PTM.PaymentTypeMasterId
		AND convert(varchar,SPT.PaymentDateTime,112) BETWEEN convert(varchar,@FromDate,112) AND convert(varchar,@ToDate,112)
		AND SM.SalesMasterId=SPT.linktoSalesMasterId
		AND SM.linktoBusinessMasterId=@linktoBusinessMasterId
	GROUP BY
		PaymentType
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesTaxTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesTaxTran_Delete]
	 @linktoSalesMasterId bigint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posSalesTaxTran
	WHERE
		linktoSalesMasterId = @linktoSalesMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesTaxTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesTaxTran_Insert]
	 @SalesTaxTranId bigint OUTPUT
	,@linktoSalesMasterId bigint
	,@linktoTaxMasterId smallint
	,@TaxName varchar(50)
	,@TaxRate numeric(5,2)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posSalesTaxTran
	(
		 linktoSalesMasterId
		,linktoTaxMasterId
		,TaxName
		,TaxRate
	)
	VALUES
	(
		 @linktoSalesMasterId
		,@linktoTaxMasterId
		,@TaxName
		,@TaxRate
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SalesTaxTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesTaxTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesTaxTran_SelectAll]
	@linktoSalesMasterId bigInt
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posSalesTaxTran.*		
	FROM
		 posSalesTaxTran
	WHERE 
		linktoSalesMasterId = @linktoSalesMasterId
	ORDER BY SalesTaxTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesTaxTranForBill_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesTaxTranForBill_SelectAll]
	@linktoSalesMasterId bigInt
AS
BEGIN

	SET NOCOUNT ON

	SELECT TaxName, TaxRate, SUM(Tax1) AS TaxAmount FROM posSalesTaxTran STT, posSalesItemTran SIT 
	WHERE SIT.linktoSalesMasterId = @linktoSalesMasterId AND STT.linktoSalesMasterId = @linktoSalesMasterId
		AND linktoTaxMasterId = (SELECT TaxMasterId FROM posTaxMaster WHERE TaxIndex = 1 
		AND linktoBusinessMasterId = (SELECT linktoBusinessMasterId FROM posSalesMaster WHERE SalesMasterId = @linktoSalesMasterId))
	GROUP BY TaxName, TaxRate

	UNION

	SELECT TaxName, TaxRate, SUM(Tax2) AS TaxAmount FROM posSalesTaxTran STT, posSalesItemTran SIT 
	WHERE SIT.linktoSalesMasterId = @linktoSalesMasterId AND STT.linktoSalesMasterId = @linktoSalesMasterId
		AND linktoTaxMasterId = (SELECT TaxMasterId FROM posTaxMaster WHERE TaxIndex = 2 
		AND linktoBusinessMasterId = (SELECT linktoBusinessMasterId FROM posSalesMaster WHERE SalesMasterId = @linktoSalesMasterId))
	GROUP BY TaxName, TaxRate

	UNION

	SELECT TaxName, TaxRate, SUM(Tax3) AS TaxAmount FROM posSalesTaxTran STT, posSalesItemTran SIT 
	WHERE SIT.linktoSalesMasterId = @linktoSalesMasterId AND STT.linktoSalesMasterId = @linktoSalesMasterId
		AND linktoTaxMasterId = (SELECT TaxMasterId FROM posTaxMaster WHERE TaxIndex = 3 
		AND linktoBusinessMasterId = (SELECT linktoBusinessMasterId FROM posSalesMaster WHERE SalesMasterId = @linktoSalesMasterId))
	GROUP BY TaxName, TaxRate
	
	UNION

	SELECT TaxName, TaxRate, SUM(Tax4) AS TaxAmount FROM posSalesTaxTran STT, posSalesItemTran SIT 
	WHERE SIT.linktoSalesMasterId = @linktoSalesMasterId AND STT.linktoSalesMasterId = @linktoSalesMasterId
		AND linktoTaxMasterId = (SELECT TaxMasterId FROM posTaxMaster WHERE TaxIndex = 4 
		AND linktoBusinessMasterId = (SELECT linktoBusinessMasterId FROM posSalesMaster WHERE SalesMasterId = @linktoSalesMasterId))
	GROUP BY TaxName, TaxRate
	
	UNION

	SELECT TaxName, TaxRate, SUM(Tax5) AS TaxAmount FROM posSalesTaxTran STT, posSalesItemTran SIT 
	WHERE SIT.linktoSalesMasterId = @linktoSalesMasterId AND STT.linktoSalesMasterId = @linktoSalesMasterId
		AND linktoTaxMasterId = (SELECT TaxMasterId FROM posTaxMaster WHERE TaxIndex = 5 
		AND linktoBusinessMasterId = (SELECT linktoBusinessMasterId FROM posSalesMaster WHERE SalesMasterId = @linktoSalesMasterId))
	GROUP BY TaxName, TaxRate

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSalesTaxTranForKOT_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSalesTaxTranForKOT_Insert]
	@SalesTaxTranId bigint OUTPUT
	,@linktoSalesMasterId bigint
	,@TaxIndex smallint
	,@TaxRate numeric(5,2)
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	DECLARE @linktoTaxMasterId smallint,@TaxName varchar(50),@IsEnabled bit
	
	SELECT @linktoTaxMasterId = TaxMasterId,@TaxName = TaxName,@IsEnabled = IsEnabled FROM posTaxMaster WHERE TaxIndex = @TaxIndex AND linktoBusinessMasterId = @linktoBusinessMasterId

	if(@IsEnabled = 1)
	BEGIN
		INSERT INTO posSalesTaxTran
		(
			 linktoSalesMasterId
			,linktoTaxMasterId
			,TaxName
			,TaxRate
		)
		VALUES
		(
			 @linktoSalesMasterId
			,@linktoTaxMasterId
			,@TaxName
			,@TaxRate
		)
	END
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SalesTaxTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSettingMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posSettingMaster_SelectAll]
@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		posSettingMaster.*
	FROM 
		posSettingMaster
	
	WHERE 
		linktoBusinessMasterId = @linktoBusinessMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSettingMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posSettingMaster_Update]
	 @SettingMasterId smallint	
	,@Value varchar(120)	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	UPDATE posSettingMaster
	SET		
		Value = @Value		
	WHERE
		SettingMasterId = @SettingMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posSettingMasterBySettingName_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posSettingMasterBySettingName_Update]
	 @Setting varchar(50)	
	,@Value varchar(120)
	,@linktoBusinessMasterId smallint	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	UPDATE posSettingMaster
	SET		
		Value = @Value		
	WHERE
		Setting = @Setting AND linktoBusinessMasterId = @linktoBusinessMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posSettingMasterTableAssignByBusinessId_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSettingMasterTableAssignByBusinessId_Select] 
	@linktoBusinessMasterId smallint,
	@SettingMasterId smallint
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    SELECT 
		* 
	FROM 
		posSettingMaster 
	WHERE 
		linktoBusinessMasterId= @linktoBusinessMasterId
		AND SettingMasterId= @SettingMasterId
END

GO
/****** Object:  StoredProcedure [dbo].[posShiftMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posShiftMaster_Insert]
	 @ShiftMasterId int OUTPUT
	,@linktoUserMasterId smallint
	,@SoftwareStartDateTime datetime
	,@SystemStartDateTine datetime	
	,@OpeningBalance money	
	,@ClosingBalance money
	,@IsClosed bit
	,@SystemName varchar(50)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	INSERT INTO posShiftMaster
	(
		linktoUserMasterId
		,SoftwareStartDateTime
		,SystemStartDateTine					
		,OpeningBalance	
		,ClosingBalance
		,IsClosed									
		,SystemName
	)
	VALUES
	(
		@linktoUserMasterId
		,@SoftwareStartDateTime
		,@SystemStartDateTine					
		,@OpeningBalance
		,@ClosingBalance
		,@IsClosed
		,@SystemName
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN		
		SET @ShiftMasterId = @@IDENTITY		
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posShiftMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posShiftMaster_Update]
	 @ShiftMasterId int	
	,@SoftwareEndDateTime datetime = NULL
	,@SystemEndDateTime datetime = NULL
	,@ClosingBalance money
	,@IsClosed bit
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posShiftMaster
	SET
		SoftwareEndDateTime = @SoftwareEndDateTime
		,SystemEndDateTime = @SystemEndDateTime
		,ClosingBalance = @ClosingBalance
		,IsClosed=@IsClosed
		
	WHERE
		ShiftMasterId = @ShiftMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posShiftMasterBylinktoUserMasterId_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posShiftMasterBylinktoUserMasterId_Select 1 
CREATE PROCEDURE [dbo].[posShiftMasterBylinktoUserMasterId_Select]
	@linktoUserMasterId smallint
AS
BEGIN
	SELECT * 
		,(SELECT Username FROM posUserMaster WHERE UserMasterId = @linktoUserMasterId) AS Username
	FROM posShiftMaster 
	WHERE linktoUserMasterId = @linktoUserMasterId and IsClosed = 0
END



GO
/****** Object:  StoredProcedure [dbo].[posShiftMasterShiftEndReport_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posShiftMasterShiftEndReport_SelectAll 1,1
CREATE PROCEDURE [dbo].[posShiftMasterShiftEndReport_SelectAll]
	 @linktoUserMasterId smallint
	,@linktoBusinessMasterId smallint
	,@PersonsVisited smallint OUTPUT
AS
BEGIN

	SET NOCOUNT ON;

	DECLARE @ShiftStartDate datetime

	SELECT @ShiftStartDate = SystemStartDateTine FROM posShiftMaster WHERE linktoUserMasterId = @linktoUserMasterId and IsClosed = 0

	SELECT @PersonsVisited = SUM(NoOfAdults) + SUM(NoOfChildren)
	FROM posSalesMaster
			WHERE linktoUserMasterIdCreatedBy = @linktoUserMasterId
			AND linktoBusinessMasterId = @linktoBusinessMasterId
			AND IsDeleted = 0
			AND CONVERT(varchar(8),CreateDateTime,112) BETWEEN CONVERT(varchar(8), @ShiftStartDate,112) AND CONVERT(varchar(8),GETDATE(),112)

	SET @PersonsVisited = ISNULL(@PersonsVisited, 0)

	SELECT * FROM posShiftMaster,
	(
		SELECT PaymentTypeCategory, ISNULL(SUM(AmountPaid), 0) AS AmountPaid, PaymentTypeCategoryMasterId
		FROM
		( 
			SELECT linktoPaymentTypeMasterId, ISNULL(SUM(AmountPaid), 0) AS AmountPaid
			FROM 
				posSalesMaster SM, posSalesPaymentTran
			WHERE
				SalesMasterId = linktoSalesMasterId
				AND SM.linktoUserMasterIdCreatedBy = @linktoUserMasterId
				AND SM.linktoBusinessMasterId = @linktoBusinessMasterId
				AND SM.IsDeleted = 0
				AND CONVERT(varchar(8),SM.CreateDateTime,112) BETWEEN CONVERT(varchar(8), @ShiftStartDate,112) AND CONVERT(varchar(8),GETDATE(),112)
			GROUP BY linktoPaymentTypeMasterId

			UNION

			SELECT linktoPaymentTypeMasterId, ISNULL(SUM(AmountPaid), 0) AS AmountPaid
			FROM 
				posBookingMaster BM, posBookingPaymentTran
			WHERE
				BM.linktoUserMasterIdCreatedBy = @linktoUserMasterId
				AND BM.linktoBusinessMasterId = @linktoBusinessMasterId
				AND BM.IsDeleted = 0
				AND CONVERT(varchar(8),BM.CreateDateTime,112) BETWEEN CONVERT(varchar(8), @ShiftStartDate,112) AND CONVERT(varchar(8),GETDATE(),112)
			GROUP BY linktoPaymentTypeMasterId

		) PaymentType
		RIGHT JOIN posPaymentTypeMaster ON PaymentTypeMasterId = linktoPaymentTypeMasterId
		RIGHT JOIN posPaymentTypeCateogryMaster ON PaymentTypeCategoryMasterId = linktoPaymentTypeCategoryMasterId
		GROUP BY PaymentTypeCategory, PaymentTypeCategoryMasterId
	
		UNION

		SELECT
			'Deposite', ISNULL(SUM(ST.CreditAmount), 0), 99
		FROM
			posShiftMaster SM
		JOIN posShiftTran ST ON ST.linktoShiftMasterId = SM.ShiftMasterId
		WHERE 
			linktoUserMasterId = @linktoUserMasterId 
			AND IsClosed = 0 
		
		UNION

		SELECT 'Withdraw', ISNULL(SUM(ST.DebitAmount), 0) * -1, 100
		FROM
			posShiftMaster SM
			JOIN posShiftTran ST ON ST.linktoShiftMasterId = SM.ShiftMasterId 
		WHERE linktoUserMasterId = @linktoUserMasterId 
			AND IsClosed = 0 
	) PaymentTypeCategory
	where IsClosed=0 and linktoUserMasterId = @linktoUserMasterId 
	ORDER BY PaymentTypeCategoryMasterId
	
END



GO
/****** Object:  StoredProcedure [dbo].[posShiftTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 
CREATE PROCEDURE [dbo].[posShiftTran_Insert]
	 @ShiftTranId int OUTPUT 
	 ,@linktoUserMasterId int
	,@DebitAmount money
	,@CreditAmount money
	,@Remark Varchar(250)
	,@CreateDateTime datetime 
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posShiftTran
	(
		 linktoShiftMasterId
		,DebitAmount
		,CreditAmount
		,Remark
		,CreateDateTime
	)
	VALUES
	(
		 (SELECT ShiftMasterId from posShiftMaster Where linktoUserMasterId = @linktoUserMasterId AND IsClosed = 0 )  
		,@DebitAmount
		,@CreditAmount
		,@Remark
		,@CreateDateTime
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @ShiftTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posShortcut_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posShortcut_SelectAll]
	
AS
BEGIN
	SET NOCOUNT ON;

	SELECT
		SC.ShortcutName
		,SC.ShortcutKey
	FROM
		posShortcuts SC
	WHERE
		SC.ShortcutKey IS NOT NULL
	UNION ALL
    SELECT
		MM.MenuName,
		MM.ShortcutKey
	FROM
		posMenuMaster MM
	WHERE
		MM.ShortcutKey IS NOT NULL
	
END




GO
/****** Object:  StoredProcedure [dbo].[posStateMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posStateMaster_DeleteAll]
	 @StateMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRowCount int
	SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@StateMasterIds, ','))

	UPDATE
		posStateMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		StateMasterId IN (SELECT * from dbo.Parse(@StateMasterIds, ','))
		AND StateMasterId NOT IN
		(
			SELECT linktoStateMasterId from posCityMaster where IsDeleted = 0 AND IsEnabled = 1 
			AND linktoStateMasterId IN (SELECT * from dbo.Parse(@StateMasterIds, ','))
		)

	SET @UpdatedRowCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		IF @TotalRecords = @UpdatedRowCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END
	END  

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posStateMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posStateMaster_Insert]
	 @StateMasterId smallint OUTPUT
	,@linktoBusinessMasterId smallint
	,@StateName varchar(50)
	,@StateCode varchar(3)
	,@linktoCountryMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

		IF EXISTS(SELECT StateMasterId FROM posStateMaster WHERE StateName = @StateName AND IsDeleted = 0)
	BEGIN
		SELECT @StateMasterId =  StateMasterId FROM posStateMaster WHERE StateName = @StateName AND IsDeleted = 0
		SET @Status = -2
		RETURN
	END

		IF EXISTS(SELECT StateMasterId FROM posStateMaster WHERE StateCode = @StateCode AND IsDeleted = 0)
	BEGIN
		SELECT @StateMasterId =  StateMasterId FROM posStateMaster WHERE StateCode = @StateCode AND IsDeleted = 0
		SET @Status = -2
		RETURN
	END
	INSERT INTO posStateMaster
	(
		linktoBusinessMasterId
		,StateName
		,StateCode
		,linktoCountryMasterId
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoBusinessMasterId
		,@StateName
		,@StateCode
		,@linktoCountryMasterId
		,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @StateMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStateMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStateMaster_Select]
	 @StateMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT 
		 posStateMaster.*
		 ,(SELECT CountryName from posCountryMaster where CountryMasterId = linktoCountryMasterId ) As CountryName
	FROM
		posStateMaster
	WHERE
		StateMasterId = @StateMasterId
	

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posStateMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posStateMaster_SelectAll]
@IsEnabled bit,
@linktoCountryMasterId smallint	= NULL ,
@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posStateMaster.*
		 ,(SELECT CountryName from posCountryMaster where CountryMasterId = linktoCountryMasterId) as CountryName
	FROM
		posStateMaster
	 
	WHERE IsEnabled = @IsEnabled  AND Isdeleted = 0 AND linktoCountryMasterId = ISNULL(@linktoCountryMasterId,linktoCountryMasterId) AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY StateName

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posStateMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStateMaster_Update]
	 @StateMasterId smallint
	,@StateName varchar(50)
	,@StateCode varchar(3)
	,@linktoCountryMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT StateMasterId FROM posStateMaster WHERE StateName = @StateName AND StateMasterId != @StateMasterId AND IsDeleted = 0)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posStateMaster
	SET
		 StateName = @StateName
		,StateCode = @StateCode
		,linktoCountryMasterId = @linktoCountryMasterId
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		StateMasterId = @StateMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStateMasterCountrywise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posStateMasterCountrywise_SelectAll]
			 
			@linktoCountryMasterId smallint,
			@linktoBusinessMasterId smallint
			
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posStateMaster.*
		 ,(SELECT CountryName from posCountryMaster where CountryMasterId = linktoCountryMasterId and linktoBusinessMasterId=@linktoBusinessMasterId) as CountryName
	FROM
		posStateMaster
	 
	WHERE 
	
		linktoCountryMasterId = @linktoCountryMasterId
		and linktoBusinessMasterId=@linktoBusinessMasterId
		AND IsEnabled = 1 AND Isdeleted = 0
	ORDER BY StateName

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posStockAdjustmentItemTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStockAdjustmentItemTran_Delete]
	 @linktoStockAdjustmentMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posStockAdjustmentItemTran
	WHERE
		linktoStockAdjustmentMasterId = @linktoStockAdjustmentMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStockAdjustmentItemTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStockAdjustmentItemTran_Insert]
	 @StockAdjustmentItemTranId int OUTPUT
	,@linktoStockAdjustmentMasterId int
	,@linktoItemMasterId int
	,@linktoUnitMasterId smallint
	,@Quantity numeric(9, 2)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posStockAdjustmentItemTran
	(
		 linktoStockAdjustmentMasterId
		,linktoItemMasterId
		,linktoUnitMasterId
		,Quantity
	)
	VALUES
	(
		 @linktoStockAdjustmentMasterId
		,@linktoItemMasterId
		,@linktoUnitMasterId
		,@Quantity
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @StockAdjustmentItemTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStockAdjustmentItemTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStockAdjustmentItemTran_SelectAll]
@linktoStockAdjustmentMasterId int 
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posStockAdjustmentItemTran.*
		--,(SELECT StockAdjustmentMasterId FROM posStockAdjustmentMaster WHERE StockAdjustmentMasterId = linktoStockAdjustmentMasterId) AS StockAdjustment		
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterId) AS Unit
	FROM
		 posStockAdjustmentItemTran
	WHERE 
		 linktoStockAdjustmentMasterId = @linktoStockAdjustmentMasterId
	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStockAdjustmentMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStockAdjustmentMaster_DeleteAll]
	 @StockAdjustmentMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	DELETE
	FROM
		 posStockAdjustmentItemTran
	WHERE
		linktoStockAdjustmentMasterId IN (SELECT * FROM dbo.Parse(@StockAdjustmentMasterIds, ','))

	DELETE
	FROM
		 posStockAdjustmentMaster
	WHERE
		StockAdjustmentMasterId IN (SELECT * from dbo.Parse(@StockAdjustmentMasterIds, ','))
		
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStockAdjustmentMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStockAdjustmentMaster_Insert]
	 @StockAdjustmentMasterId int OUTPUT
	,@VoucherNumber varchar(20)
	,@AdjustmentDate date
	,@linktoDepartmentMasterId smallint
	,@Remark varchar(250) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posStockAdjustmentMaster
	(
		 VoucherNumber
		,AdjustmentDate
		,linktoDepartmentMasterId
		,Remark
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @VoucherNumber
		,@AdjustmentDate
		,@linktoDepartmentMasterId
		,@Remark
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @StockAdjustmentMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStockAdjustmentMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStockAdjustmentMaster_Select]
	 @StockAdjustmentMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posStockAdjustmentMaster.*
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department
	FROM
		 posStockAdjustmentMaster
	WHERE
		StockAdjustmentMasterId = @StockAdjustmentMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStockAdjustmentMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStockAdjustmentMaster_SelectAll]
	 @VoucherNumber varchar(20)
	,@AdjustmentDate date = NULL

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posStockAdjustmentMaster.*
		,(SELECT DepartmentName FROM posDepartmentMaster WHERE DepartmentMasterId = linktoDepartmentMasterId) AS Department		
	FROM
		 posStockAdjustmentMaster
	WHERE
		VoucherNumber LIKE @VoucherNumber + '%'
		AND CONVERT(varchar(8), AdjustmentDate, 112) = CONVERT(varchar(8), @AdjustmentDate, 112)

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posStockAdjustmentMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posStockAdjustmentMaster_Update]
	 @StockAdjustmentMasterId int
	,@VoucherNumber varchar(20)
	,@AdjustmentDate date
	,@linktoDepartmentMasterId smallint
	,@Remark varchar(250) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posStockAdjustmentMaster
	SET
		 VoucherNumber = @VoucherNumber
		,AdjustmentDate = @AdjustmentDate
		,linktoDepartmentMasterId = @linktoDepartmentMasterId
		,Remark = @Remark
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		StockAdjustmentMasterId = @StockAdjustmentMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierItemTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierItemTran_Insert]
	 @linktoSupplierMasterId smallint
	,@linktoItemMasterId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF 

	IF NOT EXISTS(SELECT SupplierItemTranId FROM posSupplierItemTran WHERE linktoSupplierMasterId = @linktoSupplierMasterId AND linktoItemMasterId = @linktoItemMasterId)
	BEGIN
		INSERT INTO posSupplierItemTran
		(
			 linktoSupplierMasterId
			,linktoItemMasterId
		)
		VALUES
		(
			@linktoSupplierMasterId
			,@linktoItemMasterId
		) 
	END

	IF @@ERROR <> 0	 
		SET @Status = -1  
	ELSE 
		SET @Status = 0
 

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierItemTran_InsertAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierItemTran_InsertAll]
	 @linktoSupplierMasterId smallint
	,@linktoItemMasterIds varchar(1000)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	BEGIN TRANSACTION

	DELETE
	FROM
		 posSupplierItemTran
	WHERE
		linktoSupplierMasterId = @linktoSupplierMasterId

	INSERT INTO posSupplierItemTran
	(
		 linktoSupplierMasterId
		,linktoItemMasterId
	)
	SELECT
		 @linktoSupplierMasterId
		,*
	FROM
		 dbo.Parse(@linktoItemMasterIds, ',')

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
		ROLLBACK TRANSACTION
	END
	ELSE
	BEGIN
		SET @Status = 0
		COMMIT TRANSACTION
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierItemTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierItemTran_SelectAll]
	 @linktoSupplierMasterId smallint,
	 @ItemType smallint,
	 @linktoBusinessMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		IM.ItemName,IM.ItemCode,IM.ItemMasterId,IM.ShortName,
		(SELECT CategoryName FROM posCategoryMaster WHERE CategoryMasterId = IM.linktoCategoryMasterId) AS CategoryName,
		(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = IM.linktoUnitMasterId) AS Unit,
		SIT.SupplierItemTranId	
	FROM
		posItemMaster IM
	LEFT JOIN
		posSupplierItemTran SIT ON SIT.linktoItemMasterId = IM.ItemMasterId AND SIT.linktoSupplierMasterId = @linktoSupplierMasterId	
	WHERE
		IM.ItemType = @ItemType and IM.IsEnabled = 1 and IM.IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY CategoryName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierMaster_DeleteAll]
	  @SupplierMasterIds varchar(1000)
	 ,@linktoUserMasterIdUpdatedBy smallint
	 ,@UpdateDateTime datetime
	 ,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@updatedRawCount int

	set @TotalRecords = (SELECT count(*) FROM dbo.Parse(@SupplierMasterIds, ','))


	UPDATE
		posSupplierMaster
	SET IsDeleted = 1,
	    linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy,
		UpdateDateTime = @UpdateDateTime
	WHERE
		SupplierMasterId IN (SELECT * from dbo.Parse(@SupplierMasterIds, ','))
		AND SupplierMasterId NOT IN
		(
			SELECT linktoSupplierMasterId FROM posPurchaseMaster WHERE IsDeleted =0 AND IsEnabled = 1
			AND linktoSupplierMasterId IN (SELECT * from dbo.Parse(@SupplierMasterIds, ','))
		)

	SET @updatedRawCount = @@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		IF @TotalRecords = @updatedRawCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		end
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierMaster_Insert]
	 @SupplierMasterId smallint OUTPUT
	,@ShortName varchar(10)
	,@SupplierName varchar(100)
	,@Description varchar(500) = NULL
	,@ContactPersonName varchar(100) = NULL
	,@Designation varchar(50) = NULL
	,@Address varchar(500) = NULL
	,@linktoCountryMasterId smallint = NULL
	,@linktoStateMasterId smallint = NULL
	,@linktoCityMasterId int = NULL
	,@ZipCode varchar(15) = NULL
	,@Phone1 varchar(15)
	,@Phone2 varchar(15) = NULL
	,@Email1 varchar(80) = NULL
	,@Email2 varchar(80) = NULL
	,@Fax varchar(15) = NULL
	,@IsCredit bit
	,@OpeningBalance money
	,@CreditDays smallint
	,@CreditBalance money
	,@CreditLimit money
	,@Tax money
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT SupplierMasterId FROM posSupplierMaster WHERE ((Phone1=@Phone1) OR (Email1 <> '' AND Email1 = @Email1))  AND IsDeleted = 0 AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @SupplierMasterId = SupplierMasterId FROM posSupplierMaster WHERE ((Phone1=@Phone1) OR (Email1 <> '' AND Email1 = @Email1)) AND IsDeleted = 0 AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posSupplierMaster
	(
		 ShortName
		,SupplierName
		,Description
		,ContactPersonName
		,Designation
		,Address
		,linktoCountryMasterId
		,linktoStateMasterId
		,linktoCityMasterId
		,ZipCode
		,Phone1
		,Phone2
		,Email1
		,Email2
		,Fax
		,IsCredit
		,OpeningBalance
		,CreditDays
		,CreditBalance
		,CreditLimit
		,Tax
		,linktoBusinessMasterId
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @ShortName
		,@SupplierName
		,@Description
		,@ContactPersonName
		,@Designation
		,@Address
		,@linktoCountryMasterId
		,@linktoStateMasterId
		,@linktoCityMasterId
		,@ZipCode
		,@Phone1
		,@Phone2
		,@Email1
		,@Email2
		,@Fax
		,@IsCredit
		,@OpeningBalance
		,@CreditDays
		,@CreditBalance
		,@CreditLimit
		,@Tax
		,@linktoBusinessMasterId
		,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SupplierMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierMaster_Select]
	 @SupplierMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posSupplierMaster.*
		,(SELECT CountryName FROM posCountryMaster WHERE CountryMasterId = linktoCountryMasterId) AS Country
		,(SELECT StateName FROM posStateMaster WHERE StateMasterId = linktoStateMasterId) AS State
		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS City
	FROM
		 posSupplierMaster
	WHERE
		SupplierMasterId = @SupplierMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierMaster_SelectAll]
	 @SupplierName varchar(100)
	,@Phone1 varchar(15)
	,@IsCredit bit = NULL
	,@IsEnabled bit = NULL
	,@linktoBusinessMasterId smallint

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posSupplierMaster.*
		,(SELECT CountryName FROM posCountryMaster WHERE CountryMasterId = linktoCountryMasterId) AS Country		
		,(SELECT StateName FROM posStateMaster WHERE StateMasterId = linktoStateMasterId) AS State		
		,(SELECT CityName FROM posCityMaster WHERE CityMasterId = linktoCityMasterId) AS City		
	FROM
		 posSupplierMaster
	WHERE
		(SupplierName LIKE @SupplierName + '%' OR ContactPersonName LIKE @SupplierName + '%')
		AND (Phone1 LIKE @Phone1 + '%' OR Phone2 LIKE @Phone1 + '%')
		AND ((@IsCredit IS NULL AND (IsCredit IS NULL OR IsCredit IS NOT NULL)) OR (@IsCredit IS NOT NULL AND IsCredit = @IsCredit))
		AND ((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierMaster_Update]
	 @SupplierMasterId smallint
	,@ShortName varchar(10)
	,@SupplierName varchar(100)
	,@Description varchar(500) = NULL
	,@ContactPersonName varchar(100) = NULL
	,@Designation varchar(50) = NULL
	,@Address varchar(500) = NULL
	,@linktoCountryMasterId smallint = NULL
	,@linktoStateMasterId smallint = NULL
	,@linktoCityMasterId int = NULL
	,@ZipCode varchar(15) = NULL
	,@Phone1 varchar(15)
	,@Phone2 varchar(15) = NULL
	,@Email1 varchar(80) = NULL
	,@Email2 varchar(80) = NULL
	,@Fax varchar(15) = NULL
	,@IsCredit bit
	,@OpeningBalance money
	,@CreditDays smallint
	,@CreditBalance money
	,@CreditLimit money
	,@Tax money
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT SupplierMasterId FROM posSupplierMaster WHERE ((Phone1=@Phone1) OR (Email1 <> '' AND Email1 = @Email1)) AND SupplierMasterId != @SupplierMasterId AND IsDeleted = 0 AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posSupplierMaster
	SET
		 ShortName = @ShortName
		,SupplierName = @SupplierName
		,Description = @Description
		,ContactPersonName = @ContactPersonName
		,Designation = @Designation
		,Address = @Address
		,linktoCountryMasterId = @linktoCountryMasterId
		,linktoStateMasterId = @linktoStateMasterId
		,linktoCityMasterId = @linktoCityMasterId
		,ZipCode = @ZipCode
		,Phone1 = @Phone1
		,Phone2 = @Phone2
		,Email1 = @Email1
		,Email2 = @Email2
		,Fax = @Fax
		,IsCredit = @IsCredit
		,OpeningBalance = @OpeningBalance
		,CreditDays = @CreditDays
		,CreditBalance = @CreditBalance
		,CreditLimit = @CreditLimit
		,Tax = @Tax
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		SupplierMasterId = @SupplierMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierMasterSupplierName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierMasterSupplierName_SelectAll]
		@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 SupplierMasterId
		,SupplierName
	FROM
		 posSupplierMaster
	WHERE
		IsEnabled = 1
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY SupplierName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierPaymentTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierPaymentTran_Delete]
	 @SupplierPaymentTranId int
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posSupplierPaymentTran
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		SupplierPaymentTranId = @SupplierPaymentTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierPaymentTran_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierPaymentTran_DeleteAll]
	  @SupplierPaymentIds varchar(1000)
	 ,@linktoUserMasterIdUpdatedBy smallint
	 ,@UpdateDateTime datetime
	 ,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	


	UPDATE
		posSupplierPaymentTran
	SET IsDeleted = 1,
	    linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy,
		UpdateDateTime = @UpdateDateTime
	WHERE
		SupplierPaymentTranId IN (SELECT * from dbo.Parse(@SupplierPaymentIds, ','))
	
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0		
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierPaymentTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierPaymentTran_Insert]
	 @SupplierPaymentTranId int OUTPUT
	,@linktoSupplierMasterId smallint
	,@linktoPaymentTypeMasterId smallint
	,@linktoPurchaseMasterId int = NULL
	,@PaymentDate date
	,@ReceiptNumber varchar(20)
	,@Amount money
	,@Remark varchar(250) = NULL
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posSupplierPaymentTran
	(
		 linktoSupplierMasterId
		,linktoPaymentTypeMasterId
		,linktoPurchaseMasterId
		,PaymentDate
		,ReceiptNumber
		,Amount
		,Remark
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		
	)
	VALUES
	(
		 @linktoSupplierMasterId
		,@linktoPaymentTypeMasterId
		,@linktoPurchaseMasterId
		,@PaymentDate
		,@ReceiptNumber
		,@Amount
		,@Remark
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @SupplierPaymentTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierPaymentTran_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierPaymentTran_Select]
	 @SupplierPaymentTranId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posSupplierPaymentTran.*
		,(SELECT SupplierName FROM posSupplierMaster WHERE SupplierMasterId = linktoSupplierMasterId) AS Supplier
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType
		,(SELECT VoucherNumber FROM posPurchaseMaster WHERE PurchaseMasterId = linktoPurchaseMasterId) AS Purchase
	FROM
		 posSupplierPaymentTran
	WHERE
		SupplierPaymentTranId = @SupplierPaymentTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierPaymentTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posSupplierPaymentTran_SelectAll 1,'20150616' ,'20160616',0,''
CREATE PROCEDURE [dbo].[posSupplierPaymentTran_SelectAll]
@linktoSupplierMasterId smallint,
@PaymentDate datetime,
@PaymentToDate datetime,
@linktoPurchaseMasterId int,
@ReceiptNumber varchar(20)

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posSupplierPaymentTran.*
		,(SELECT SupplierName FROM posSupplierMaster WHERE SupplierMasterId = linktoSupplierMasterId) AS Supplier		
		,(SELECT PaymentType FROM posPaymentTypeMaster WHERE PaymentTypeMasterId = linktoPaymentTypeMasterId) AS PaymentType		
		,(SELECT VoucherNumber FROM posPurchaseMaster WHERE PurchaseMasterId = linktoPurchaseMasterId) AS Purchase
	FROM
		 posSupplierPaymentTran
	WHERE
			ReceiptNumber like '%' + @ReceiptNumber +'%'
			AND
			CONVERT(varchar(8),PaymentDate,112) BETWEEN CONVERT(varchar(8), @PaymentDate,112) AND CONVERT(varchar(8), @PaymentToDate,112)
			AND
			((@linktoPurchaseMasterId = 0 and linktoPurchaseMasterId IS NULL) OR (@linktoPurchaseMasterId != 0 and linktoPurchaseMasterId = @linktoPurchaseMasterId))
			AND
			linktoSupplierMasterId=@linktoSupplierMasterId

	AND
		IsDeleted = 0
	ORDER BY SupplierPaymentTranId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posSupplierPaymentTran_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posSupplierPaymentTran_Update]
	 @SupplierPaymentTranId int
	,@linktoSupplierMasterId smallint
	,@linktoPaymentTypeMasterId smallint
	,@linktoPurchaseMasterId int = NULL
	,@PaymentDate date
	,@ReceiptNumber varchar(20)
	,@Amount money
	,@Remark varchar(250) = NULL
	,@IsDeleted bit
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posSupplierPaymentTran
	SET
		 linktoSupplierMasterId = @linktoSupplierMasterId
		,linktoPaymentTypeMasterId = @linktoPaymentTypeMasterId
		,linktoPurchaseMasterId = @linktoPurchaseMasterId
		,PaymentDate = @PaymentDate
		,ReceiptNumber = @ReceiptNumber
		,Amount = @Amount
		,Remark = @Remark
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		
	WHERE
		SupplierPaymentTranId = @SupplierPaymentTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableAmentitiesTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableAmentitiesTran_Insert]
	 @AmentitiesIds varchar(1000)
	,@linktoTableMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	DELETE 
		FROM 
			posTableAmentitiesTran 
		WHERE
			linktoTableMasterId = @linktoTableMasterId 
	
	INSERT INTO posTableAmentitiesTran
	(
		 linktoTableMasterId
		,linktoAmentitiesMasterId
	)
	 SELECT @linktoTableMasterId, parseValue FROM  dbo.Parse(@AmentitiesIds,',') 

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN 
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableAmentitiesTranBylinktoTableMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create PROCEDURE [dbo].[posTableAmentitiesTranBylinktoTableMasterId_SelectAll]
	 
	@linktoTableMasterId smallint  
	 

AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posTableAmentitiesTran.*
	FROM
		 posTableAmentitiesTran
	WHERE
		 linktoTableMasterId = @linktoTableMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posTableMaster_DeleteAll]
	 @TableMasterIds varchar(1000)
	,@StatusUpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posTableMaster
	SET
		 IsDeleted = 1
		,StatusUpdateDateTime = @StatusUpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		TableMasterId IN (SELECT * from dbo.Parse(@TableMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END

GO
/****** Object:  StoredProcedure [dbo].[posTableMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posTableMaster_Insert]
	 @TableMasterId smallint OUTPUT
	,@TableName varchar(50) = NULL
	,@ShortName varchar(5) = NULL
	,@Description varchar(500) = NULL
	,@MinPerson smallint
	,@MaxPerson smallint
	,@linktoTableStatusMasterId smallint
	,@linktoOrderTypeMasterId smallint	
	,@OriginX int
	,@OriginY int
	,@Height numeric(10, 2)
	,@Width numeric(10, 2)
	,@TableColor varchar(6) = NULL
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@IsBookingAvailable bit
	,@HourlyBookingRate money= null
	,@DailyBookingRate money= null
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT TableMasterId FROM posTableMaster WHERE TableName = @TableName AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsDeleted = 0) 
	BEGIN
		SELECT @TableMasterId = TableMasterId FROM posTableMaster WHERE TableName = @TableName  AND linktoBusinessMasterId=@linktoBusinessMasterId AND IsDeleted = 0
		SET @Status = -2
		RETURN
	END
	INSERT INTO posTableMaster
	(
		 TableName
		,ShortName
		,Description
		,MinPerson
		,MaxPerson
		,linktoTableStatusMasterId
		,linktoOrderTypeMasterId		
		,OriginX
		,OriginY
		,Height
		,Width
		,TableColor
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoBusinessMasterId
		,IsEnabled
		,IsBookingAvailable
		,HourlyBookingRate
		,DailyBookingRate
		
	)
	VALUES
	(
		 @TableName
		,@ShortName
		,@Description
		,@MinPerson
		,@MaxPerson
		,@linktoTableStatusMasterId
		,@linktoOrderTypeMasterId		
		,@OriginX
		,@OriginY
		,@Height
		,@Width
		,@TableColor
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoBusinessMasterId
		,@IsEnabled
		,@IsBookingAvailable
		,@HourlyBookingRate
		,@DailyBookingRate
		
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @TableMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableMaster_Select]
	 @TableMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posTableMaster.*
		,(SELECT TableStatus FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS TableStatus
		,(SELECT StatusColor FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS StatusColor		
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS Waiter	
		,(SELECT  WaiterName FROM posWaiterMaster WHERE WaiterMasterId=linktoCaptainMasterId)AS Captain
	FROM
		 posTableMaster
	WHERE
		TableMasterId = @TableMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--  posTableMaster_SelectAll null,null,null,null,1,null,NULL
CREATE PROCEDURE [dbo].[posTableMaster_SelectAll]
	 @TableName varchar(50) = NULL
	,@MinPerson smallint = NULL
	,@MaxPerson smallint = NULL	
	,@IsEnabled bit
	,@linktoTableStatusMasterId smallint = NULL
	,@IsBookingAvailable bit= null
	,@linktoBusinessMasterId smallint
	,@linktoOrderTypeMasterId smallint = NULL
AS
BEGIN

	SET NOCOUNT ON
	
	SELECT posTableMaster.*
		,(SELECT TableStatus FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS TableStatus					
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS Waiter	
		,(SELECT StatusColor FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS StatusColor
		,(SELECT  OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId=linktoOrderTypeMasterId) AS OrderType
		,(SELECT  WaiterName FROM posWaiterMaster WHERE WaiterMasterId=linktoCaptainMasterId)AS CaptainName
	FROM
			posTableMaster
	WHERE
		ISNULL(TableName,'') LIKE ISNULL(@TableName,'') + '%'
		AND MinPerson = ISNULL(@MinPerson, MinPerson)
		AND MaxPerson = ISNULL(@MaxPerson, MaxPerson)		
		And IsEnabled = ISNULL(@IsEnabled, IsEnabled)
		and linktoTableStatusMasterId = ISNULL(@linktoTableStatusMasterId, linktoTableStatusMasterId)
		AND IsBookingAvailable = ISNULL(@IsBookingAvailable, IsBookingAvailable)
		AND linktoOrderTypeMasterId = ISNULL(@linktoOrderTypeMasterId, linktoOrderTypeMasterId)
		AND linktoBusinessMasterId=@linktoBusinessMasterId
		AND IsDeleted = 0
		ORDER BY TableName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableMaster_Update]
	 @TableMasterId smallint
	,@TableName varchar(50) = NULL
	,@ShortName varchar(5) = NULL
	,@Description varchar(500) = NULL
	,@MinPerson smallint
	,@MaxPerson smallint
	,@linktoOrderTypeMasterId smallint	
	,@OriginX int
	,@OriginY int
	,@Height numeric(10, 2)
	,@Width numeric(10, 2)
	,@TableColor varchar(6) = NULL
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@IsBookingAvailable bit
	,@HourlyBookingRate money = NULL
	,@DailyBookingRate money = NULL
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT TableName FROM posTableMaster WHERE TableName = @TableName AND TableMasterId != @TableMasterId AND   linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posTableMaster
	SET
		 TableName = @TableName
		,ShortName = @ShortName
		,Description = @Description
		,MinPerson = @MinPerson
		,MaxPerson = @MaxPerson
		,linktoOrderTypeMasterId = @linktoOrderTypeMasterId		
		,OriginX = @OriginX
		,OriginY = @OriginY
		,Height = @Height
		,Width = @Width
		,TableColor = @TableColor
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoBusinessMasterId = @linktoBusinessMasterId
		,IsEnabled = @IsEnabled
		,IsBookingAvailable=@IsBookingAvailable
		,DailyBookingRate=@DailyBookingRate
		,HourlyBookingRate=@HourlyBookingRate
	WHERE
		TableMasterId = @TableMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMasterBulkUpdate_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableMasterBulkUpdate_Update]
	 @TableMasterId smallint
	,@TableName varchar(50) = NULL
	,@ShortName varchar(5) = NULL 
	,@MinPerson smallint
	,@MaxPerson smallint 
	,@linktoOrderTypeMasterId smallint	
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL 
	,@IsBookingAvailable bit
	,@HourlyBookingRate money= null
	,@DailyBookingRate money= null
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT TableName FROM posTableMaster WHERE TableName = @TableName AND TableMasterId != @TableMasterId AND linktoBusinessMasterId = @linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posTableMaster
	SET
		 TableName = @TableName
		,ShortName = @ShortName 
		,MinPerson = @MinPerson
		,MaxPerson = @MaxPerson 
		,linktoOrderTypeMasterId = @linktoOrderTypeMasterId		
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy 
		,IsBookingAvailable=@IsBookingAvailable
		,DailyBookingRate=@DailyBookingRate
		,HourlyBookingRate=@HourlyBookingRate
	WHERE
		TableMasterId = @TableMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMasterByBusinessMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableMasterByBusinessMasterId_SelectAll]

	@linktoCounterMasterId smallint,
	@linktoTableStatusMasterId smallint = NULL,
	@linktoOrderTypeMasterId smallint = NULL,
	@linktoBusinessMasterId smallint,
	@FromDate datetime
AS
BEGIN

	SET NOCOUNT ON

		SELECT
			 TM.*
			,(SELECT TableStatus FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS TableStatus	
			,(SELECT StatusColor FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS StatusColor		
			,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS Waiter				
			,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business	
			,(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = linktoOrderTypeMasterId) AS OrderType
			,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId=linktoCaptainMasterId)AS CaptainName
			,(SELECT TOP 1 PersonName + '^' + CAST(NoOfPersons AS varchar(10))
				FROM posWaitingMaster WM, posOrderMaster 
				WHERE linktoTableMasterId = TableMasterId 
					AND TableMasterId IN (SELECT * FROM dbo.Parse(linktoTableMasterIds,','))
					AND linktoSalesMasterId IS NULL
					AND CONVERT(varchar(8), OrderDateTime, 112) = CONVERT(varchar(8), @FromDate, 112)
					AND CONVERT(varchar(8), WM.CreateDateTime, 112) = CONVERT(varchar(8), @FromDate, 112)
				ORDER BY WM.CreateDateTime DESC
				) AS WaitingPersonName
		FROM 
			posTableMaster TM INNER JOIN posCounterTableTran CTT on TM.TableMasterId = CTT.linktoTableMasterId
		WHERE 
			CTT.linktoCounterMasterId = @linktoCounterMasterId
			AND TM.linktoTableStatusMasterId = ISNULL(@linktoTableStatusMasterId,linktoTableStatusMasterId)
			AND TM.linktoOrderTypeMasterId = ISNULL(@linktoOrderTypeMasterId,linktoOrderTypeMasterId)
			AND TM.IsEnabled = 1
			AND TM.IsDeleted = 0
			AND TM.linktoOrderTypeMasterId <> 3
			AND linktoBusinessMasterId=@linktoBusinessMasterId

	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posTableMasterByCounterMasterId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableMasterByCounterMasterId_SelectAll]
	@CounterMasterID int
	,@linkToOrderTypeMasterId smallint = null
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SELECT * FROM
	(
		SELECT 
			TM.TableMasterId,
			TM.TableColor,
			TM.TableName,
			TM.MaxPerson,
			TM.ShortName,
			TM.OriginX, 
			TM.OriginY, 
			TM.Width,
			TM.Height,
			TM.TableType,		
			TM.linktoTableStatusMasterId,
			TM.StatusUpdateDateTime,
			linktoWaiterMasterId,
			TM.linktoOrderTypeMasterId,
			(SELECT OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId = TM.linktoOrderTypeMasterId) AS OrderType,
			ISNULL((SELECT WaiterName  FROM posWaiterMaster WHERE WaiterMasterId = TM.linktoWaiterMasterId AND IsEnabled = 1 AND linktoBusinessMasterId=@linktoBusinessMasterId),'') WaiterName,
			(SELECT StatusColor FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS StatusColor
		FROM 
			posTableMaster TM , posCounterTableTran CT
	
		WHERE 
			CT.linktoCounterMasterId = @CounterMasterID
			AND CT.linktoTableMasterId = TM.TableMasterId 
			AND TM.IsEnabled = 1 
			AND TM.IsDeleted = 0
			AND TM.linktoOrderTypeMasterId = CASE WHEN @linkToOrderTypeMasterId = 0 THEN linktoOrderTypeMasterId ELSE ISNULL(@linkToOrderTypeMasterId, linktoOrderTypeMasterId) END		
			AND linktoBusinessMasterId=@linktoBusinessMasterId
	) TempTable 

END

GO
/****** Object:  StoredProcedure [dbo].[posTableMasterbyTableMasterIDs_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableMasterbyTableMasterIDs_SelectAll]
	 @TableMasterIDs varchar(MAX) 
	 ,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON
	
	SELECT posTableMaster.*
		,(SELECT TableStatus FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS TableStatus			
		,(SELECT WaiterName FROM posWaiterMaster WHERE WaiterMasterId = linktoWaiterMasterId) AS Waiter	
		,(SELECT BusinessName FROM posBusinessMaster WHERE BusinessMasterId = linktoBusinessMasterId) AS Business
		,(SELECT StatusColor FROM posTableStatusMaster WHERE TableStatusMasterId = linktoTableStatusMasterId) AS StatusColor
		,(SELECT  OrderType FROM posOrderTypeMaster WHERE OrderTypeMasterId=linktoOrderTypeMasterId) AS OrderType
		,(SELECT  WaiterName FROM posWaiterMaster WHERE WaiterMasterId=linktoCaptainMasterId)AS CaptainName
	FROM
			posTableMaster
	WHERE
		TableMasterId IN (SELECT Parsevalue FROM dbo.parse(@TableMasterIDs,','))
		AND linktoBusinessMasterId=@linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMasterDesign_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posTableMasterDesign_Update]

	 @TableMasterId smallint
	,@OriginX int
	,@OriginY int
	,@Height numeric(10, 2)
	,@Width numeric(10, 2)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posTableMaster
	SET
		 OriginX = @OriginX
		,OriginY = @OriginY
		,Height = @Height
		,Width = @Width
		
	WHERE
		TableMasterId = @TableMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMasterTableName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableMasterTableName_SelectAll]
		@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 TableMasterId
		,TableName
	FROM
		 posTableMaster
	WHERE
		IsEnabled = 1
		AND IsDeleted = 0
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY TableName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMasterTableStatus_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posTableMasterTableStatus_Update]
	 @TableMasterId smallint
	,@linktoTableStatusMasterId smallint
	,@StatusUpdateDateTime datetime
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	DECLARE @TempTableStatusMasterId smallint
			,@TempOrderTypeMasterId smallint
			,@Count smallint

	SELECT @TempTableStatusMasterId = linktoTableStatusMasterId FROM posTableMaster WHERE TableMasterId = @TableMasterId
	IF (@TempTableStatusMasterId != @linktoTableStatusMasterId)
	BEGIN
	-- TableStatus: 1-Vacant, 2-Occupied, 3-Dirty, 4-Block
	-- OrderType: 1-Dine In, 2-Take Away, 3-Home Delivery
		IF (@linktoTableStatusMasterId = 3)
		BEGIN
			SELECT @TempOrderTypeMasterId = linktoOrderTypeMasterId  FROM posTableMaster WHERE TableMasterId = @TableMasterId
			IF (@TempOrderTypeMasterId != 1)
			BEGIN
				UPDATE posTableMaster
				SET
					linktoTableStatusMasterId = 1,
					StatusUpdateDateTime = @StatusUpdateDateTime
				WHERE
					TableMasterId = @TableMasterId
			END
			ELSE
			BEGIN
				SELECT @Count = COUNT(*) FROM posOrderMaster WHERE linktoTableMasterIds = CAST(@TableMasterId AS varchar(50)) AND linktoSalesMasterId IS NULL
				IF (@Count = 0)
				BEGIN
					UPDATE posTableMaster
					SET
						linktoTableStatusMasterId = @linktoTableStatusMasterId,
						StatusUpdateDateTime = @StatusUpdateDateTime
					WHERE
						TableMasterId = @TableMasterId
				END
			END
		END
		ELSE
		BEGIN
			UPDATE posTableMaster
			SET
				linktoTableStatusMasterId = @linktoTableStatusMasterId,
				StatusUpdateDateTime = @StatusUpdateDateTime
			WHERE
				TableMasterId = @TableMasterId
		END
	END

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableMasterWaiter_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posTableMasterWaiter_Update]
	 @TableMasterId smallint
	,@linktoWaiterMasterId int
	,@linktoCaptainMasterId smallint

	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	UPDATE posTableMaster
	SET
		linktoWaiterMasterId = @linktoWaiterMasterId
		,linktoCaptainMasterId = @linktoCaptainMasterId
	WHERE
		TableMasterId = @TableMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTableStatusMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTableStatusMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON
	
	SELECT posTableStatusMaster.*
	FROM
		posTableStatusMaster

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTaxMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTaxMaster_Insert]
	 @TaxMasterId smallint OUTPUT
	,@TaxName varchar(50)
	,@TaxIndex smallint
	,@TaxRate numeric(5,2)
	,@linktoBusinessMasterId smallint
	,@IsEnabled bit
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	Declare @ID as smallint

	SELECT @ID = TaxMasterId FROM posTaxMaster WHERE linktoBusinessMasterId = @linktoBusinessMasterId 
	AND TaxIndex = @TaxIndex

	IF @ID > 0
	BEGIN
		UPDATE posTaxMaster
		SET
			 TaxName = @TaxName
			,TaxRate = @TaxRate
			,TaxIndex = @TaxIndex
			,linktoBusinessMasterId = @linktoBusinessMasterId
			,IsEnabled = @IsEnabled
			,UpdateDateTime = @CreateDateTime
			,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdCreatedBy
		
		WHERE
			TaxMasterId = @ID

		SET @TaxMasterId = @ID
	END
	ELSE
	BEGIN
		INSERT INTO posTaxMaster
		(
			 TaxName
			,TaxIndex
			,TaxRate
			,linktoBusinessMasterId
			,IsEnabled
			,CreateDateTime
			,linktoUserMasterIdCreatedBy
		
		)
		VALUES
		(
			 @TaxName
			,@TaxIndex
			,@TaxRate
			,@linktoBusinessMasterId
			,@IsEnabled
			,@CreateDateTime
			,@linktoUserMasterIdCreatedBy
		
		)

		SET @TaxMasterId = @@IDENTITY
	END

	IF (@IsEnabled = 0)
	BEGIN
		DECLARE @Sql varchar(100)
		SET @Sql = 'UPDATE posItemRateTran SET Tax' + CAST(@TaxIndex AS varchar) + ' = 0'
		EXEC(@Sql)
	END

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posTaxMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posTaxMaster_SelectAll]
	 @linktoBusinessMasterId smallint
	,@IsEnabled bit = NULL
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		TaxMasterId,TaxName,TaxIndex,
		TaxRate,linktoBusinessMasterId,IsEnabled,CreateDateTime,linktoUserMasterIdUpdatedBy,linktoUserMasterIdCreatedBy,UpdateDateTime
	FROM
		 posTaxMaster
	WHERE
		linktoBusinessMasterId = @linktoBusinessMasterId
		AND IsEnabled = ISNULL(@IsEnabled, IsEnabled)

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitConversionTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUnitConversionTran_Delete]
	 @UnitConversionTranId int
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		posUnitConversionTran
	WHERE
		UnitConversionTranId = @UnitConversionTranId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitConversionTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUnitConversionTran_Insert]
	 @UnitConversionTranId int OUTPUT
	,@linktoItemMasterId int = NULL
	,@linktoUnitMasterIdFrom smallint
	,@linktoUnitMasterIdTo smallint
	,@Quantity numeric(9, 2)
	,@Status smallint OUTPUT
AS
BEGIN
SET NOCOUNT OFF

IF EXISTS(SELECT UnitConversionTranId FROM posUnitConversionTran WHERE linktoUnitMasterIdTo= @linktoUnitMasterIdTo AND linktoItemMasterId=@linktoItemMasterId)
	BEGIN
		SELECT @UnitConversionTranId = UnitConversionTranId FROM posUnitConversionTran WHERE linktoUnitMasterIdTo = @linktoUnitMasterIdTo AND linktoItemMasterId=@linktoItemMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posUnitConversionTran
	(
		 linktoItemMasterId
		,linktoUnitMasterIdFrom
		,linktoUnitMasterIdTo
		,Quantity
	)
	VALUES
	(
		 @linktoItemMasterId
		,@linktoUnitMasterIdFrom
		,@linktoUnitMasterIdTo
		,@Quantity
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @UnitConversionTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitConversionTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUnitConversionTran_SelectAll]
@linktoItemMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posUnitConversionTran.*
		,(SELECT ItemName FROM posItemMaster WHERE ItemMasterId = linktoItemMasterId) AS Item		
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterIdFrom) AS UnitFrom		
		,(SELECT UnitName FROM posUnitMaster WHERE UnitMasterId = linktoUnitMasterIdTo) AS UnitTo
	FROM
		 posUnitConversionTran
	WHERE
		linktoItemMasterId=@linktoItemMasterId	

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitConversionTranUnitNames_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posUnitConversionTranUnitNames_SelectAll 196

CREATE  PROCEDURE [dbo].[posUnitConversionTranUnitNames_SelectAll]
@linktoItemMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT DISTINCT 
		UnitMasterId,UnitName
	FROM
	(
			SELECT 
				UnitMasterId, UnitName		
			FROM
				posItemMaster IM
			JOIN 
				posUnitMaster UM ON UM.UnitMasterId = IM.linktoUnitMasterId			
			WHERE
				ItemMasterId = @linktoItemMasterId AND IM.IsEnabled = 1 AND IM.IsDeleted = 0
		UNION ALL
			SELECT 
				UnitMasterId, UnitName		
			FROM
				posUnitMaster, posUnitConversionTran
			WHERE
				linktoItemMasterId = @linktoItemMasterId	
				AND (linktoUnitMasterIdTo = UnitMasterId OR linktoUnitMasterIdFrom = UnitMasterId)
				AND IsEnabled = 1
				AND IsDeleted = 0
	)TempTable
		 

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUnitMaster_DeleteAll]
	 @UnitMasterIds varchar(1000)
	,@Status smallint OUTPUT

AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRawCount int

	UPDATE
		posUnitMaster
	SET IsDeleted = 1
	WHERE
		UnitMasterId IN (SELECT * from dbo.Parse(@UnitMasterIds, ','))
		AND UnitMasterId NOT IN
		(
			SELECT linktoUnitMasterId FROM posItemMaster WHERE IsDeleted = 0 AND IsEnabled = 1 
			AND linktoUnitMasterId IN (SELECT * from dbo.Parse(@UnitMasterIds, ','))
		)

		SET @UpdatedRawCount = @@ROWCOUNT

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@UnitMasterIds, ','))
		IF @TotalRecords = @UpdatedRawCount
		BEGIN
			SET @Status = 0
		END
		ELSE
		BEGIN
			SET @Status = -2
		END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posUnitMaster_Insert]
	 @UnitMasterId smallint OUTPUT
	,@UnitName varchar(15)
	,@ShortName varchar(5)
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoBusinessMasterId smallint
	,@linktoUserMasterIdCreatedBy smallint
	,@CreateDateTime datetime
	,@Status smallint OUTPUT
	
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT UnitMasterId FROM posUnitMaster WHERE UnitName = @UnitName AND linktoBusinessMasterId = @linktoBusinessMasterId AND IsDeleted = 0)
	BEGIN
		SELECT @UnitMasterId = UnitMasterId FROM posUnitMaster WHERE UnitName = @UnitName AND linktoBusinessMasterId = @linktoBusinessMasterId AND IsDeleted = 0
		SET @Status = -2
		RETURN
	END

	INSERT INTO posUnitMaster
	(
		 UnitName
		,ShortName
		,Description
		,IsEnabled
		,IsDeleted
		,linktoBusinessMasterId
		,linktoUserMasterIdCreatedBy
		,CreateDateTime
	)
	VALUES
	(
		 @UnitName
		,@ShortName
		,@Description
		,@IsEnabled
		,@IsDeleted
		,@linktoBusinessMasterId
		,@linktoUserMasterIdCreatedBy
		,@CreateDateTime
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @UnitMasterId = @@IDENTITY
		SET @Status = 0
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUnitMaster_Select]
	 @UnitMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posUnitMaster.*
	FROM
		 posUnitMaster
	WHERE
		UnitMasterId = @UnitMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUnitMaster_SelectAll]
	 @IsEnabled bit = NULL,
	 @linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posUnitMaster.*
	FROM
		 posUnitMaster
	WHERE
		((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND IsDeleted = 0 AND linktoBusinessMasterId = @linktoBusinessMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUnitMaster_Update]
	 @UnitMasterId smallint
	,@UnitName varchar(15)
	,@ShortName varchar(5)
	,@Description varchar(500) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoBusinessMasterId smallint
	,@linktoUserMasterIdUpdatedBy smallint
	,@UpdateDateTime datetime
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT UnitMasterId FROM posUnitMaster WHERE UnitName = @UnitName AND UnitMasterId != @UnitMasterId AND linktoBusinessMasterId = @linktoBusinessMasterId AND IsDeleted = 0)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posUnitMaster
	SET
		 UnitName = @UnitName
		,ShortName = @ShortName
		,Description = @Description
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,linktoUserMasterIdUpdatedBy=@linktoUserMasterIdUpdatedBy
		,UpdateDateTime=@UpdateDateTime
	WHERE
		UnitMasterId = @UnitMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUnitMasterUnitName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posUnitMasterUnitName_SelectAll]
	@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 UnitMasterId
		,UnitName
	FROM
		 posUnitMaster
	WHERE
		IsDeleted = 0
		and IsEnabled = 1 AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY UnitName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUserCounterTran_Delete]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserCounterTran_Delete]
	 @linktoUserMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DELETE
	FROM
		 posUserCounterTran
	WHERE
		linktoUserMasterId = @linktoUserMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUserCounterTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserCounterTran_Insert]
	 @UserCounterTranId int OUTPUT
	,@linktoUserMasterId smallint
	,@linktoCounterMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posUserCounterTran
	(
		 linktoUserMasterId
		,linktoCounterMasterId
	)
	VALUES
	(
		 @linktoUserMasterId
		,@linktoCounterMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @UserCounterTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUserCounterTran_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO





CREATE PROCEDURE [dbo].[posUserCounterTran_SelectAll]
	@linktoUserMasterId smallint
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		CM.CounterMasterId,CM.CounterName,UCT.UserCounterTranId,linktoUserMasterId		
	FROM
		posCounterMaster CM
	LEFT JOIN
		posUserCounterTran UCT ON UCT.linktoCounterMasterId = CM.CounterMasterId AND UCT.linktoUserMasterId = @linktoUserMasterId
	WHERE
		CM.IsDeleted = 0 AND CM.IsEnabled = 1 AND CM.linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY CounterName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUserCounterTranUserWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- posUserCounterTran_SelectAll 1

create PROCEDURE [dbo].[posUserCounterTranUserWise_SelectAll]
	@linktoUserMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	 SELECT
		*
		,(SELECT CounterName FROM posCounterMaster WHERE CounterMasterId = linktoCounterMasterId) AS CounterName 
	  FROM 
		posUserCounterTran
	  WHERE 
	  linktoUserMasterId = @linktoUserMasterId
	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUserMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserMaster_DeleteAll]
	 @UserMasterIds varchar(1000)
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE
		posUserMaster
	SET
		 IsDeleted = 1
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		UserMasterId IN (SELECT * from dbo.Parse(@UserMasterIds, ','))

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END

GO
/****** Object:  StoredProcedure [dbo].[posUserMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posUserMaster_Insert]
	 @UserMasterId smallint OUTPUT
	,@Username nvarchar(50)
	,@Password nvarchar(25)
	,@linktoRoleMasterId smallint
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@LastLoginDateTime datetime = NULL
	,@LoginFailCount smallint
	,@LastLockoutDateTime datetime = NULL
	,@LastPasswordChangedDateTime datetime = NULL
	,@Comment nvarchar(100) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoBusinessMasterId smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT UserMasterId FROM posUserMaster WHERE Username = @Username AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SELECT @UserMasterId = UserMasterId FROM posUserMaster WHERE Username = @Username AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posUserMaster
	(
		 Username
		,Password
		,linktoRoleMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,LastLoginDateTime
		,LoginFailCount
		,LastLockoutDateTime
		,LastPasswordChangedDateTime
		,Comment
		,IsEnabled
		,IsDeleted
		,linktoBusinessMasterId
		
	)
	VALUES
	(
		 @Username
		,@Password
		,@linktoRoleMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@LastLoginDateTime
		,@LoginFailCount
		,@LastLockoutDateTime
		,@LastPasswordChangedDateTime
		,@Comment
		,@IsEnabled
		,@IsDeleted
		,@linktoBusinessMasterId
		
	)

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
	BEGIN
		SET @UserMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posUserMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserMaster_Select]
	 @UserMasterId smallint
AS
BEGIN
	SET NOCOUNT ON

	SELECT 
		 posUserMaster.*
		 ,(SELECT Role FROM posRoleMaster WHERE RoleMasterId = linktoRoleMasterId) AS Role
		 ,(SELECT WaiterMasterId FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId) AS WaiterMasterId
		 ,(SELECT WaiterType FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId) AS linktoUserTypeMasterId
	FROM
		posUserMaster
	WHERE
		UserMasterId = @UserMasterId

	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posUserMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 
CREATE PROCEDURE [dbo].[posUserMaster_SelectAll]
	 @IsEnabled bit
	,@linktoBusinessMasterId smallint 
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		posUserMaster.*
		,(SELECT Role FROM posRoleMaster WHERE RoleMasterId = linktoRoleMasterId) AS Role
		,(SELECT WaiterMasterId FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId) AS WaiterMasterId
		,(SELECT WaiterType FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId) AS linktoUserTypeMasterId
	FROM 
		posUserMaster
	WHERE
		IsDeleted = 0
		AND IsEnabled = @IsEnabled
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY Username

	RETURN
END





GO
/****** Object:  StoredProcedure [dbo].[posUserMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserMaster_Update]
	 @UserMasterId smallint
	,@Username nvarchar(50)
	,@Password nvarchar(25)
	,@linktoRoleMasterId smallint
	,@UpdateDateTime datetime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL	
	,@LastPasswordChangedDateTime datetime = NULL
	,@Comment nvarchar(100) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@linktoBusinessMasterId smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DECLARE @Pwd nvarchar(25) 
	
	SELECT @pwd = Password FROM posUserMaster WHERE  UserMasterId = @UserMasterId AND  linktoBusinessMasterId=@linktoBusinessMasterId
	IF @Pwd <>@Password
	BEGIN
		UPDATE 
				posUserMaster
		SET 
				LastPasswordChangedDateTime=GETDATE()
		WHERE 
		  UserMasterId = @UserMasterId AND  linktoBusinessMasterId=@linktoBusinessMasterId			
	END

	IF EXISTS(SELECT UserMasterId FROM posUserMaster WHERE Username = @Username AND UserMasterId != @UserMasterId AND  linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posUserMaster
	SET
		 Username = @Username
		,Password = @Password
		,linktoRoleMasterId = @linktoRoleMasterId
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy		
		,Comment = @Comment
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,linktoBusinessMasterId=@linktoBusinessMasterId
	WHERE
		UserMasterId = @UserMasterId

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posUserMasterByPassword_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posUserMasterByPassword_Update]
	 @UserMasterId smallint
	,@Password varchar(25)	
	,@OldPassword varchar(25)	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	
	IF EXISTS(SELECT Password FROM posUserMaster WHERE UserMasterId = @UserMasterId AND Password = @OldPassword) OR @OldPassword = 'New'
	BEGIN
		UPDATE posUserMaster
		SET
			Password = @Password
		WHERE
			UserMasterId = @UserMasterId

		IF @@ERROR <> 0
			SET @Status = -1
		ELSE
			BEGIN
				SET @Status = 0

				UPDATE 
					posUserMaster
				SET
					LastPasswordChangedDateTime=GETDATE()
				WHERE
					UserMasterId = @UserMasterId
			END
		RETURN
	END		
	ELSE
	BEGIN
		SET @Status = -3
		RETURN
	END	
END




GO
/****** Object:  StoredProcedure [dbo].[posUserMasterByUsername_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 -- posUserMasterByUsername_Select 'gg',1
CREATE PROCEDURE [dbo].[posUserMasterByUsername_Select]
	@Username varchar(80)
	,@linktoBusinessMasterId smallint = NULL
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		posUserMaster.*
		,(SELECT Role FROM posRoleMaster WHERE RoleMasterId = linktoRoleMasterId) AS Role
		,(SELECT posBusinessTypeMaster.BusinessTypeMasterId FROM posBusinessTypeMaster WHERE linktoBusinessMasterId = posUserMaster.linktoBusinessMasterId) AS BusinessTypeMasterId
		,(SELECT WaiterMasterId FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId) AS WaiterMasterId
		,(SELECT BusinessName FROM posBusinessMaster WHERE linktoBusinessMasterId = BusinessMasterId) AS BusinessName
		,(SELECT linktoCityMasterId FROM posBusinessMaster WHERE linktoBusinessMasterId = BusinessMasterId) AS linktoCityMasterId
		,(SELECT WaiterType FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId) AS linktoUserTypeMasterId
	FROM
		posUserMaster
	WHERE
		Username = @Username
		AND IsEnabled = 1
		AND IsDeleted = 0
		AND linktoBusinessMasterId = ISNULL(@linktoBusinessMasterId, linktoBusinessMasterId)
		
	RETURN
END






GO
/****** Object:  StoredProcedure [dbo].[posUserMasterLastLoginDatetime_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserMasterLastLoginDatetime_Update]
	 @UserMasterId smallint
	,@linktoBusinessMasterId smallint
	,@LastLoginDateTime datetime
	,@Status smallint output
AS
BEGIN

	UPDATE 
		posUserMaster
	SET 
		LastLoginDateTime = @LastLoginDateTime,
		LoginFailCount = 0
	WHERE 
		UserMasterId = @UserMasterId 
		AND linktoBusinessMasterId = @linktoBusinessMasterId

	IF @@ERROR <> 0
		SET @Status =- 1
	ELSE
		SET @Status = 0

END



GO
/****** Object:  StoredProcedure [dbo].[posUserMasterLastLoginFailCount_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserMasterLastLoginFailCount_Update]
  @UserMasterId smallint
 ,@linktoBusinessMasterId smallint
 ,@Status smallint output
AS
BEGIN
	SET NOCOUNT ON;

	DECLARE @LoginCount smallint
	SELECT  @LoginCount=LoginFailCount FROM posUserMaster WHERE UserMasterId=@UserMasterId AND linktoBusinessMasterId=@linktoBusinessMasterId

	IF @LoginCount > 7
		BEGIN
			UPDATE 
				posUserMaster
			SET 			
				LastLockoutDateTime=GETDATE()
			WHERE 
				UserMasterId=@UserMasterId 
				AND linktoBusinessMasterId=@linktoBusinessMasterId
		END

	UPDATE 
		posUserMaster
	SET 
		LoginFailCount=LoginFailCount + 1
	WHERE 
		UserMasterId=@UserMasterId 
		AND linktoBusinessMasterId=@linktoBusinessMasterId

	if @@ERROR<>0
		BEGIN
		set @Status=-1
		END
	ELSE
		BEGIN
		SET @Status=0
		END
END



GO
/****** Object:  StoredProcedure [dbo].[posUserMasterUnblockUser_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserMasterUnblockUser_Update]
  @UserMasterId smallint
 ,@Status smallint output
AS
BEGIN
	SET NOCOUNT ON;
	
			UPDATE 
				posUserMaster
			SET 
				LoginFailCount = 0		
			WHERE 
				UserMasterId = @UserMasterId 

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

END



GO
/****** Object:  StoredProcedure [dbo].[posUserMasterUsername_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserMasterUsername_SelectAll]
    @linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 UserMasterId
		,Username
	FROM
		posUserMaster
	WHERE
		IsDeleted = 0
		AND IsEnabled = 1
		AND linktoBusinessMasterId = @linktoBusinessMasterId
	ORDER BY Username

	RETURN
END






GO
/****** Object:  StoredProcedure [dbo].[posUserMasterWaiterTypeWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posUserMasterWaiterTypeWise_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	-- Captain = 1, Waiter = 2, Delivery_Person = 3
	SELECT
		 UserMasterId
		,Username
		,linktoBusinessMasterId
		,(SELECT WaiterMasterId FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId AND WaiterType = 1 AND IsDeleted = 0) AS WaiterMasterIdCaptian
		,(SELECT WaiterMasterId FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId AND WaiterType = 2 AND IsDeleted = 0) AS WaiterMasterId
		,(SELECT WaiterMasterId FROM posWaiterMaster WHERE linktoUserMasterId = UserMasterId AND WaiterType = 3 AND IsDeleted = 0) AS WaiterMasterIdDeliveryPerson
	FROM
		posUserMaster
	WHERE
		IsDeleted = 0
		AND IsEnabled = 1
	ORDER BY Username

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posUserRightsGroupMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[posUserRightsGroupMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		posUserRightsGroupMaster.*
	FROM 
		posUserRightsGroupMaster

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posUserRightsMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[posUserRightsMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT 
		posUserRightsMaster.*
	FROM 
		posUserRightsMaster

	RETURN
END




GO
/****** Object:  StoredProcedure [dbo].[posUserRightsTran_InsertAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[posUserRightsTran_InsertAll]
	@ids VARCHAR(1000)
	,@linktoRoleMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

    INSERT INTO posUserRightsTran
	(
		linktoUserRightsMasterId
		,linktoRoleMasterId
	)
	SELECT
		TEMP_TABLE.parseValue
		,@linktoRoleMasterId
	FROM
		(SELECT * from dbo.Parse(@ids, ',')) AS TEMP_TABLE

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUserRightsTran_UpdateAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posUserRightsTran_UpdateAll]
	 @ids varchar(1000)
	 ,@linktoRoleMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	DECLARE @deleted varchar(100)
	DECLARE @inserted varchar(100)

	SELECT
		@deleted = COALESCE(@deleted,'') + TEMP.parseValue + ','
	FROM
		(SELECT * from dbo.Parse(@ids, ',')) AS TEMP,
		posUserRightsTran
	WHERE
		linktoUserRightsMasterId = TEMP.parseValue AND
		linktoRoleMasterId = @linktoRoleMasterId

	SELECT
		@inserted = COALESCE(@inserted,'') + TEMP.parseValue + ','
	FROM
		(SELECT * from dbo.Parse(@ids, ',')) AS TEMP
	WHERE
		TEMP.parseValue NOT IN (SELECT * from dbo.Parse(@deleted, ','))

	SET @deleted = LEFT(@deleted, LEN(@deleted) -1)
	SET @inserted = LEFT(@inserted, LEN(@inserted) -1)

	DELETE
	FROM
		posUserRightsTran
	WHERE
		linktoUserRightsMasterId IN (SELECT * from dbo.Parse(@deleted, ',')) AND
		linktoRoleMasterId = @linktoRoleMasterId

	INSERT INTO posUserRightsTran
	(
		linktoUserRightsMasterId
		,linktoRoleMasterId
	)
	SELECT
		TEMP_TABLE.parseValue
		,@linktoRoleMasterId
	FROM
		(SELECT * from dbo.Parse(@inserted, ',')) AS TEMP_TABLE

	IF @@ERROR <> 0
		SET @Status = -1
	ELSE
		SET @Status = 0

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posUserRightsTranRoleWise_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[posUserRightsTranRoleWise_SelectAll]
	@linktoRoleMasterId smallint
AS
BEGIN
	SET NOCOUNT OFF

	SELECT
		*
	FROM
		posUserRightsTran
	WHERE
		linktoRoleMasterId = @linktoRoleMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMaster_DeleteAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterMaster_DeleteAll]
	 @WaiterMasterIds varchar(1000)
	,@Status smallint OUTPUT
	,@WaiterType int
	
AS
BEGIN
	SET NOCOUNT OFF

	Declare @TotalRecords int,@UpdatedRawCount int
	SET @TotalRecords = (SELECT count(*) FROM dbo.Parse(@WaiterMasterIds, ','))

	UPDATE
		posWaiterMaster
	SET IsDeleted = 1
	WHERE
		WaiterMasterId IN (SELECT * from dbo.Parse(@WaiterMasterIds, ','))
		AND WaiterMasterId NOT IN
		(
			SELECT CASE WHEN @WaiterType = 2 THEN linktoWaiterMasterId ELSE linktoCaptainMasterId  END FROM posTableMaster where IsDeleted =0 AND IsEnabled = 1
			AND CASE WHEN @WaiterType = 2 THEN linktoWaiterMasterId ELSE linktoCaptainMasterId  END  IN (SELECT * from dbo.Parse(@WaiterMasterIds, ','))
		)
	
	SET @UpdatedRawCount = @@ROWCOUNT
	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
			if @TotalRecords = @UpdatedRawCount
		begin
			SET @Status = 0
		end
		else
		begin
			SET @Status = -2
		end
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterMaster_Insert]
	 @WaiterMasterId int OUTPUT
	,@ShortName varchar(10)
	,@WaiterName varchar(50)
	--,@linktoUserMasterId int
	,@Description varchar(250) = NULL
	,@IsEnabled bit
	,@IsDeleted bit
	,@CreateDateTime datetime
	,@WaiterType smallint
	,@linktoUserMasterIdCreatedBy smallint
	,@Status smallint OUTPUT
	,@linktoBusinessMasterId smallint
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT WaiterMasterId FROM posWaiterMaster WHERE WaiterName = @WaiterName AND IsDeleted = 0 AND WaiterType = @WaiterType AND linktoBusinessMasterId=@linktoBusinessMasterId)	
	BEGIN
		SELECT @WaiterMasterId = WaiterMasterId FROM posWaiterMaster WHERE WaiterName = @WaiterName AND IsDeleted = 0 AND WaiterType = @WaiterType AND linktoBusinessMasterId=@linktoBusinessMasterId
		SET @Status = -2
		RETURN
	END
	INSERT INTO posWaiterMaster
	(
		 ShortName
		,WaiterName
	--	,linktoUserMasterId
		,Description
		,IsEnabled
		,IsDeleted
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,WaiterType
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @ShortName
		,@WaiterName
	--	,@linktoUserMasterId
		,@Description
		,@IsEnabled
		,@IsDeleted
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@WaiterType
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @WaiterMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMaster_Select]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterMaster_Select]
	 @WaiterMasterId int
AS
BEGIN
	SET NOCOUNT ON

	SELECT
		 posWaiterMaster.*
	FROM
		 posWaiterMaster
	WHERE
		WaiterMasterId = @WaiterMasterId

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- posWaiterMaster_SelectAll 1,2,''
CREATE PROCEDURE [dbo].[posWaiterMaster_SelectAll]
	 @IsEnabled bit = NULL
	,@WaiterType smallint
	,@WaiterName varchar(50) = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posWaiterMaster.*
	FROM
		 posWaiterMaster
	WHERE
		WaiterName like ISNULL(@WaiterName,WaiterName) + '%'
		AND ((@IsEnabled IS NULL AND (IsEnabled IS NULL OR IsEnabled IS NOT NULL)) OR (@IsEnabled IS NOT NULL AND IsEnabled = @IsEnabled))
		AND IsDeleted = 0
		AND WaiterType=@WaiterType
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY WaiterName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterMaster_Update]
	 @WaiterMasterId int
	,@ShortName varchar(10)
	,@WaiterName varchar(50)
--	,@linktoUserMasterId int
	,@Description varchar(250) = NULL
	,@WaiterType smallint
	,@IsEnabled bit
	,@IsDeleted bit
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
	,@linktoBusinessMasterId smallint
AS
BEGIN
	SET NOCOUNT OFF

	IF EXISTS(SELECT WaiterMasterId FROM posWaiterMaster WHERE WaiterName = @WaiterName AND WaiterMasterId != @WaiterMasterId AND IsDeleted = 0 AND WaiterType = @WaiterType AND linktoBusinessMasterId=@linktoBusinessMasterId)
	BEGIN
		SET @Status = -2
		RETURN
	END
	UPDATE posWaiterMaster
	SET
		 ShortName = @ShortName
		,WaiterName = @WaiterName
	--	,linktoUserMasterId = @linktoUserMasterId
		,Description = @Description
		,IsEnabled = @IsEnabled
		,IsDeleted = @IsDeleted
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,linktoBusinessMasterId=@linktoBusinessMasterId
		,WaiterType=@WaiterType
	WHERE
		WaiterMasterId = @WaiterMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMasterByHomeDeliveryOrders_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterMasterByHomeDeliveryOrders_Update]
	 @WaiterMasterId int
	 ,@IsOut bit
	 ,@InOutDateTime datetime
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF 
	 
	UPDATE posWaiterMaster
	SET
		 IsOut = @IsOut
	    ,InOutDateTime = @InOutDateTime
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy 
	WHERE
		WaiterMasterId = @WaiterMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMasterIsOut_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create PROCEDURE [dbo].[posWaiterMasterIsOut_Update]
	 @WaiterMasterId int
	 ,@IsOut bit
	 ,@InOutDateTime datetime
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF 
	 
	UPDATE posWaiterMaster
	SET
		 IsOut = @IsOut
	    ,InOutDateTime = @InOutDateTime
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy 
	WHERE
		WaiterMasterId = @WaiterMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMasterRemoveUsers_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterMasterRemoveUsers_Update]
	@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posWaiterMaster
	SET
		 linktoUserMasterId = NULL

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMasterUserMasterId_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterMasterUserMasterId_Update]
	 @WaiterMasterId int
	,@linktoUserMasterId smallint
	,@UpdateDateTime datetime
	,@linktoUserMasterIdUpdatedBy smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posWaiterMaster
	SET
		 linktoUserMasterId = @linktoUserMasterId
		,UpdateDateTime = @UpdateDateTime
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
	WHERE
		WaiterMasterId = @WaiterMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterMasterWaiterName_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posWaiterMasterWaiterName_SelectAll] 
	@WaiterType smallint = NULL
	,@linktoBusinessMasterId smallint
AS
BEGIN 
	SET NOCOUNT ON 

	SELECT
		 WaiterMasterId
		,WaiterName
		,WaiterType
	FROM
		 posWaiterMaster
	WHERE
		IsEnabled = 1
		AND IsDeleted = 0
		AND WaiterType = ISNULL(@WaiterType, WaiterType)
		AND linktoBusinessMasterId=@linktoBusinessMasterId
	ORDER BY WaiterName

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterNotificationMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterNotificationMaster_Insert]
	 @WaiterNotificationMasterId bigint OUTPUT
	,@NotificationDateTime datetime
	,@linktoTableMasterId smallint
	,@Message varchar(100)
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	INSERT INTO posWaiterNotificationMaster
	(
		 NotificationDateTime
		,linktoTableMasterId
		,Message
	)
	VALUES
	(
		 @NotificationDateTime
		,@linktoTableMasterId
		,@Message
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @WaiterNotificationMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterNotificationMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaiterNotificationMaster_SelectAll]
	@linktoWaiterMasterId smallint
	,@FromDate Datetime
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 NM.*
		 ,TM.ShortName As TableName
		 ,(SELECT TOP 1 WaiterNotificationTranId FROM posWaiterNotificationTran Where linktoWaiterNotificationMasterId = WaiterNotificationMasterId)WaiterNotificationTranId
	FROM
		 posWaiterNotificationMaster NM 
	     JOIN posTableMaster TM on NM.linktoTableMasterId = TM.TableMasterId and TM.linktoWaiterMasterId = @linktoWaiterMasterId
	WHERE
		 CAST(NotificationDateTime AS datetime) >= CAST(@FromDate AS datetime)

	Order By NM.WaiterNotificationMasterId DESC

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaiterNotificationTran_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posWaiterNotificationTran_Insert]
	 @WaiterNotificationTranId bigint OUTPUT
	,@linktoWaiterNotificationMasterId bigint
	,@linktoUserMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	INSERT INTO posWaiterNotificationTran
	(
		 linktoWaiterNotificationMasterId
		,linktoUserMasterId
	)
	VALUES
	(
		 @linktoWaiterNotificationMasterId
		,@linktoUserMasterId

	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @WaiterNotificationTranId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaitingMaster_Insert]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posWaitingMaster_Insert]
	 @WaitingMasterId bigint OUTPUT
	,@PersonName varchar(50)
	,@PersonMobile varchar(10)
	,@NoOfPersons smallint
	,@linktoWaitingStatusMasterId smallint
	,@CreateDateTime datetime
	,@linktoUserMasterIdCreatedBy smallint
	,@linktoBusinessMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	INSERT INTO posWaitingMaster
	(
		 PersonName
		,PersonMobile
		,NoOfPersons
		,linktoWaitingStatusMasterId
		,CreateDateTime
		,linktoUserMasterIdCreatedBy
		,linktoBusinessMasterId
	)
	VALUES
	(
		 @PersonName
		,@PersonMobile
		,@NoOfPersons
		,@linktoWaitingStatusMasterId
		,@CreateDateTime
		,@linktoUserMasterIdCreatedBy
		,@linktoBusinessMasterId
	)

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @WaitingMasterId = @@IDENTITY
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaitingMaster_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaitingMaster_Update]
	 @WaitingMasterId bigint
	,@PersonName varchar(50)
	,@PersonMobile varchar(10)
	,@NoOfPersons smallint
	,@linktoWaitingStatusMasterId smallint
	
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF
	UPDATE posWaitingMaster
	SET
		 PersonName = @PersonName
		,PersonMobile = @PersonMobile
		,NoOfPersons = @NoOfPersons
		,linktoWaitingStatusMasterId = @linktoWaitingStatusMasterId
		
	WHERE
		WaitingMasterId = @WaitingMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaitingMasterByWaitingMasterId_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaitingMasterByWaitingMasterId_Update]
	 @WaitingMasterId bigint
	,@linktoWaitingStatusMasterId smallint
	,@linktoTableMasterId smallint = NULL
	,@UpdateDateTime dateTime = NULL
	,@linktoUserMasterIdUpdatedBy smallint = NULL

	,@Status smallint OUTPUT
AS
BEGIN
	
	SET NOCOUNT OFF

	UPDATE posWaitingMaster
	SET
		linktoWaitingStatusMasterId = @linktoWaitingStatusMasterId
		,linktoTableMasterId = @linktoTableMasterId
		,linktoUserMasterIdUpdatedBy = @linktoUserMasterIdUpdatedBy
		,UpdateDateTime = @UpdateDateTime
		
	WHERE
		WaitingMasterId = @WaitingMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaitingMasterByWaitingStatusId_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaitingMasterByWaitingStatusId_SelectAll]
	 @linktoWaitingStatusMasterId smallint
	,@FromDate Datetime
	,@linktoBusinessMasterId smallint
	,@OrderBy varchar(50)
	
AS
BEGIN

	SET NOCOUNT ON
	
	
		SELECT posWaitingMaster.*
			,(SELECT WaitingStatus FROM posWaitingStatusMaster WHERE WaitingStatusMasterId = linktoWaitingStatusMasterId) AS WaitingStatus		
		FROM 
			posWaitingMaster 
		WHERE 
			linktoWaitingStatusMasterId = @linktoWaitingStatusMasterId AND
			CONVERT(varchar(8), CreateDateTime, 112) = CONVERT(varchar(8), @FromDate, 112)
		AND linktoBusinessMasterId = @linktoBusinessMasterId
		ORDER BY
			 CASE WHEN @OrderBy = 'WaitingMasterId' THEN WaitingMasterId END DESC
			,CASE WHEN @OrderBy = 'UpdateDateTime' THEN UpdateDateTime END DESC
	
	RETURN
END


GO
/****** Object:  StoredProcedure [dbo].[posWaitingMasterTable_Update]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[posWaitingMasterTable_Update]
	 @WaitingMasterId int
	,@linktoTableMasterId smallint
	,@Status smallint OUTPUT
AS
BEGIN
	SET NOCOUNT OFF

	UPDATE posWaitingMaster
	SET
		linktoTableMasterId = @linktoTableMasterId
	WHERE
		WaitingMasterId = @WaitingMasterId

	IF @@ERROR <> 0
	BEGIN
		SET @Status = -1
	END
	ELSE
	BEGIN
		SET @Status = 0
	END

	RETURN
END



GO
/****** Object:  StoredProcedure [dbo].[posWaitingStatusMaster_SelectAll]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[posWaitingStatusMaster_SelectAll]
AS
BEGIN

	SET NOCOUNT ON

	SELECT
		 posWaitingStatusMaster.*
	FROM
		 posWaitingStatusMaster
	

	RETURN
END



GO
/****** Object:  UserDefinedFunction [dbo].[Parse]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE FUNCTION [dbo].[Parse] ( @Array VARCHAR(1000), @separator VARCHAR(10)) 
RETURNS @resultTable TABLE 
	(parseValue VARCHAR(100))
AS
BEGIN

	DECLARE @separator_position INT 
	DECLARE @array_value VARCHAR(1000) 
	
	SET @array = @array + @separator
	
	WHILE patindex('%' + @separator + '%' , @array) <> 0 
	BEGIN
	
	  SELECT @separator_position =  patindex('%' + @separator + '%', @array)
	  SELECT @array_value = left(@array, @separator_position - 1)
	
		INSERT @resultTable
		VALUES (Cast(@array_value AS varchar))

	  SELECT @array = stuff(@array, 1, @separator_position, '')
	END

	RETURN
END






GO
/****** Object:  Table [dbo].[posAccountCategoryMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posAccountCategoryMaster](
	[AccountCategoryMasterId] [int] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NULL,
	[AccountCategoryName] [varchar](50) NOT NULL,
	[Description] [varchar](500) NULL,
	[IsIncome] [bit] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posAccountCategoryMaster] PRIMARY KEY CLUSTERED 
(
	[AccountCategoryMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posAccountMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posAccountMaster](
	[AccountMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[linktoAccountCategoryMasterId] [int] NOT NULL,
	[AccountName] [varchar](100) NOT NULL,
	[AccountNumber] [varchar](15) NOT NULL,
	[OpeningBalance] [money] NOT NULL,
	[IsCredit] [bit] NOT NULL,
	[CreditLimit] [money] NOT NULL,
	[Description] [varchar](250) NULL,
	[Address] [varchar](500) NULL,
	[linktoCountryMasterId] [smallint] NULL,
	[linktoStateMasterId] [smallint] NULL,
	[linktoCityMasterId] [int] NULL,
	[linktoAreaMasterId] [int] NULL,
	[Zipcode] [varchar](10) NULL,
	[Phone1] [varchar](15) NULL,
	[Phone2] [varchar](15) NULL,
	[Fax] [varchar](15) NULL,
	[Email1] [varchar](80) NULL,
	[Email2] [varchar](80) NULL,
	[TIN] [varchar](15) NULL,
	[CST] [varchar](15) NULL,
	[PAN] [varchar](15) NULL,
	[TDS] [varchar](15) NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posAccountMaster] PRIMARY KEY CLUSTERED 
(
	[AccountMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posAddLessItemTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posAddLessItemTran](
	[AddLessItemTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoAddLessMasterId] [smallint] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
 CONSTRAINT [PK_posAddLessItemTran] PRIMARY KEY CLUSTERED 
(
	[AddLessItemTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posAddLessMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posAddLessMaster](
	[AddLessMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](20) NOT NULL,
	[CalculatedOn] [smallint] NOT NULL,
	[Amount] [money] NOT NULL,
	[IsPercentage] [bit] NOT NULL,
	[IsRounding] [bit] NOT NULL,
	[IsSale] [bit] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsForAllItems] [bit] NULL,
 CONSTRAINT [PK_posAddLessMaster] PRIMARY KEY CLUSTERED 
(
	[AddLessMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posAmentitiesMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posAmentitiesMaster](
	[AmentitiesMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[AmentiesName] [varchar](100) NOT NULL,
	[Description] [varchar](500) NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posAmentitiesMaster] PRIMARY KEY CLUSTERED 
(
	[AmentitiesMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posApplicationMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posApplicationMaster](
	[ApplicationMasterId] [tinyint] NOT NULL,
	[ApplicationName] [varchar](50) NOT NULL,
	[Description] [varchar](250) NULL,
	[ApplicationVersion] [varchar](15) NOT NULL,
	[DatabaseVersion] [varchar](15) NOT NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posApplicationMaster] PRIMARY KEY CLUSTERED 
(
	[ApplicationMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posAppThemeMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posAppThemeMaster](
	[AppThemeMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[LogoImageName] [varchar](100) NULL,
	[ProfileImageName] [varchar](100) NULL,
	[WelcomeBackImage] [varchar](100) NULL,
	[BackImageName1] [varchar](100) NULL,
	[BackImageName2] [varchar](100) NULL,
	[ContactMap] [varchar](500) NULL,
	[ColorPrimary] [varchar](8) NULL,
	[ColorPrimaryDark] [varchar](8) NULL,
	[ColorPrimaryLight] [varchar](8) NULL,
	[ColorAccent] [varchar](8) NULL,
	[ColorAccentDark] [varchar](8) NULL,
	[ColorAccentLight] [varchar](8) NULL,
	[ColorTextPrimary] [varchar](8) NULL,
	[ColorFloatingButton] [varchar](8) NULL,
	[ColorButtonRipple] [varchar](8) NULL,
	[ColorCardView] [varchar](8) NULL,
	[ColorCardViewRipple] [varchar](8) NULL,
	[ColorCardText] [varchar](8) NULL,
	[ColorHeaderText] [varchar](8) NULL,
 CONSTRAINT [PK_posAppThemeMaster] PRIMARY KEY CLUSTERED 
(
	[AppThemeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posAreaMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posAreaMaster](
	[AreaMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[AreaName] [varchar](50) NOT NULL,
	[ZipCode] [varchar](10) NOT NULL,
	[linktoCityMasterId] [int] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posAreaMaster] PRIMARY KEY CLUSTERED 
(
	[AreaMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posAudit]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posAudit](
	[AuditId] [int] IDENTITY(1,1) NOT NULL,
	[TableName] [varchar](200) NOT NULL,
	[TablePrimaryKey] [int] NOT NULL,
	[Operation] [varchar](100) NOT NULL,
	[OperationDate] [datetime] NOT NULL,
	[UserId] [int] NOT NULL,
 CONSTRAINT [PK_posAudit] PRIMARY KEY CLUSTERED 
(
	[AuditId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBackupMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBackupMaster](
	[BackupMasterId] [int] IDENTITY(1,1) NOT NULL,
	[BackupDateTime] [datetime] NOT NULL,
	[BackupPath] [varchar](250) NOT NULL,
	[IsAutoBackup] [bit] NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posBackupMaster] PRIMARY KEY CLUSTERED 
(
	[BackupMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBankBookMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBankBookMaster](
	[BankBookMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[linktoAccountMasterIdBank] [int] NOT NULL,
	[BankBookNumber] [varchar](10) NOT NULL,
	[BankBookDate] [date] NOT NULL,
	[VoucherNumber] [varchar](20) NOT NULL,
	[IsPaid] [bit] NOT NULL,
	[linktoAccountMasterId] [int] NOT NULL,
	[Amount] [money] NOT NULL,
	[ChequeNumber] [varchar](10) NULL,
	[linktoBankMasterId] [int] NULL,
	[Remark] [varchar](500) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posBankBookMaster] PRIMARY KEY CLUSTERED 
(
	[BankBookMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBankMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBankMaster](
	[BankMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[ShortName] [varchar](10) NULL,
	[BankName] [varchar](100) NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posBankMaster] PRIMARY KEY CLUSTERED 
(
	[BankMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBookingMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBookingMaster](
	[BookingMasterId] [int] IDENTITY(1,1) NOT NULL,
	[FromDate] [date] NOT NULL,
	[ToDate] [date] NOT NULL,
	[FromTime] [time](7) NULL,
	[ToTime] [time](7) NULL,
	[IsHourly] [bit] NOT NULL,
	[linktoCustomerMasterId] [int] NOT NULL,
	[NoOfAdults] [smallint] NOT NULL,
	[NoOfChildren] [smallint] NOT NULL,
	[TotalAmount] [money] NOT NULL,
	[DiscountPercentage] [smallint] NOT NULL,
	[DiscountAmount] [money] NOT NULL,
	[ExtraAmount] [money] NOT NULL,
	[NetAmount] [money] NOT NULL,
	[PaidAmount] [money] NOT NULL,
	[BalanceAmount] [money] NOT NULL,
	[Remark] [varchar](500) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posBookingMaster] PRIMARY KEY CLUSTERED 
(
	[BookingMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBookingPaymentTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBookingPaymentTran](
	[BookingPaymentTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoBookingMasterId] [int] NOT NULL,
	[linktoPaymentTypeMasterId] [smallint] NULL,
	[linktoCustomerMasterId] [int] NULL,
	[PaymentDateTime] [datetime] NOT NULL,
	[AmountPaid] [money] NOT NULL,
	[Remark] [varchar](250) NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posBookingPaymentTran] PRIMARY KEY CLUSTERED 
(
	[BookingPaymentTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBookingTableTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posBookingTableTran](
	[BookingTableTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBookingMasterId] [int] NOT NULL,
	[linktoTableMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posTempTable] PRIMARY KEY CLUSTERED 
(
	[BookingTableTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posBusinessDescription]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBusinessDescription](
	[BusinessDescriptionId] [smallint] IDENTITY(1,1) NOT NULL,
	[Title] [varchar](250) NOT NULL,
	[Description] [varchar](max) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsDefault] [bit] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[UpdateDateTime] [datetime] NULL,
 CONSTRAINT [PK_posBusinessDescription] PRIMARY KEY CLUSTERED 
(
	[BusinessDescriptionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBusinessFollowersTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posBusinessFollowersTran](
	[BusinessFollowersTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoRegisteredUserMasterId] [int] NOT NULL,
	[CreateDatetime] [datetime] NOT NULL,
	[UnFollowDateTime] [datetime] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posBusinessFollowersTran] PRIMARY KEY CLUSTERED 
(
	[BusinessFollowersTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posBusinessGalleryTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBusinessGalleryTran](
	[BusinessGalleryTranId] [int] IDENTITY(1,1) NOT NULL,
	[ImageTitle] [varchar](50) NULL,
	[ImageName] [varchar](100) NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[SortOrder] [smallint] NULL,
 CONSTRAINT [PK_posBusinessGalleryTran] PRIMARY KEY CLUSTERED 
(
	[BusinessGalleryTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBusinessHoursTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posBusinessHoursTran](
	[BusinessHoursTranId] [smallint] IDENTITY(1,1) NOT NULL,
	[DayOfWeek] [smallint] NOT NULL,
	[OpeningTime] [time](7) NOT NULL,
	[ClosingTime] [time](7) NOT NULL,
	[BreakStartTime] [time](7) NULL,
	[BreakEndTime] [time](7) NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posBusinessHoursTran] PRIMARY KEY CLUSTERED 
(
	[BusinessHoursTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posBusinessInfoAnswerMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBusinessInfoAnswerMaster](
	[BusinessInfoAnswerMasterId] [int] NOT NULL,
	[linktoBusinessInfoQuestionMasterId] [int] NOT NULL,
	[Answer] [varchar](50) NOT NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posBusinessInfoAnswerMaster] PRIMARY KEY CLUSTERED 
(
	[BusinessInfoAnswerMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBusinessInfoAnswerTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBusinessInfoAnswerTran](
	[BusinessInfoAnswerTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[linktoBusinessInfoQuestionMasterId] [int] NOT NULL,
	[linktoBusinessInfoAnswerMasterId] [int] NULL,
	[Answer] [varchar](50) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
 CONSTRAINT [PK_posBusinessInfoAnswerTran] PRIMARY KEY CLUSTERED 
(
	[BusinessInfoAnswerTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBusinessInfoQuestionMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBusinessInfoQuestionMaster](
	[BusinessInfoQuestionMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessTypeMasterId] [smallint] NOT NULL,
	[Question] [varchar](50) NOT NULL,
	[QuestionType] [smallint] NOT NULL,
	[SortOrder] [int] NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posBusinessInfoQuestionMaster] PRIMARY KEY CLUSTERED 
(
	[BusinessInfoQuestionMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBusinessMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBusinessMaster](
	[BusinessMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[BusinessName] [varchar](80) NOT NULL,
	[BusinessShortName] [varchar](10) NULL,
	[Address] [varchar](500) NOT NULL,
	[Phone1] [varchar](15) NOT NULL,
	[Phone2] [varchar](15) NULL,
	[Email] [varchar](80) NULL,
	[Fax] [varchar](15) NULL,
	[Website] [varchar](80) NULL,
	[linktoCountryMasterId] [smallint] NOT NULL,
	[linktoStateMasterId] [smallint] NOT NULL,
	[linktoCityMasterId] [int] NOT NULL,
	[ZipCode] [varchar](10) NOT NULL,
	[ImageName] [varchar](100) NULL,
	[ExtraText] [varchar](100) NULL,
	[TIN] [varchar](15) NULL,
	[TINRegistrationDate] [date] NULL,
	[CST] [varchar](15) NULL,
	[CSTRegistrationDate] [date] NULL,
	[PAN] [varchar](15) NULL,
	[PANRegistrationDate] [date] NULL,
	[TDS] [varchar](15) NULL,
	[TDSRegistrationDate] [date] NULL,
	[linktoBusinessTypeMasterId] [smallint] NOT NULL,
	[UniqueId] [varchar](6) NOT NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posBusinessMaster] PRIMARY KEY CLUSTERED 
(
	[BusinessMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posBusinessTypeMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posBusinessTypeMaster](
	[BusinessTypeMasterId] [smallint] NOT NULL,
	[BusinessType] [varchar](50) NOT NULL,
	[Description] [varchar](500) NULL,
 CONSTRAINT [PK_posBusinessTypeMaster] PRIMARY KEY CLUSTERED 
(
	[BusinessTypeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCashBookMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCashBookMaster](
	[CashBookMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[linktoAccountMasterIdCash] [int] NOT NULL,
	[CashBookNumber] [varchar](10) NOT NULL,
	[CashBookDate] [date] NOT NULL,
	[VoucherNumber] [varchar](20) NOT NULL,
	[IsPaid] [bit] NOT NULL,
	[linktoAccountMasterId] [int] NOT NULL,
	[Amount] [money] NOT NULL,
	[Remark] [varchar](500) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posCashBookMaster] PRIMARY KEY CLUSTERED 
(
	[CashBookMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCategoryMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCategoryMaster](
	[CategoryMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[CategoryName] [varchar](80) NOT NULL,
	[linktoCategoryMasterIdParent] [smallint] NULL,
	[ImageName] [varchar](100) NULL,
	[CategoryColor] [varchar](6) NULL,
	[Description] [varchar](500) NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[SortOrder] [smallint] NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[IsRawMaterial] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[IsHiddenForCustomer] [bit] NOT NULL,
 CONSTRAINT [PK_posCategoryMaster] PRIMARY KEY CLUSTERED 
(
	[CategoryMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCityMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCityMaster](
	[CityMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[CityName] [varchar](50) NOT NULL,
	[CityCode] [varchar](3) NOT NULL,
	[linktoStateMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posCityMaster] PRIMARY KEY CLUSTERED 
(
	[CityMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCounterDayEndTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posCounterDayEndTran](
	[CounterDayEndTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[DayEndDateTime] [datetime] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
 CONSTRAINT [PK_posCounterDayEndTran] PRIMARY KEY CLUSTERED 
(
	[CounterDayEndTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posCounterItemRateTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posCounterItemRateTran](
	[CounterItemRateTranId] [smallint] IDENTITY(1,1) NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[DineInRateIndex] [smallint] NULL,
	[TakeAwayRateIndex] [smallint] NULL,
	[HomeDeliveryRateIndex] [smallint] NULL,
 CONSTRAINT [PK_posCounterItemRateTran] PRIMARY KEY CLUSTERED 
(
	[CounterItemRateTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posCounterItemTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posCounterItemTran](
	[CounterItemTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
 CONSTRAINT [PK_posCounterItemTran] PRIMARY KEY CLUSTERED 
(
	[CounterItemTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posCounterMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCounterMaster](
	[CounterMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NULL,
	[CounterName] [varchar](100) NOT NULL,
	[Description] [varchar](500) NULL,
	[ImageName] [varchar](100) NULL,
	[CounterColor] [varchar](6) NULL,
	[linktoDepartmentMasterId] [smallint] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posCounterMaster] PRIMARY KEY CLUSTERED 
(
	[CounterMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCounterPrinterTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCounterPrinterTran](
	[CounterPrinterTranId] [smallint] IDENTITY(1,1) NOT NULL,
	[PrinterName] [varchar](50) NOT NULL,
	[Copy] [smallint] NOT NULL,
	[Size] [smallint] NOT NULL,
	[IsReceiptPrinter] [bit] NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[linktoCategoryMasterId] [smallint] NULL,
 CONSTRAINT [PK_posCounterPrinterTran] PRIMARY KEY CLUSTERED 
(
	[CounterPrinterTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCounterSettingMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCounterSettingMaster](
	[CounterSettingMasterId] [smallint] NOT NULL,
	[CounterSetting] [varchar](50) NOT NULL,
	[DefaultValue] [varchar](500) NULL,
	[linktoBusinessTypeMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posCounterSettingMaster] PRIMARY KEY CLUSTERED 
(
	[CounterSettingMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCounterSettingValueTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCounterSettingValueTran](
	[CounterSettingValueTranId] [smallint] IDENTITY(1,1) NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[linktoCounterSettingMasterId] [smallint] NOT NULL,
	[Value] [varchar](500) NOT NULL,
 CONSTRAINT [PK_posCounterSettingValueTran] PRIMARY KEY CLUSTERED 
(
	[CounterSettingValueTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCounterTableTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posCounterTableTran](
	[CounterTableTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[linktoTableMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posCounterTableTran] PRIMARY KEY CLUSTERED 
(
	[CounterTableTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posCountryMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCountryMaster](
	[CountryMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[CountryName] [varchar](50) NOT NULL,
	[CountryCode] [varchar](3) NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posCountryMaster] PRIMARY KEY CLUSTERED 
(
	[CountryMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCustomerAddressTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCustomerAddressTran](
	[CustomerAddressTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoCustomerMasterId] [int] NOT NULL,
	[Address] [varchar](500) NULL,
	[linktoCountryMasterId] [smallint] NULL,
	[linktoStateMasterId] [smallint] NULL,
	[linktoCityMasterId] [int] NULL,
	[linktoAreaMasterId] [int] NULL,
	[ZipCode] [varchar](10) NULL,
	[IsPrimary] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posCustomerAddressTran] PRIMARY KEY CLUSTERED 
(
	[CustomerAddressTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCustomerInvoiceMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCustomerInvoiceMaster](
	[CustomerInvoiceMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoCustomerMasterId] [int] NOT NULL,
	[InvoiceNo] [varchar](20) NOT NULL,
	[InvoiceDate] [datetime] NOT NULL,
	[DueDate] [datetime] NOT NULL,
	[Remark] [varchar](250) NULL,
	[TotalAmount] [money] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posCustomerInvoiceMaster] PRIMARY KEY CLUSTERED 
(
	[CustomerInvoiceMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCustomerInvoiceTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posCustomerInvoiceTran](
	[CustomerInvoiceTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoCustomerInvoiceMasterId] [int] NOT NULL,
	[linktoSalesMasterId] [bigint] NOT NULL,
	[TotalAmount] [money] NOT NULL,
 CONSTRAINT [PK_posCustomerInvoiceTran] PRIMARY KEY CLUSTERED 
(
	[CustomerInvoiceTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posCustomerMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCustomerMaster](
	[CustomerMasterId] [int] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NOT NULL,
	[CustomerName] [varchar](100) NOT NULL,
	[Description] [varchar](500) NULL,
	[ContactPersonName] [varchar](100) NULL,
	[Designation] [varchar](50) NULL,
	[Phone1] [varchar](15) NOT NULL,
	[IsPhone1DND] [bit] NOT NULL,
	[Phone2] [varchar](15) NULL,
	[IsPhone2DND] [bit] NULL,
	[Email1] [varchar](80) NULL,
	[Email2] [varchar](80) NULL,
	[Fax] [varchar](15) NULL,
	[ImageName] [varchar](100) NULL,
	[Age] [smallint] NULL,
	[BirthDate] [date] NULL,
	[AnniversaryDate] [date] NULL,
	[CustomerType] [smallint] NOT NULL,
	[IsFavourite] [bit] NOT NULL,
	[IsCredit] [bit] NOT NULL,
	[OpeningBalance] [money] NOT NULL,
	[CreditDays] [smallint] NOT NULL,
	[CreditBalance] [money] NOT NULL,
	[CreditLimit] [money] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[linktoDiscountMasterId] [int] NULL,
	[linktoMembershipTypeMasterId] [smallint] NULL,
	[MembershipCardNumber] [varchar](50) NULL,
	[MembershipCardExpiryDate] [datetime] NULL,
	[IsMembershipCardUsedFirstTime] [bit] NULL,
	[TotalPoints] [int] NULL,
	[TotalAmount] [money] NULL,
	[Gender] [varchar](6) NULL,
	[Password] [varchar](25) NULL,
	[linktoSourceMasterId] [smallint] NOT NULL,
	[LastLoginDateTime] [datetime] NULL,
	[Remark] [varchar](100) NULL,
	[GooglePlusUserId] [varchar](10) NULL,
	[FacebookUserId] [varchar](10) NULL,
	[AgeMinRange] [int] NULL,
	[AgeMaxRange] [int] NULL,
	[IsVerified] [bit] NULL,
	[GCMToken] [varchar](250) NULL,
 CONSTRAINT [PK_posCustomerMaster] PRIMARY KEY CLUSTERED 
(
	[CustomerMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posCustomerPaymentTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posCustomerPaymentTran](
	[CustomerPaymentTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoCustomerMasterId] [int] NOT NULL,
	[linktoCustomerInvoiceMasterId] [int] NULL,
	[linktoPaymentTypeMasterId] [smallint] NOT NULL,
	[PaymentDate] [datetime] NOT NULL,
	[ReceiptNo] [varchar](20) NOT NULL,
	[TotalAmount] [money] NOT NULL,
	[Remark] [varchar](250) NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posCustomerPaymentTran] PRIMARY KEY CLUSTERED 
(
	[CustomerPaymentTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posDenominationMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posDenominationMaster](
	[DenominationMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[Denomination] [money] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posDenominationMaster] PRIMARY KEY CLUSTERED 
(
	[DenominationMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posDepartmentMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posDepartmentMaster](
	[DepartmentMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NULL,
	[DepartmentName] [varchar](50) NOT NULL,
	[Description] [varchar](500) NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsSync] [bit] NOT NULL,
	[SyncId] [smallint] NULL,
	[TransactionDateTime] [datetime] NULL,
 CONSTRAINT [PK_posDepartmentMaster] PRIMARY KEY CLUSTERED 
(
	[DepartmentMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posDesignationMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posDesignationMaster](
	[DesignationMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NOT NULL,
	[DesignationName] [varchar](50) NOT NULL,
	[Description] [varchar](500) NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posDesignationMaster] PRIMARY KEY CLUSTERED 
(
	[DesignationMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posDiscountMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posDiscountMaster](
	[DiscountMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[DiscountTitle] [varchar](50) NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [int] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [int] NULL,
 CONSTRAINT [PK_posDiscountMaster] PRIMARY KEY CLUSTERED 
(
	[DiscountMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posDiscountSelectionMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posDiscountSelectionMaster](
	[DiscountSelectionMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[Discount] [numeric](6, 2) NOT NULL,
	[IsPercentage] [bit] NOT NULL,
	[DiscountTitle] [varchar](50) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posDiscountSelectionMaster] PRIMARY KEY CLUSTERED 
(
	[DiscountSelectionMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posDiscountTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posDiscountTran](
	[DiscountTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoDiscountMasterId] [int] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[Discount] [money] NOT NULL,
	[IsPercentage] [bit] NOT NULL,
 CONSTRAINT [PK_posDiscountTran] PRIMARY KEY CLUSTERED 
(
	[DiscountTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posEmployeeMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posEmployeeMaster](
	[EmployeeMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[EmployeeCode] [varchar](9) NOT NULL,
	[EmployeeName] [varchar](100) NOT NULL,
	[ImageName] [varchar](250) NULL,
	[BirthDate] [date] NOT NULL,
	[Gender] [varchar](6) NOT NULL,
	[Address] [varchar](250) NOT NULL,
	[linktoCountryMasterId] [smallint] NOT NULL,
	[linktoStateMasterId] [smallint] NOT NULL,
	[linktoCityMasterId] [int] NOT NULL,
	[ZipCode] [varchar](10) NOT NULL,
	[Phone1] [varchar](15) NOT NULL,
	[Phone2] [varchar](15) NULL,
	[Email] [varchar](80) NULL,
	[Salary] [money] NULL,
	[JoinDate] [date] NULL,
	[LeaveDate] [date] NULL,
	[BloodGroup] [varchar](3) NULL,
	[linktoDesignationMasterId] [smallint] NOT NULL,
	[linktoDepartmentMasterId] [smallint] NOT NULL,
	[ReferenceBy] [varchar](50) NULL,
	[Remark] [varchar](250) NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_EmployeeMasterId] PRIMARY KEY CLUSTERED 
(
	[EmployeeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posErrorLog]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posErrorLog](
	[ErrorLogId] [smallint] IDENTITY(1,1) NOT NULL,
	[ErrorDateTime] [datetime] NOT NULL,
	[ErrorMessage] [varchar](500) NOT NULL,
	[ErrorStackTrace] [varchar](4000) NOT NULL,
	[LastErrorDateTime] [datetime] NULL,
	[ErrorCount] [smallint] NOT NULL,
 CONSTRAINT [PK_posErrorLog] PRIMARY KEY CLUSTERED 
(
	[ErrorLogId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posFeedbackAnswerMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posFeedbackAnswerMaster](
	[FeedbackAnswerMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoFeedbackQuestionMasterId] [int] NOT NULL,
	[Answer] [varchar](50) NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posFeedbackAnswerMaster] PRIMARY KEY CLUSTERED 
(
	[FeedbackAnswerMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posFeedbackMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posFeedbackMaster](
	[FeedbackMasterId] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NULL,
	[Email] [varchar](80) NOT NULL,
	[Phone] [varchar](15) NULL,
	[Feedback] [varchar](2000) NOT NULL,
	[FeedbackDateTime] [datetime] NOT NULL,
	[linktoFeedbackTypeMasterId] [smallint] NOT NULL,
	[linktoCustomerMasterId] [int] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posFeedbackMaster] PRIMARY KEY CLUSTERED 
(
	[FeedbackMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posFeedbackQuestionGroupMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posFeedbackQuestionGroupMaster](
	[FeedbackQuestionGroupMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[GroupName] [varchar](50) NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posFeedbackQuestionGroupMaster] PRIMARY KEY CLUSTERED 
(
	[FeedbackQuestionGroupMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posFeedbackQuestionMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posFeedbackQuestionMaster](
	[FeedbackQuestionMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[linktoFeedbackQuestionGroupMasterId] [smallint] NULL,
	[FeedbackQuestion] [varchar](250) NOT NULL,
	[QuestionType] [smallint] NOT NULL,
	[SortOrder] [int] NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posFeedbackQuestionMaster] PRIMARY KEY CLUSTERED 
(
	[FeedbackQuestionMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posFeedbackTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posFeedbackTran](
	[FeedbackTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoFeedbackMasterId] [int] NOT NULL,
	[linktoFeedbackQuestionMasterId] [int] NOT NULL,
	[linktoFeedbackAnswerMasterId] [int] NULL,
	[Answer] [varchar](200) NULL,
 CONSTRAINT [PK_posFeedbackTran] PRIMARY KEY CLUSTERED 
(
	[FeedbackTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posFeedbackTypeMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posFeedbackTypeMaster](
	[FeedbackTypeMasterId] [smallint] NOT NULL,
	[FeedbackType] [varchar](50) NOT NULL,
 CONSTRAINT [PK_posFeedbackTypeMaster] PRIMARY KEY CLUSTERED 
(
	[FeedbackTypeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posFinancialYearMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posFinancialYearMaster](
	[FinancialYearMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[FromDate] [date] NOT NULL,
	[ToDate] [date] NOT NULL,
	[IsDefault] [bit] NOT NULL,
 CONSTRAINT [PK_posFinancialYearMaster] PRIMARY KEY CLUSTERED 
(
	[FinancialYearMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posIssueItemMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posIssueItemMaster](
	[IssueItemMasterId] [int] IDENTITY(1,1) NOT NULL,
	[IssueNumber] [varchar](20) NOT NULL,
	[IssueDate] [date] NOT NULL,
	[linktoDepartmentMasterIdFrom] [smallint] NOT NULL,
	[linktoDepartmentMasterIdTo] [smallint] NOT NULL,
	[Remark] [varchar](250) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posIssueItemMaster] PRIMARY KEY CLUSTERED 
(
	[IssueItemMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posIssueItemTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posIssueItemTran](
	[IssueItemTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoIssueItemMasterId] [int] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoUnitMasterId] [smallint] NOT NULL,
	[Quantity] [numeric](10, 2) NOT NULL,
 CONSTRAINT [PK_posIssueItemTran] PRIMARY KEY CLUSTERED 
(
	[IssueItemTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posItemCategoryTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posItemCategoryTran](
	[ItemCategoryTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoCategoryMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posItemCategoryTran] PRIMARY KEY CLUSTERED 
(
	[ItemCategoryTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posItemComboTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posItemComboTran](
	[ItemComboTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoItemMasterIdCombo] [int] NOT NULL,
 CONSTRAINT [PK_posItemComboTran] PRIMARY KEY CLUSTERED 
(
	[ItemComboTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posItemMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posItemMaster](
	[ItemMasterId] [int] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](20) NULL,
	[ItemName] [varchar](50) NOT NULL,
	[ItemCode] [varchar](20) NOT NULL,
	[BarCode] [varchar](50) NULL,
	[ShortDescription] [varchar](500) NULL,
	[linktoUnitMasterId] [smallint] NULL,
	[linktoCategoryMasterId] [smallint] NOT NULL,
	[IsFavourite] [bit] NOT NULL,
	[ImageName] [varchar](100) NULL,
	[ItemPoint] [smallint] NOT NULL,
	[PriceByPoint] [smallint] NOT NULL,
	[SearchWords] [varchar](200) NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[SortOrder] [int] NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[ItemType] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[IsDineInOnly] [bit] NOT NULL,
 CONSTRAINT [PK_posItemMaster] PRIMARY KEY CLUSTERED 
(
	[ItemMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posItemModifierTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posItemModifierTran](
	[ItemModifierTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoItemMasterModifierId] [int] NOT NULL,
 CONSTRAINT [PK_posItemModifierTran] PRIMARY KEY CLUSTERED 
(
	[ItemModifierTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posItemOptionTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posItemOptionTran](
	[ItemOptionTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoOptionValueTranId] [int] NOT NULL,
 CONSTRAINT [PK_posItemOptionTran] PRIMARY KEY CLUSTERED 
(
	[ItemOptionTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posItemRateTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posItemRateTran](
	[ItemRateTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[PurchaseRate] [money] NOT NULL,
	[MRP] [money] NOT NULL,
	[SaleRate] [money] NOT NULL,
	[Rate1] [money] NOT NULL,
	[Rate2] [money] NOT NULL,
	[Rate3] [money] NOT NULL,
	[Rate4] [money] NOT NULL,
	[Rate5] [money] NOT NULL,
	[IsRateTaxInclusive] [bit] NOT NULL,
	[Tax1] [numeric](5, 2) NOT NULL,
	[Tax2] [numeric](5, 2) NOT NULL,
	[Tax3] [numeric](5, 2) NOT NULL,
	[Tax4] [numeric](5, 2) NOT NULL,
	[Tax5] [numeric](5, 2) NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posItemRateTran] PRIMARY KEY CLUSTERED 
(
	[ItemRateTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posItemRemarkMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posItemRemarkMaster](
	[ItemRemarkMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[ItemRemark] [varchar](100) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posItemRemarkMaster] PRIMARY KEY CLUSTERED 
(
	[ItemRemarkMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posItemStockTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posItemStockTran](
	[ItemStockTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoUnitMasterId] [smallint] NOT NULL,
	[linktoDepartmentMasterId] [smallint] NOT NULL,
	[IsMaintainStock] [bit] NOT NULL,
	[OpeningStock] [numeric](9, 2) NOT NULL,
	[InHand] [numeric](9, 2) NOT NULL,
	[MinimumStock] [numeric](9, 2) NOT NULL,
	[MaximumStock] [numeric](9, 2) NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdatedDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posItemStockTran] PRIMARY KEY CLUSTERED 
(
	[ItemStockTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posItemSuggestedTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posItemSuggestedTran](
	[ItemSuggestedTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoItemMasterIdSuggested] [int] NOT NULL,
 CONSTRAINT [PK_posItemSuggestedTran] PRIMARY KEY CLUSTERED 
(
	[ItemSuggestedTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posItemUsageTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posItemUsageTran](
	[ItemUsageTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoItemMasterIdUse] [int] NOT NULL,
	[linktoUnitMasterIdUse] [smallint] NOT NULL,
	[Quantity] [numeric](9, 2) NOT NULL,
 CONSTRAINT [PK_posItemUsageTran] PRIMARY KEY CLUSTERED 
(
	[ItemUsageTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posJournalVoucherMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posJournalVoucherMaster](
	[JournalVoucherMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[VoucherDate] [date] NOT NULL,
	[VoucherNumber] [varchar](20) NOT NULL,
	[Remark] [varchar](500) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posJournalVoucherMaster] PRIMARY KEY CLUSTERED 
(
	[JournalVoucherMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posJournalVoucherTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posJournalVoucherTran](
	[JournalVoucherTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoJournalVoucherMasterId] [int] NOT NULL,
	[linktoAccountMasterId] [int] NOT NULL,
	[Debit] [money] NULL,
	[Credit] [money] NULL,
 CONSTRAINT [PK_posJournalVoucherTran] PRIMARY KEY CLUSTERED 
(
	[JournalVoucherTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posMembershipType]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posMembershipType](
	[MembershipTypeMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[MembershipType] [varchar](50) NOT NULL,
	[Prefix] [varchar](10) NULL,
	[DisplayFormat] [varchar](50) NOT NULL,
	[TotalDigits] [smallint] NOT NULL,
	[PricePerPoint] [money] NOT NULL,
	[IsCardOfPoint] [bit] NOT NULL,
	[NewCardRate] [money] NULL,
	[NewCardBonusPoints] [smallint] NULL,
	[NewCardBonusAmount] [money] NULL,
	[CardRenewalRate] [money] NULL,
	[CardRenewalBonusPoints] [smallint] NULL,
	[CardRenewalBonusAmount] [money] NULL,
	[ValidMonths] [smallint] NULL,
	[MinimumPointsForDeduction] [smallint] NOT NULL,
	[TermsAndConditions] [varchar](500) NULL,
	[IsEnabled] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [int] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [int] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posMembershipType] PRIMARY KEY CLUSTERED 
(
	[MembershipTypeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posMenuMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posMenuMaster](
	[MenuMasterId] [smallint] NOT NULL,
	[MenuName] [varchar](50) NOT NULL,
	[ShortcutKey] [varchar](15) NULL,
	[DefaultShortcutKey] [varchar](15) NULL,
	[linktoMenuMasterId] [smallint] NULL,
	[SortOrder] [smallint] NULL,
	[IsShowInQuickLaunch] [char](1) NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posMenuMaster] PRIMARY KEY CLUSTERED 
(
	[MenuMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posMiscExpenseCategoryMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posMiscExpenseCategoryMaster](
	[MiscExpenseCategoryMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[CategoryName] [varchar](50) NULL,
	[Description] [varchar](500) NULL,
	[IsEnabled] [bit] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posMiscExpenseCategoryMaster] PRIMARY KEY CLUSTERED 
(
	[MiscExpenseCategoryMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posMiscExpenseMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posMiscExpenseMaster](
	[MiscExpenseMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoMiscExpenseCategoryMasterId] [smallint] NOT NULL,
	[PaidTo] [varchar](100) NOT NULL,
	[PaidDate] [date] NOT NULL,
	[InvoiceNo] [varchar](20) NULL,
	[linktoPaymentTypeMasterId] [smallint] NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[Amount] [money] NOT NULL,
	[Remark] [varchar](200) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posMiscExpenseMaster] PRIMARY KEY CLUSTERED 
(
	[MiscExpenseMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posMiscIncomeCategoryMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posMiscIncomeCategoryMaster](
	[MiscIncomeCategoryMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[CategoryName] [varchar](50) NULL,
	[Description] [varchar](500) NULL,
	[IsEnabled] [bit] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posMiscIncomeCategoryMaster] PRIMARY KEY CLUSTERED 
(
	[MiscIncomeCategoryMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posMiscIncomeMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posMiscIncomeMaster](
	[MiscIncomeMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoMiscIncomeCategoryMasterId] [smallint] NOT NULL,
	[PaidBy] [varchar](100) NOT NULL,
	[ReceiveDate] [date] NOT NULL,
	[ReceiptNo] [varchar](20) NULL,
	[linktoPaymentTypeMasterId] [smallint] NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[Amount] [money] NOT NULL,
	[Remark] [varchar](200) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posMiscIncomeMaster] PRIMARY KEY CLUSTERED 
(
	[MiscIncomeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posModifierMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posModifierMaster](
	[ModifierMasterId] [int] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NOT NULL,
	[ModifierName] [varchar](50) NOT NULL,
	[Description] [varchar](250) NULL,
	[ModifierRate] [money] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[UpdateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NOT NULL,
 CONSTRAINT [PK_posModifierMaster] PRIMARY KEY CLUSTERED 
(
	[ModifierMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOfferCodesTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOfferCodesTran](
	[OfferCodesTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoOfferMasterId] [int] NOT NULL,
	[OfferCode] [varchar](10) NOT NULL,
	[linktoCustomerMasterId] [int] NULL,
	[linktoItemMasterId] [int] NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[RedeemDateTime] [datetime] NULL,
	[linktoUserMasterIdRedeemedBy] [int] NULL,
	[linktoSourceMasterId] [smallint] NULL,
 CONSTRAINT [PK_posOfferCodesTran] PRIMARY KEY CLUSTERED 
(
	[OfferCodesTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOfferDaysTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posOfferDaysTran](
	[OfferDaysTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoOfferMasterId] [int] NOT NULL,
	[Day] [smallint] NOT NULL,
	[FromTime] [time](7) NOT NULL,
	[ToTime] [time](7) NOT NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posOfferDaysTran] PRIMARY KEY CLUSTERED 
(
	[OfferDaysTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posOfferItemsTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posOfferItemsTran](
	[OfferItemsTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoOfferMasterId] [int] NOT NULL,
	[linktoItemMasterId] [int] NULL,
	[OfferItemType] [smallint] NULL,
 CONSTRAINT [PK_posOfferItemsTran] PRIMARY KEY CLUSTERED 
(
	[OfferItemsTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posOfferMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOfferMaster](
	[OfferMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoOfferTypeMasterId] [smallint] NOT NULL,
	[OfferTitle] [varchar](50) NOT NULL,
	[OfferContent] [varchar](2000) NULL,
	[FromDate] [date] NULL,
	[ToDate] [date] NULL,
	[FromTime] [time](7) NULL,
	[ToTime] [time](7) NULL,
	[MinimumBillAmount] [money] NULL,
	[Discount] [money] NOT NULL,
	[IsDiscountPercentage] [bit] NOT NULL,
	[RedeemCount] [int] NULL,
	[OfferCode] [varchar](50) NULL,
	[ImagePhysicalName] [varchar](100) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[TermsAndConditions] [varchar](4000) NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[IsForCustomers] [bit] NOT NULL,
	[BuyItemCount] [int] NULL,
	[GetItemCount] [int] NULL,
	[linktoCounterMasterId] [varchar](50) NULL,
	[linktoOrderTypeMasterIds] [varchar](50) NULL,
	[IsOnline] [bit] NULL,
	[IsForApp] [bit] NULL,
	[IsForAllDays] [bit] NULL,
	[IsNotApplicableWithOtherOffers] [bit] NOT NULL,
 CONSTRAINT [PK_posOfferMaster] PRIMARY KEY CLUSTERED 
(
	[OfferMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOfferTypeMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOfferTypeMaster](
	[OfferTypeMasterId] [smallint] NOT NULL,
	[OfferType] [varchar](50) NOT NULL,
	[Description] [varchar](100) NULL,
 CONSTRAINT [PK_posOfferTypeMaster] PRIMARY KEY CLUSTERED 
(
	[OfferTypeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOptionMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOptionMaster](
	[OptionMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[OptionName] [varchar](20) NOT NULL,
	[linktoBusinessTypeMasterId] [smallint] NOT NULL,
	[SortOrder] [smallint] NULL,
	[IsDeleted] [bit] NOT NULL,
	[IsDefault] [bit] NOT NULL,
 CONSTRAINT [PK_posOptionMaster] PRIMARY KEY CLUSTERED 
(
	[OptionMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOptionValueTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOptionValueTran](
	[OptionValueTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoOptionMasterId] [smallint] NOT NULL,
	[OptionValue] [varchar](50) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posOptionValueTran] PRIMARY KEY CLUSTERED 
(
	[OptionValueTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOrderItemModifierTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posOrderItemModifierTran](
	[OrderItemModifierTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoOrderItemTranId] [bigint] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[Rate] [money] NOT NULL,
 CONSTRAINT [PK_posOrderItemModifierTran] PRIMARY KEY CLUSTERED 
(
	[OrderItemModifierTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posOrderItemOptionTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOrderItemOptionTran](
	[OrderItemOptionTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoOrderItemTranId] [bigint] NOT NULL,
	[linktoOptionValueTranId] [int] NOT NULL,
	[OptionValue] [varchar](50) NOT NULL,
	[Rate] [money] NOT NULL,
 CONSTRAINT [PK_posOrderItemOptionTran] PRIMARY KEY CLUSTERED 
(
	[OrderItemOptionTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOrderItemTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOrderItemTran](
	[OrderItemTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoOrderMasterId] [bigint] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[Quantity] [smallint] NOT NULL,
	[Rate] [money] NOT NULL,
	[DiscountPercentage] [numeric](5, 2) NOT NULL,
	[DiscountAmount] [money] NOT NULL,
	[IsRateTaxInclusive] [bit] NOT NULL,
	[ItemPoint] [smallint] NOT NULL,
	[DeductedPoint] [smallint] NOT NULL,
	[ItemRemark] [varchar](250) NULL,
	[linktoOrderStatusMasterId] [smallint] NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posOrderItemTran] PRIMARY KEY CLUSTERED 
(
	[OrderItemTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOrderMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOrderMaster](
	[OrderMasterId] [bigint] IDENTITY(1,1) NOT NULL,
	[OrderNumber] [varchar](20) NOT NULL,
	[OrderDateTime] [datetime] NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[linktoTableMasterIds] [varchar](50) NOT NULL,
	[linktoWaiterMasterId] [int] NULL,
	[linktoWaiterMasterIdCaptain] [int] NULL,
	[linktoCustomerMasterId] [int] NULL,
	[linktoOrderTypeMasterId] [smallint] NOT NULL,
	[linktoOrderStatusMasterId] [smallint] NULL,
	[NoOfAdults] [smallint] NULL,
	[NoOfChildren] [smallint] NULL,
	[TotalAmount] [money] NOT NULL,
	[TotalTax] [money] NOT NULL,
	[DiscountPercentage] [money] NOT NULL,
	[Discount] [money] NOT NULL,
	[ExtraAmount] [money] NOT NULL,
	[NetAmount] [money] NOT NULL,
	[Remark] [varchar](250) NULL,
	[RateIndex] [smallint] NULL,
	[TotalItemPoint] [smallint] NOT NULL,
	[TotalDeductedPoint] [smallint] NOT NULL,
	[linktoSalesMasterId] [bigint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[PrintCount] [smallint] NULL,
	[linktoSourceMasterId] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posOrderMaster] PRIMARY KEY CLUSTERED 
(
	[OrderMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOrderStatusMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOrderStatusMaster](
	[OrderStatusMasterId] [smallint] NOT NULL,
	[StatusName] [varchar](20) NOT NULL,
	[StatusColor] [varchar](6) NOT NULL,
	[linktoBusinessTypeMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posOrderStatusMaster] PRIMARY KEY CLUSTERED 
(
	[OrderStatusMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posOrderTrackTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posOrderTrackTran](
	[OrderTrackTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoOrderMasterId] [bigint] NOT NULL,
	[linktoOrderStatusMasterId] [smallint] NULL,
	[UpdateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdUpdatedBy] [int] NOT NULL,
 CONSTRAINT [PK_posOrderTrackTran] PRIMARY KEY CLUSTERED 
(
	[OrderTrackTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posOrderTypeMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posOrderTypeMaster](
	[OrderTypeMasterId] [smallint] NOT NULL,
	[OrderType] [varchar](20) NOT NULL,
	[linktoBusinessTypeMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posOrderTypeMaster] PRIMARY KEY CLUSTERED 
(
	[OrderTypeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posPaymentTypeCateogryMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posPaymentTypeCateogryMaster](
	[PaymentTypeCategoryMasterId] [smallint] NOT NULL,
	[PaymentTypeCategory] [varchar](50) NOT NULL,
 CONSTRAINT [PK_posPaymentTypeCateogryMaster] PRIMARY KEY CLUSTERED 
(
	[PaymentTypeCategoryMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posPaymentTypeMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posPaymentTypeMaster](
	[PaymentTypeMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NULL,
	[PaymentType] [varchar](50) NOT NULL,
	[Description] [varchar](500) NULL,
	[linktoPaymentTypeCategoryMasterId] [smallint] NOT NULL,
	[IsDefault] [bit] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[UpdateDateTime] [datetime] NULL,
 CONSTRAINT [PK_posPaymentTypeMaster] PRIMARY KEY CLUSTERED 
(
	[PaymentTypeMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posPurchaseItemTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posPurchaseItemTran](
	[PurchaseItemTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoPurchaseMasterId] [int] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[Quantity] [numeric](9, 2) NOT NULL,
	[linktoUnitMasterId] [smallint] NOT NULL,
	[Rate] [money] NOT NULL,
	[Tax] [money] NOT NULL,
	[Discount] [money] NOT NULL,
 CONSTRAINT [PK_posPurchaseItemTran] PRIMARY KEY CLUSTERED 
(
	[PurchaseItemTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posPurchaseMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posPurchaseMaster](
	[PurchaseMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[linktoDepartmentMasterId] [smallint] NOT NULL,
	[VoucherNumber] [varchar](20) NOT NULL,
	[PurchaseDate] [date] NOT NULL,
	[linktoSupplierMasterId] [smallint] NOT NULL,
	[InvoiceNumber] [varchar](20) NULL,
	[InvoiceDate] [date] NULL,
	[TotalAmount] [money] NOT NULL,
	[TotalTax] [money] NOT NULL,
	[TotalDiscount] [money] NOT NULL,
	[NetAmount] [money] NOT NULL,
	[Remark] [varchar](250) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posPurchaseMaster] PRIMARY KEY CLUSTERED 
(
	[PurchaseMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posRateCaptionMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posRateCaptionMaster](
	[RateCaptionMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[RateName] [varchar](20) NOT NULL,
	[RateCaption] [varchar](20) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[RateIndex] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posRateCaptionMaster] PRIMARY KEY CLUSTERED 
(
	[RateCaptionMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posRegisteredUserMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posRegisteredUserMaster](
	[RegisteredUserMasterId] [int] IDENTITY(1,1) NOT NULL,
	[Email] [varchar](80) NOT NULL,
	[Phone] [varchar](15) NULL,
	[Password] [varchar](25) NOT NULL,
	[FirstName] [varchar](25) NULL,
	[LastName] [varchar](25) NULL,
	[Gender] [varchar](6) NULL,
	[BirthDate] [date] NULL,
	[AnniversaryDate] [date] NULL,
	[linktoCountryMasterId] [smallint] NULL,
	[linktoStateMasterId] [smallint] NULL,
	[linktoCityMasterId] [smallint] NULL,
	[linktoAreaMasterId] [smallint] NULL,
	[linktoBusinessMasterId] [smallint] NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[LastLoginDateTime] [datetime] NULL,
	[linktoSourceMasterId] [smallint] NOT NULL,
	[Comment] [varchar](100) NULL,
	[IsEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_posRegisteredUserMaster] PRIMARY KEY CLUSTERED 
(
	[RegisteredUserMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posRoleMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posRoleMaster](
	[RoleMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[Role] [varchar](50) NOT NULL,
	[Description] [varchar](100) NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posRoleMaster] PRIMARY KEY CLUSTERED 
(
	[RoleMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSalesItemModifierTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSalesItemModifierTran](
	[SalesItemModifierTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoSalesItemTranId] [bigint] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[ItemName] [varchar](50) NOT NULL,
	[Rate] [money] NOT NULL,
 CONSTRAINT [PK_posSalesItemModifierTran] PRIMARY KEY CLUSTERED 
(
	[SalesItemModifierTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSalesItemTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSalesItemTran](
	[SalesItemTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoSalesMasterId] [bigint] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoUnitMasterId] [smallint] NOT NULL,
	[ItemCode] [varchar](20) NOT NULL,
	[ItemName] [varchar](50) NOT NULL,
	[Quantity] [int] NOT NULL,
	[Rate] [money] NOT NULL,
	[DiscountPercentage] [numeric](5, 2) NOT NULL,
	[DiscountAmount] [money] NOT NULL,
	[Tax1] [money] NOT NULL,
	[Tax2] [money] NOT NULL,
	[Tax3] [money] NOT NULL,
	[Tax4] [money] NOT NULL,
	[Tax5] [money] NOT NULL,
	[IsRateTaxInclusive] [bit] NOT NULL,
	[PurchaseRate] [money] NOT NULL,
	[AddLessAmount] [money] NULL,
	[ItemPoint] [smallint] NOT NULL,
	[DeductedPoint] [smallint] NOT NULL,
	[ItemRemark] [varchar](250) NULL,
	[linktoOrderStatusMasterId] [smallint] NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posSalesItemTran] PRIMARY KEY CLUSTERED 
(
	[SalesItemTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSalesMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSalesMaster](
	[SalesMasterId] [bigint] IDENTITY(1,1) NOT NULL,
	[BillNumber] [varchar](50) NOT NULL,
	[BillDateTime] [datetime] NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
	[linktoTableMasterIds] [varchar](50) NOT NULL,
	[linktoWaiterMasterId] [int] NULL,
	[linktoWaiterMasterIdCaptain] [int] NULL,
	[linktoCustomerMasterId] [int] NULL,
	[linktoOrderTypeMasterId] [smallint] NULL,
	[linktoOrderStatusMasterId] [smallint] NULL,
	[NoOfAdults] [smallint] NULL,
	[NoOfChildren] [smallint] NULL,
	[TotalAmount] [money] NOT NULL,
	[TotalTax] [money] NOT NULL,
	[DiscountPercentage] [money] NOT NULL,
	[DiscountAmount] [money] NOT NULL,
	[ExtraAmount] [money] NOT NULL,
	[TotalItemDiscount] [money] NOT NULL,
	[TotalItemTax] [money] NOT NULL,
	[Rounding] [money] NOT NULL,
	[NetAmount] [money] NOT NULL,
	[PaidAmount] [money] NOT NULL,
	[BalanceAmount] [money] NOT NULL,
	[Remark] [varchar](250) NULL,
	[RateIndex] [smallint] NULL,
	[IsComplimentary] [bit] NOT NULL,
	[TotalItemPoint] [smallint] NOT NULL,
	[TotalDeductedPoint] [smallint] NOT NULL,
	[linktoOfferMasterId] [int] NULL,
	[OfferCode] [varchar](50) NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posSalesMaster] PRIMARY KEY CLUSTERED 
(
	[SalesMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSalesPaymentTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSalesPaymentTran](
	[SalesPaymentTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoSalesMasterId] [bigint] NOT NULL,
	[linktoPaymentTypeMasterId] [smallint] NOT NULL,
	[linktoCustomerMasterId] [int] NULL,
	[PaymentDateTime] [datetime] NOT NULL,
	[AmountPaid] [money] NOT NULL,
	[Remark] [varchar](250) NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posSalesPaymentTran] PRIMARY KEY CLUSTERED 
(
	[SalesPaymentTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSalesTaxTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSalesTaxTran](
	[SalesTaxTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoSalesMasterId] [bigint] NOT NULL,
	[linktoTaxMasterId] [smallint] NOT NULL,
	[TaxName] [varchar](50) NOT NULL,
	[TaxRate] [numeric](5, 2) NOT NULL,
 CONSTRAINT [PK_posSalesTaxTran] PRIMARY KEY CLUSTERED 
(
	[SalesTaxTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSettingMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSettingMaster](
	[SettingMasterId] [smallint] NOT NULL,
	[Setting] [varchar](50) NOT NULL,
	[Value] [varchar](120) NOT NULL,
	[DefaultValue] [varchar](120) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posShiftMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posShiftMaster](
	[ShiftMasterId] [int] IDENTITY(1,1) NOT NULL,
	[linktoUserMasterId] [smallint] NOT NULL,
	[SoftwareStartDateTime] [datetime] NOT NULL,
	[SystemStartDateTine] [datetime] NOT NULL,
	[SoftwareEndDateTime] [datetime] NULL,
	[SystemEndDateTime] [datetime] NULL,
	[OpeningBalance] [money] NOT NULL,
	[ClosingBalance] [money] NOT NULL,
	[IsClosed] [bit] NOT NULL,
	[SystemName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_posShiftMaster] PRIMARY KEY CLUSTERED 
(
	[ShiftMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posShiftTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posShiftTran](
	[ShiftTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoShiftMasterId] [int] NOT NULL,
	[DebitAmount] [money] NOT NULL,
	[CreditAmount] [money] NOT NULL,
	[Remark] [varchar](250) NULL,
	[CreateDateTime] [datetime] NOT NULL,
 CONSTRAINT [PK_posShiftTran] PRIMARY KEY CLUSTERED 
(
	[ShiftTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posShortcuts]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posShortcuts](
	[ShortcutId] [smallint] NOT NULL,
	[ShortcutName] [varchar](50) NOT NULL,
	[ShortcutKey] [varchar](15) NOT NULL,
 CONSTRAINT [PK_posShortcuts] PRIMARY KEY CLUSTERED 
(
	[ShortcutId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSourceMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSourceMaster](
	[SourceMasterId] [smallint] NOT NULL,
	[SourceName] [varchar](20) NOT NULL,
 CONSTRAINT [PK_posSourceMaster] PRIMARY KEY CLUSTERED 
(
	[SourceMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posStateMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posStateMaster](
	[StateMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[StateName] [varchar](50) NOT NULL,
	[StateCode] [varchar](3) NOT NULL,
	[linktoCountryMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posStateMaster] PRIMARY KEY CLUSTERED 
(
	[StateMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posStockAdjustmentItemTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posStockAdjustmentItemTran](
	[StockAdjustmentItemTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoStockAdjustmentMasterId] [int] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
	[linktoUnitMasterId] [smallint] NOT NULL,
	[Quantity] [numeric](9, 2) NOT NULL,
 CONSTRAINT [PK_posStockAdjustmentItemTran] PRIMARY KEY CLUSTERED 
(
	[StockAdjustmentItemTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posStockAdjustmentMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posStockAdjustmentMaster](
	[StockAdjustmentMasterId] [int] IDENTITY(1,1) NOT NULL,
	[VoucherNumber] [varchar](20) NOT NULL,
	[AdjustmentDate] [date] NOT NULL,
	[linktoDepartmentMasterId] [smallint] NOT NULL,
	[Remark] [varchar](250) NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posStockAdjustmentMaster] PRIMARY KEY CLUSTERED 
(
	[StockAdjustmentMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSupplierItemTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posSupplierItemTran](
	[SupplierItemTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoSupplierMasterId] [smallint] NOT NULL,
	[linktoItemMasterId] [int] NOT NULL,
 CONSTRAINT [PK_posSupplierItemTran] PRIMARY KEY CLUSTERED 
(
	[SupplierItemTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posSupplierMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSupplierMaster](
	[SupplierMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NULL,
	[SupplierName] [varchar](100) NOT NULL,
	[Description] [varchar](500) NULL,
	[ContactPersonName] [varchar](100) NULL,
	[Designation] [varchar](50) NULL,
	[Address] [varchar](500) NULL,
	[linktoCountryMasterId] [smallint] NULL,
	[linktoStateMasterId] [smallint] NULL,
	[linktoCityMasterId] [int] NULL,
	[ZipCode] [varchar](15) NULL,
	[Phone1] [varchar](15) NOT NULL,
	[Phone2] [varchar](15) NULL,
	[Email1] [varchar](80) NULL,
	[Email2] [varchar](80) NULL,
	[Fax] [varchar](15) NULL,
	[IsCredit] [bit] NOT NULL,
	[OpeningBalance] [money] NOT NULL,
	[CreditDays] [smallint] NOT NULL,
	[CreditBalance] [money] NOT NULL,
	[CreditLimit] [money] NOT NULL,
	[Tax] [money] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posSupplierMaster] PRIMARY KEY CLUSTERED 
(
	[SupplierMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posSupplierPaymentTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posSupplierPaymentTran](
	[SupplierPaymentTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoSupplierMasterId] [smallint] NOT NULL,
	[linktoPaymentTypeMasterId] [smallint] NOT NULL,
	[linktoPurchaseMasterId] [int] NULL,
	[PaymentDate] [date] NOT NULL,
	[ReceiptNumber] [varchar](20) NOT NULL,
	[Amount] [money] NOT NULL,
	[Remark] [varchar](250) NULL,
	[IsDeleted] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posSupplierPaymentTran] PRIMARY KEY CLUSTERED 
(
	[SupplierPaymentTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posTableAmentitiesTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posTableAmentitiesTran](
	[TableAmentitiesTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoTableMasterId] [smallint] NOT NULL,
	[linktoAmentitiesMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posTableAmentitiesTran] PRIMARY KEY CLUSTERED 
(
	[TableAmentitiesTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posTableMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posTableMaster](
	[TableMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[TableName] [varchar](50) NOT NULL,
	[ShortName] [varchar](5) NOT NULL,
	[Description] [varchar](500) NULL,
	[MinPerson] [smallint] NOT NULL,
	[MaxPerson] [smallint] NOT NULL,
	[linktoTableStatusMasterId] [smallint] NOT NULL,
	[StatusUpdateDateTime] [datetime] NULL,
	[linktoOrderTypeMasterId] [smallint] NOT NULL,
	[linktoWaiterMasterId] [int] NULL,
	[linktoCaptainMasterId] [int] NULL,
	[OriginX] [int] NOT NULL,
	[OriginY] [int] NOT NULL,
	[Height] [numeric](10, 2) NOT NULL,
	[Width] [numeric](10, 2) NOT NULL,
	[TableColor] [varchar](6) NULL,
	[TableType] [smallint] NOT NULL,
	[IsBookingAvailable] [bit] NOT NULL,
	[HourlyBookingRate] [money] NULL,
	[DailyBookingRate] [money] NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posTableMaster] PRIMARY KEY CLUSTERED 
(
	[TableMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posTableStatusMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posTableStatusMaster](
	[TableStatusMasterId] [smallint] NOT NULL,
	[TableStatus] [varchar](10) NOT NULL,
	[StatusColor] [varchar](6) NOT NULL,
 CONSTRAINT [PK_posTableStatusMaster] PRIMARY KEY CLUSTERED 
(
	[TableStatusMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posTaxMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posTaxMaster](
	[TaxMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[TaxIndex] [smallint] NOT NULL,
	[TaxName] [varchar](50) NOT NULL,
	[TaxRate] [numeric](5, 2) NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posTaxMaster] PRIMARY KEY CLUSTERED 
(
	[TaxMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posUnitConversionTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posUnitConversionTran](
	[UnitConversionTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoItemMasterId] [int] NULL,
	[linktoUnitMasterIdFrom] [smallint] NOT NULL,
	[linktoUnitMasterIdTo] [smallint] NOT NULL,
	[Quantity] [numeric](9, 2) NOT NULL,
 CONSTRAINT [PK_posUnitConversionTran] PRIMARY KEY CLUSTERED 
(
	[UnitConversionTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posUnitMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posUnitMaster](
	[UnitMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NOT NULL,
	[UnitName] [varchar](50) NOT NULL,
	[Description] [varchar](500) NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[UpdateDateTime] [datetime] NULL,
 CONSTRAINT [PK_posUnitMaster] PRIMARY KEY CLUSTERED 
(
	[UnitMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posUserCounterTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posUserCounterTran](
	[UserCounterTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoUserMasterId] [smallint] NOT NULL,
	[linktoCounterMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posUserCounterTran] PRIMARY KEY CLUSTERED 
(
	[UserCounterTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posUserMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posUserMaster](
	[UserMasterId] [smallint] IDENTITY(1,1) NOT NULL,
	[Username] [varchar](50) NOT NULL,
	[Password] [varchar](25) NOT NULL,
	[linktoRoleMasterId] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[LastLoginDateTime] [datetime] NULL,
	[LoginFailCount] [smallint] NOT NULL,
	[LastLockoutDateTime] [datetime] NULL,
	[LastPasswordChangedDateTime] [datetime] NULL,
	[Comment] [varchar](250) NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
 CONSTRAINT [PK_posUserMaster] PRIMARY KEY CLUSTERED 
(
	[UserMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posUserRightsGroupMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posUserRightsGroupMaster](
	[UserRightsGroupMasterId] [smallint] NOT NULL,
	[UserRightsGroup] [varchar](50) NOT NULL,
	[SortOrder] [smallint] NOT NULL,
 CONSTRAINT [PK_posUserRightsGroupMaster] PRIMARY KEY CLUSTERED 
(
	[UserRightsGroupMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posUserRightsMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posUserRightsMaster](
	[UserRightsMasterId] [smallint] NOT NULL,
	[UserRight] [varchar](50) NOT NULL,
	[linktoUserRightsGroupMasterId] [smallint] NOT NULL,
	[SortOrder] [smallint] NOT NULL,
 CONSTRAINT [PK_posUserRightsMaster] PRIMARY KEY CLUSTERED 
(
	[UserRightsMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posUserRightsTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posUserRightsTran](
	[UserRightsTranId] [int] IDENTITY(1,1) NOT NULL,
	[linktoUserRightsMasterId] [smallint] NOT NULL,
	[linktoRoleMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posUserRightsTran] PRIMARY KEY CLUSTERED 
(
	[UserRightsTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posWaiterMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posWaiterMaster](
	[WaiterMasterId] [int] IDENTITY(1,1) NOT NULL,
	[ShortName] [varchar](10) NOT NULL,
	[WaiterName] [varchar](50) NOT NULL,
	[Description] [varchar](250) NULL,
	[WaiterType] [smallint] NOT NULL,
	[linktoUserMasterId] [smallint] NULL,
	[IsEnabled] [bit] NOT NULL,
	[IsDeleted] [bit] NOT NULL,
	[IsOut] [bit] NULL,
	[InOutDateTime] [datetime] NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posWaiterMaster] PRIMARY KEY CLUSTERED 
(
	[WaiterMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posWaiterNotificationMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posWaiterNotificationMaster](
	[WaiterNotificationMasterId] [bigint] IDENTITY(1,1) NOT NULL,
	[NotificationDateTime] [datetime] NOT NULL,
	[linktoTableMasterId] [smallint] NOT NULL,
	[Message] [varchar](100) NULL,
 CONSTRAINT [PK_posWaiterNotificationMaster] PRIMARY KEY CLUSTERED 
(
	[WaiterNotificationMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posWaiterNotificationTran]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[posWaiterNotificationTran](
	[WaiterNotificationTranId] [bigint] IDENTITY(1,1) NOT NULL,
	[linktoWaiterNotificationMasterId] [bigint] NOT NULL,
	[linktoUserMasterId] [smallint] NOT NULL,
 CONSTRAINT [PK_posWaiterNotificationTran] PRIMARY KEY CLUSTERED 
(
	[WaiterNotificationTranId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[posWaitingMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posWaitingMaster](
	[WaitingMasterId] [bigint] IDENTITY(1,1) NOT NULL,
	[PersonName] [varchar](50) NOT NULL,
	[PersonMobile] [varchar](10) NOT NULL,
	[NoOfPersons] [smallint] NOT NULL,
	[linktoWaitingStatusMasterId] [smallint] NOT NULL,
	[linktoTableMasterId] [smallint] NULL,
	[linktoOrderStatusMasterId] [smallint] NULL,
	[TokenNumber] [bigint] NULL,
	[linktoBusinessMasterId] [smallint] NOT NULL,
	[CreateDateTime] [datetime] NOT NULL,
	[linktoUserMasterIdCreatedBy] [smallint] NOT NULL,
	[UpdateDateTime] [datetime] NULL,
	[linktoUserMasterIdUpdatedBy] [smallint] NULL,
 CONSTRAINT [PK_posWaitingMaster] PRIMARY KEY CLUSTERED 
(
	[WaitingMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[posWaitingStatusMaster]    Script Date: 30/12/2016 2:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[posWaitingStatusMaster](
	[WaitingStatusMasterId] [smallint] NOT NULL,
	[WaitingStatus] [varchar](10) NOT NULL,
	[StatusColor] [varchar](6) NOT NULL,
 CONSTRAINT [PK_posWaitingStatusMaster] PRIMARY KEY CLUSTERED 
(
	[WaitingStatusMasterId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
ALTER TABLE [dbo].[posAccountMaster] ADD  CONSTRAINT [DF_posAccountMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posAddLessMaster] ADD  CONSTRAINT [DF_posAddLessMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posAmentitiesMaster] ADD  CONSTRAINT [DF_posAmentitiesMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posAmentitiesMaster] ADD  CONSTRAINT [DF_posAmentitiesMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posApplicationMaster] ADD  CONSTRAINT [DF_posApplicationMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posAreaMaster] ADD  CONSTRAINT [DF_posAreaMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posBankMaster] ADD  CONSTRAINT [DF_posBankMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posBankMaster] ADD  CONSTRAINT [DF_posBankMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posBookingMaster] ADD  CONSTRAINT [DF_posBookingMaster_IsHourly]  DEFAULT ((1)) FOR [IsHourly]
GO
ALTER TABLE [dbo].[posBookingMaster] ADD  CONSTRAINT [DF_posBookingMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posBookingPaymentTran] ADD  CONSTRAINT [DF_posBookingPaymentTran_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posBusinessInfoAnswerMaster] ADD  CONSTRAINT [DF_posBusinessInfoAnswerMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posBusinessInfoQuestionMaster] ADD  CONSTRAINT [DF_posBusinessInfoQuestionMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posBusinessMaster] ADD  CONSTRAINT [DF_posBusinessMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posCategoryMaster] ADD  CONSTRAINT [DF_posCategoryMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posCategoryMaster] ADD  CONSTRAINT [DF_posCategoryMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posCategoryMaster] ADD  CONSTRAINT [DF_posCategoryMaster_IsHiddenForCustomer]  DEFAULT ((0)) FOR [IsHiddenForCustomer]
GO
ALTER TABLE [dbo].[posCityMaster] ADD  CONSTRAINT [DF_posCityMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posCounterMaster] ADD  CONSTRAINT [DF_posCounterMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posCounterMaster] ADD  CONSTRAINT [DF_posCounterMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posCounterPrinterTran] ADD  CONSTRAINT [DF_posCounterPrinterTran_Copy]  DEFAULT ((1)) FOR [Copy]
GO
ALTER TABLE [dbo].[posCustomerAddressTran] ADD  CONSTRAINT [DF_posCustomerAddressTran_IsPrimary]  DEFAULT ((0)) FOR [IsPrimary]
GO
ALTER TABLE [dbo].[posCustomerAddressTran] ADD  CONSTRAINT [DF_posCustomerAddressTran_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posCustomerMaster] ADD  CONSTRAINT [DF_posCustomerMaster_IsFavourite]  DEFAULT ((0)) FOR [IsFavourite]
GO
ALTER TABLE [dbo].[posCustomerMaster] ADD  CONSTRAINT [DF_posCustomerMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posCustomerMaster] ADD  CONSTRAINT [DF_posCustomerMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posCustomerMaster] ADD  CONSTRAINT [DF_posCustomerMaster_TotalPoints]  DEFAULT ((0)) FOR [TotalPoints]
GO
ALTER TABLE [dbo].[posDepartmentMaster] ADD  CONSTRAINT [DF_posDepartmentMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posDepartmentMaster] ADD  CONSTRAINT [DF_posDepartmentMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posDepartmentMaster] ADD  CONSTRAINT [DF_posDepartmentMaster_IsSync]  DEFAULT ((0)) FOR [IsSync]
GO
ALTER TABLE [dbo].[posDesignationMaster] ADD  CONSTRAINT [DF_posDesignationMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posDesignationMaster] ADD  CONSTRAINT [DF_posDesignationMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posEmployeeMaster] ADD  CONSTRAINT [DF_posEmployeeMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posEmployeeMaster] ADD  CONSTRAINT [DF_posEmployeeMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posErrorLog] ADD  CONSTRAINT [DF_posErrorLog_ErrorCount]  DEFAULT ((0)) FOR [ErrorCount]
GO
ALTER TABLE [dbo].[posFeedbackAnswerMaster] ADD  CONSTRAINT [DF_posFeedbackAnswerMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posFeedbackQuestionGroupMaster] ADD  CONSTRAINT [DF_posFeedbackQuestionGroupMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posFeedbackQuestionMaster] ADD  CONSTRAINT [DF_posFeedbackQuestionMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posFeedbackQuestionMaster] ADD  CONSTRAINT [DF_posFeedbackQuestionMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posFinancialYearMaster] ADD  CONSTRAINT [DF_posFinancialYearMaster_IsDefault]  DEFAULT ((0)) FOR [IsDefault]
GO
ALTER TABLE [dbo].[posItemMaster] ADD  CONSTRAINT [DF_posItemMaster_IsFavourite]  DEFAULT ((0)) FOR [IsFavourite]
GO
ALTER TABLE [dbo].[posItemMaster] ADD  CONSTRAINT [DF_posItemMaster_ItemPoint]  DEFAULT ((0)) FOR [ItemPoint]
GO
ALTER TABLE [dbo].[posItemMaster] ADD  CONSTRAINT [DF_posItemMaster_PriceByPoint]  DEFAULT ((0)) FOR [PriceByPoint]
GO
ALTER TABLE [dbo].[posItemMaster] ADD  CONSTRAINT [DF_posItemMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posItemMaster] ADD  CONSTRAINT [DF_posItemMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posItemMaster] ADD  CONSTRAINT [DF_posItemMaster_IsRowMaterial]  DEFAULT ((0)) FOR [ItemType]
GO
ALTER TABLE [dbo].[posItemMaster] ADD  CONSTRAINT [DF_posItemMaster_CreateDateTime]  DEFAULT (getdate()) FOR [CreateDateTime]
GO
ALTER TABLE [dbo].[posItemMaster] ADD  CONSTRAINT [DF_posItemMaster_IsDineInOnly]  DEFAULT ((0)) FOR [IsDineInOnly]
GO
ALTER TABLE [dbo].[posItemRateTran] ADD  CONSTRAINT [DF_posItemRateTran_SeleRate]  DEFAULT ((0)) FOR [SaleRate]
GO
ALTER TABLE [dbo].[posItemRateTran] ADD  CONSTRAINT [DF_posItemRateTran_Tax1]  DEFAULT ((0)) FOR [Tax1]
GO
ALTER TABLE [dbo].[posItemRateTran] ADD  CONSTRAINT [DF_posItemRateTran_Tax2]  DEFAULT ((0)) FOR [Tax2]
GO
ALTER TABLE [dbo].[posItemRateTran] ADD  CONSTRAINT [DF_posItemRateTran_Tax3]  DEFAULT ((0)) FOR [Tax3]
GO
ALTER TABLE [dbo].[posItemRateTran] ADD  CONSTRAINT [DF_posItemRateTran_Tax4]  DEFAULT ((0)) FOR [Tax4]
GO
ALTER TABLE [dbo].[posItemRateTran] ADD  CONSTRAINT [DF_posItemRateTran_Tax5]  DEFAULT ((0)) FOR [Tax5]
GO
ALTER TABLE [dbo].[posItemStockTran] ADD  CONSTRAINT [DF_posItemStockTran_IsMaintainStock]  DEFAULT ((0)) FOR [IsMaintainStock]
GO
ALTER TABLE [dbo].[posMenuMaster] ADD  CONSTRAINT [DF_posMenuMaster_IsShowInQuickLaunch_1]  DEFAULT ((0)) FOR [IsShowInQuickLaunch]
GO
ALTER TABLE [dbo].[posMenuMaster] ADD  CONSTRAINT [DF_posMenuMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posMiscExpenseCategoryMaster] ADD  CONSTRAINT [DF_posMiscExpenseCategoryMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posMiscIncomeCategoryMaster] ADD  CONSTRAINT [DF_posMiscIncomeCategoryMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posModifierMaster] ADD  CONSTRAINT [DF_posModifierMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posModifierMaster] ADD  CONSTRAINT [DF_posModifierMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posOfferDaysTran] ADD  CONSTRAINT [DF_posOfferDaysTran_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posOfferMaster] ADD  CONSTRAINT [DF_posOfferMaster_IsDiscountPercentage]  DEFAULT ((0)) FOR [IsDiscountPercentage]
GO
ALTER TABLE [dbo].[posOfferMaster] ADD  CONSTRAINT [DF_posOfferMaster_CreateDateTime]  DEFAULT (getdate()) FOR [CreateDateTime]
GO
ALTER TABLE [dbo].[posOfferMaster] ADD  CONSTRAINT [DF_posOfferMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posOfferMaster] ADD  CONSTRAINT [DF_posOfferMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posOfferMaster] ADD  CONSTRAINT [DF_posOfferMaster_IsForCustomers]  DEFAULT ((0)) FOR [IsForCustomers]
GO
ALTER TABLE [dbo].[posOptionValueTran] ADD  CONSTRAINT [DF_posOptionValueTran_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posOrderItemTran] ADD  CONSTRAINT [DF_posOrderItemTran_DiscountPercentage]  DEFAULT ((0)) FOR [DiscountPercentage]
GO
ALTER TABLE [dbo].[posOrderItemTran] ADD  CONSTRAINT [DF_posOrderItemTran_DiscountAmount]  DEFAULT ((0)) FOR [DiscountAmount]
GO
ALTER TABLE [dbo].[posOrderItemTran] ADD  CONSTRAINT [DF_posOrderItemTran_IsRateTaxInclusive]  DEFAULT ((0)) FOR [IsRateTaxInclusive]
GO
ALTER TABLE [dbo].[posOrderMaster] ADD  CONSTRAINT [DF_posOrderMaster_NetAmount]  DEFAULT ((0)) FOR [NetAmount]
GO
ALTER TABLE [dbo].[posOrderMaster] ADD  CONSTRAINT [DF_posOrderMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posOrderMaster] ADD  CONSTRAINT [DF_posOrderMaster_PrintCount]  DEFAULT ((0)) FOR [PrintCount]
GO
ALTER TABLE [dbo].[posOrderStatusMaster] ADD  CONSTRAINT [DF_posOrderStatusMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posPaymentTypeMaster] ADD  CONSTRAINT [DF_posPaymentTypeMaster_IsDefault]  DEFAULT ((0)) FOR [IsDefault]
GO
ALTER TABLE [dbo].[posPaymentTypeMaster] ADD  CONSTRAINT [DF_posPaymentTypeMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posPurchaseItemTran] ADD  CONSTRAINT [DF_posPurchaseItemTran_Discount]  DEFAULT ((0)) FOR [Discount]
GO
ALTER TABLE [dbo].[posRegisteredUserMaster] ADD  CONSTRAINT [DF_posRegisteredUserMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posRoleMaster] ADD  CONSTRAINT [DF_posRoleMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posSalesItemTran] ADD  CONSTRAINT [DF_posSalesItemTran_Tax1]  DEFAULT ((0)) FOR [Tax1]
GO
ALTER TABLE [dbo].[posSalesItemTran] ADD  CONSTRAINT [DF_posSalesItemTran_Tax2]  DEFAULT ((0)) FOR [Tax2]
GO
ALTER TABLE [dbo].[posSalesItemTran] ADD  CONSTRAINT [DF_posSalesItemTran_Tax3]  DEFAULT ((0)) FOR [Tax3]
GO
ALTER TABLE [dbo].[posSalesItemTran] ADD  CONSTRAINT [DF_posSalesItemTran_Tax4]  DEFAULT ((0)) FOR [Tax4]
GO
ALTER TABLE [dbo].[posSalesItemTran] ADD  CONSTRAINT [DF_posSalesItemTran_Tax5]  DEFAULT ((0)) FOR [Tax5]
GO
ALTER TABLE [dbo].[posSalesMaster] ADD  CONSTRAINT [DF_posSalesMaster_IsComplementry]  DEFAULT ((0)) FOR [IsComplimentary]
GO
ALTER TABLE [dbo].[posSalesMaster] ADD  CONSTRAINT [DF_posSalesMaster_TotalItemPoint]  DEFAULT ((0)) FOR [TotalItemPoint]
GO
ALTER TABLE [dbo].[posSalesMaster] ADD  CONSTRAINT [DF_posSalesMaster_TotalDeductedPoint]  DEFAULT ((0)) FOR [TotalDeductedPoint]
GO
ALTER TABLE [dbo].[posSalesPaymentTran] ADD  CONSTRAINT [DF_posSalesPaymentTran_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posSupplierMaster] ADD  CONSTRAINT [DF_posSupplierMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posSupplierMaster] ADD  CONSTRAINT [DF_posSupplierMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posSupplierPaymentTran] ADD  CONSTRAINT [DF_posSupplierPaymentTran_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posTableMaster] ADD  CONSTRAINT [DF_posTableMaster_TableType]  DEFAULT ((0)) FOR [TableType]
GO
ALTER TABLE [dbo].[posTableMaster] ADD  CONSTRAINT [DF_posTableMaster_IsBooking]  DEFAULT ((0)) FOR [IsBookingAvailable]
GO
ALTER TABLE [dbo].[posTableMaster] ADD  CONSTRAINT [DF_posTableMaster_IsEnable]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posTableMaster] ADD  CONSTRAINT [DF_posTableMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posTaxMaster] ADD  CONSTRAINT [DF_posTaxMaster_IsEnable]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posUnitMaster] ADD  CONSTRAINT [DF_posUnitMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posUnitMaster] ADD  CONSTRAINT [DF_posUnitMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posUserMaster] ADD  CONSTRAINT [DF_posUserMaster_LoginFailCount]  DEFAULT ((0)) FOR [LoginFailCount]
GO
ALTER TABLE [dbo].[posUserMaster] ADD  CONSTRAINT [DF_posUserMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posUserMaster] ADD  CONSTRAINT [DF_posUserMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
ALTER TABLE [dbo].[posWaiterMaster] ADD  CONSTRAINT [DF_posWaiterMaster_IsEnabled]  DEFAULT ((1)) FOR [IsEnabled]
GO
ALTER TABLE [dbo].[posWaiterMaster] ADD  CONSTRAINT [DF_posWaiterMaster_IsDeleted]  DEFAULT ((0)) FOR [IsDeleted]
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'0' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'posPurchaseItemTran', @level2type=N'COLUMN',@level2name=N'Discount'
GO
USE [master]
GO
ALTER DATABASE [abPOS] SET  READ_WRITE 
GO
