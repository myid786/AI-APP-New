param(
    [string]$Repo = "myid786/AI-APP-New",
    [string]$ResourceGroup = "rg-ai-media-platform",
    [string]$StorageAccount = "aiappmedia786",
    [string]$AcrName = "aimediaacr786"
)

$ErrorActionPreference = "Stop"

$Gh = (Get-Command gh -ErrorAction SilentlyContinue).Source
if ([string]::IsNullOrWhiteSpace($Gh)) {
    $DefaultGh = "C:\Program Files\GitHub CLI\gh.exe"
    if (Test-Path $DefaultGh) {
        $Gh = $DefaultGh
    } else {
        throw "GitHub CLI was not found. Install it or add gh.exe to PATH."
    }
}

function Set-RepoSecret {
    param(
        [string]$Name,
        [string]$Value
    )

    if ([string]::IsNullOrWhiteSpace($Value)) {
        throw "Secret $Name has an empty value."
    }

    $Value | & $Gh secret set $Name --repo $Repo
    Write-Host "Set $Name"
}

function Get-PublishProfile {
    param([string]$AppName)

    az webapp deployment list-publishing-profiles `
        --resource-group $ResourceGroup `
        --name $AppName `
        --xml
}

Write-Host "Checking GitHub CLI authentication..."
& $Gh auth status

Write-Host "Setting storage and ACR secrets..."
$storageConnection = az storage account show-connection-string `
    --resource-group $ResourceGroup `
    --name $StorageAccount `
    --query connectionString `
    -o tsv
Set-RepoSecret "AZURE_STORAGE_CONNECTION_STRING" $storageConnection

$acrUser = az acr credential show --name $AcrName --query username -o tsv
$acrPass = az acr credential show --name $AcrName --query "passwords[0].value" -o tsv
Set-RepoSecret "ACR_USERNAME" $acrUser
Set-RepoSecret "ACR_PASSWORD" $acrPass

Write-Host "Setting Web App publish profile secrets..."
$profiles = @{
    "AZURE_WEBAPP_PUBLISH_PROFILE_DISCOVERY"    = "ai-media-discovery-786"
    "AZURE_WEBAPP_PUBLISH_PROFILE_AUTH"         = "ai-media-auth-786"
    "AZURE_WEBAPP_PUBLISH_PROFILE_USER"         = "ai-media-user-786"
    "AZURE_WEBAPP_PUBLISH_PROFILE_POST"         = "ai-media-post-786"
    "AZURE_WEBAPP_PUBLISH_PROFILE_INTERACTION"  = "ai-media-interaction-786"
    "AZURE_WEBAPP_PUBLISH_PROFILE_MEDIA"        = "ai-media-media-786"
    "AZURE_WEBAPP_PUBLISH_PROFILE_AI"           = "ai-media-ai-786"
    "AZURE_WEBAPP_PUBLISH_PROFILE_GATEWAY"      = "ai-media-gateway-786"
}

foreach ($item in $profiles.GetEnumerator()) {
    $profile = Get-PublishProfile $item.Value
    Set-RepoSecret $item.Key $profile
}

Write-Host "All GitHub Actions secrets have been set for $Repo."
