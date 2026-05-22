using System;
using Microsoft.AspNetCore.Builder;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using ooadepazar.Data;
using ooadepazar.Models;
using Microsoft.AspNetCore.Http.Features;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllersWithViews();

builder.Services.AddDbContext<ApplicationDbContext>(options =>
       options.UseSqlite("Data Source=local.db"));

builder.Services.AddDefaultIdentity<ApplicationUser>(options => options.SignIn.RequireConfirmedAccount = false)
    .AddRoles<IdentityRole>()
    .AddEntityFrameworkStores<ApplicationDbContext>();
builder.Services.AddScoped<IMailService, MailService>();
builder.WebHost.ConfigureKestrel(serverOptions =>
{
    serverOptions.Limits.MaxRequestBodySize = 104857600; // 100 MB
});

builder.Services.Configure<FormOptions>(options =>
{
    options.MultipartBodyLengthLimit = 104857600; // 100 MB, promeni po potrebi
});
var app = builder.Build();

using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<ApplicationDbContext>();
    var userManager = scope.ServiceProvider.GetRequiredService<UserManager<ApplicationUser>>();
    var roleManager = scope.ServiceProvider.GetRequiredService<RoleManager<IdentityRole>>();

    // Dodaj admin rolu ako ne postoji
    if (!await roleManager.RoleExistsAsync("Admin"))
        await roleManager.CreateAsync(new IdentityRole("Admin"));

    // Dodaj admin korisnika ako ne postoji
    var adminUser = await userManager.FindByNameAsync("admin");
    if (adminUser == null)
    {
        adminUser = new ApplicationUser
        {
            UserName = "admin",
            Email = "admin@example.com",
            Ime = "Admin",
            Prezime = "Adminić",
            Adresa = "Admin Adresa",
            EmailAdresa = "admin@example.com",
            BrojTelefona = "000000000",
            Uloga = Uloga.Admin,
            DatumRegistracije = DateTime.Now
        };
        // Lozinka: Admin123!
        await userManager.CreateAsync(adminUser, "Admin123!");
        await userManager.AddToRoleAsync(adminUser, "Admin");
    }

    // Dodaj test user-a kao i do sada
    var user = await userManager.FindByNameAsync("testuser");
    if (user == null)
    {
        user = new ApplicationUser
        {
            UserName = "testuser",
            Email = "test@example.com",
            Ime = "Test",
            Prezime = "User",
            Adresa = "Test Address",
            EmailAdresa = "test@example.com",
            BrojTelefona = "123456789",
            Uloga = Uloga.Korisnik,
            DatumRegistracije = DateTime.Now
        };
        await userManager.CreateAsync(user, "Test123!");
    }

    // Dodaj test artikal
    if (!db.Artikal.Any())
    {
        db.Artikal.Add(new Artikal
        {
            Naziv = "Test Artikal",
            Stanje = Stanje.Novo,
            Opis = "Ovo je test artikal.",
            Cijena = 100,
            Kategorija = Kategorija.Elektronika,
            Lokacija = "Test Lokacija",
            DatumObjave = DateTime.Now,
            DatumAzuriranja = DateTime.Now,
            Narucen = false,
            Korisnik = user
        });
        db.SaveChanges();
    }
}


// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseStatusCodePagesWithReExecute("/Error/{0}");

app.UseHttpsRedirection();
app.UseStaticFiles(); // Make sure to include this for serving static files

app.UseRouting();

app.UseAuthentication(); // Add authentication middleware
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.MapRazorPages(); // If you are using Razor Pages for Identity

app.Run();