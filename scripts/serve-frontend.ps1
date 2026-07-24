# serve-frontend.ps1
# Mini web server statico (zero dipendenze) per servire il frontend di TicketStadio
# su una porta locale, cosi' che le chiamate API verso il backend (localhost:8080)
# rispettino le regole CORS (origine http://localhost:*).
#
# Uso:  powershell -ExecutionPolicy Bypass -File serve-frontend.ps1 [-Port 5500] [-Root <cartella>]

param(
    [int]$Port = 5500,
    [string]$Root = "C:\dev\TicketStadio\frontend"
)

$ErrorActionPreference = 'Stop'
$RootFull = [System.IO.Path]::GetFullPath($Root)

if (-not (Test-Path $RootFull)) {
    Write-Host "[ERRORE] Cartella frontend non trovata: $RootFull"
    exit 1
}

$mime = @{
    '.html'='text/html; charset=utf-8';       '.htm' ='text/html; charset=utf-8';
    '.css' ='text/css; charset=utf-8';         '.js'  ='application/javascript; charset=utf-8';
    '.mjs' ='application/javascript; charset=utf-8'; '.json'='application/json; charset=utf-8';
    '.map' ='application/json';                 '.svg' ='image/svg+xml';
    '.png' ='image/png';                        '.jpg' ='image/jpeg';
    '.jpeg'='image/jpeg';                       '.gif' ='image/gif';
    '.ico' ='image/x-icon';                     '.webp'='image/webp';
    '.woff'='font/woff';                        '.woff2'='font/woff2';
    '.ttf' ='font/ttf';                         '.txt' ='text/plain; charset=utf-8'
}

$listener = New-Object System.Net.HttpListener
$listener.Prefixes.Add("http://localhost:$Port/")
try {
    $listener.Start()
} catch {
    Write-Host "[ERRORE] Impossibile avviare il server sulla porta $Port."
    Write-Host "         $($_.Exception.Message)"
    Write-Host "         (la porta potrebbe essere gia' occupata)"
    exit 1
}

Write-Host "============================================"
Write-Host "  Frontend TicketStadio attivo"
Write-Host "  URL : http://localhost:$Port/"
Write-Host "  Root: $RootFull"
Write-Host "  (chiudi questa finestra per fermare il frontend)"
Write-Host "============================================"

while ($listener.IsListening) {
    try {
        $ctx = $listener.GetContext()
    } catch {
        break
    }
    $req = $ctx.Request
    $res = $ctx.Response
    try {
        $rel = [System.Uri]::UnescapeDataString($req.Url.AbsolutePath)
        if ([string]::IsNullOrEmpty($rel) -or $rel -eq '/') { $rel = '/index.html' }
        $rel = $rel.TrimStart('/')
        $candidate = [System.IO.Path]::GetFullPath((Join-Path $RootFull $rel))

        # Protezione contro path traversal (../)
        if (-not $candidate.StartsWith($RootFull, [System.StringComparison]::OrdinalIgnoreCase)) {
            $res.StatusCode = 403
        }
        elseif ((Test-Path $candidate) -and -not (Get-Item $candidate).PSIsContainer) {
            $ext = [System.IO.Path]::GetExtension($candidate).ToLowerInvariant()
            $ct  = $mime[$ext]
            if (-not $ct) { $ct = 'application/octet-stream' }
            $bytes = [System.IO.File]::ReadAllBytes($candidate)
            $res.ContentType = $ct
            $res.ContentLength64 = $bytes.Length
            $res.OutputStream.Write($bytes, 0, $bytes.Length)
        }
        else {
            $res.StatusCode = 404
            $body = [System.Text.Encoding]::UTF8.GetBytes("404 Not Found: /$rel")
            $res.ContentType = 'text/plain; charset=utf-8'
            $res.OutputStream.Write($body, 0, $body.Length)
        }
    } catch {
        try { $res.StatusCode = 500 } catch {}
    } finally {
        try { $res.OutputStream.Close() } catch {}
    }
}
