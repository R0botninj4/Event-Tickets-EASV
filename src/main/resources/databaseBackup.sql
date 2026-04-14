create table Users
(
    UserID       int identity
        primary key,
    Name         nvarchar(100) not null,
    PhoneNumber  nvarchar(20),
    Email        nvarchar(100)
        unique,
    Role         nvarchar(50)
        check ([Role] = 'Customer' OR [Role] = 'Coordinator' OR [Role] = 'Admin'),
    CreatedAt    datetime default getdate(),
    Username     nvarchar(50)  not null,
    PasswordHash nvarchar(256) not null,
    Roleint      int           not null,
    DeletedAt    datetime
)
    go

create table Events
(
    EventID          int identity
        primary key,
    EventName        nvarchar(150) not null,
    EventInfo        nvarchar(max),
    EventDate        date          not null,
    EndDate          date,
    EndTime          time,
    Location         nvarchar(200),
    LocationGuidance nvarchar(500),
    TicketAmount     int           not null
        check ([TicketAmount] >= 0),
    TicketsSold      int default 0
        check ([TicketsSold] >= 0),
    CoordinatorID    int
        references Users,
    DeletedAt        datetime
)
    go

create table Tickets
(
    TicketID     int identity
        primary key,
    EventID      int not null
        references Events,
    CustomerID   int not null
        references Users,
    TicketType   nvarchar(50),
    TicketAmount int not null
        check ([TicketAmount] > 0),
    PurchaseDate datetime     default getdate(),
    Email        nvarchar(100),
    Status       nvarchar(20) default 'Active',
    Barcode      nvarchar(100)
)
    go

create table Vouchers
(
    VoucherID   int identity
        primary key,
    VoucherCode nvarchar(50) not null
        unique,
    VoucherType nvarchar(50),
    Discount    varchar(255),
    CreatedAt   datetime default getdate(),
    ValidUntil  date
)
    go

